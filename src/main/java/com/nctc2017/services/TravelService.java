package com.nctc2017.services;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.bean.Ship;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.dao.StockDao;
import com.nctc2017.exception.BattleStartException;
import com.nctc2017.exception.PlayerNotFoundException;
import com.nctc2017.services.utils.AutoDecisionTask;
import com.nctc2017.services.utils.BattleManager;
import com.nctc2017.services.utils.TravelManager;
import com.nctc2017.services.utils.Visitor;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class TravelService {
    private static final Logger LOG = Logger.getLogger(TravelService.class);
    private static final int DELAY = 50000;
    
    @Autowired
    private BattleManager battleManager;
    @Autowired
    private TravelManager travelManager;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private CityDao cityDao;
    @Autowired
    private ShipDao shipDao;
    @Autowired
    private StockDao stockDao;
    
    private Map<BigInteger, Thread> playerAutoDecision = new ConcurrentHashMap<>();

    public void relocate(BigInteger playerId, BigInteger cityId) {
        Player player = playerDao.findPlayerById(playerId);
        BigInteger curCityId = playerDao.getPlayerCity(playerId);
        if (curCityId.equals(cityId)) {
            RuntimeException ex = new IllegalArgumentException("You cannot move to the same city.");
            LOG.error("Exception while player " + playerId + " relocating from city with id = " + curCityId 
                    + " to city with id = " + cityId, ex);
            throw ex;
        }
        LOG.debug("Starting relocation");
        travelManager.startJourney(playerId, player.getLevel(), cityId);
    }

    public City getCurrentCity(BigInteger playerId) {
        BigInteger cityId = playerDao.getPlayerCity(playerId);
        City city=cityDao.find(cityId);
        return city;
    }

    public List<City> getCities() {
        List<City> allCity = cityDao.findAll();
        return allCity;
    }

    public int getRelocateTime(BigInteger playerId) {
        return travelManager.getRelocateTime(playerId);
    }

    public boolean isEnemyOnHorizon(BigInteger playerId) throws PlayerNotFoundException {
        if(travelManager.prepareEnemyFor(playerId)) { 
            LOG.debug("Saw enemy on horizon. Two player's decision timers start if not started yet.");
            autoDecisionTimer(playerId);
            BigInteger enemyId = travelManager.getEnemyId(playerId);
            autoDecisionTimer(enemyId);
            return true;
        } else {
            return false;
        }
    }

    public int resumeRelocateTime(BigInteger playerId) throws PlayerNotFoundException {
        LOG.debug("Relocation timer resume.");
        int timeLeft = travelManager.continueTravel(playerId);
        LOG.debug("Time left: " + timeLeft);
        return timeLeft;
    }
    
    private void stopAutoDecisionTimer(BigInteger playerId) {
        Thread timer = playerAutoDecision.get(playerId);
        LOG.debug("Auto Decision timer stoping. timer = null ? " + (timer == null));
        if (timer != null) {
            timer.interrupt();
            playerAutoDecision.remove(playerId);
            LOG.debug("Auto Decision timer stoped");
        }
    }

    public void confirmAttack(BigInteger playerId, boolean decision) throws PlayerNotFoundException, BattleStartException {
        stopAutoDecisionTimer(playerId);
        travelManager.decisionWasMade(playerId);
        LOG.debug("Made decision. Confirm Attack - " + decision);
        if (decision) {
            BigInteger enemyId = travelManager.getEnemyId(playerId);
            stopAutoDecisionTimer(enemyId);
            battleManager.newBattleBetween(playerId, enemyId);
            LOG.debug("Stop relocation timer for enemy");
            travelManager.stopRelocationTimer(enemyId);
            travelManager.setParticipated(playerId);
            travelManager.setParticipated(enemyId);
            LOG.debug("     --==battle created!==--");
        } else {
            LOG.debug("Attack rejecting ...");
            travelManager.friendly(playerId);
            LOG.debug("Attack rejected.");
        }
    }

    public boolean isBattleStart(BigInteger playerId) {
        return battleManager.isBattleStart(playerId);
    }
    
    public boolean isFleetSpeedOk(BigInteger playerId) {
        return playerDao.getFleetSpeed(playerId) > 1;
    }
    
    public boolean isHaveShip(BigInteger playerId) {
        List<BigInteger> shipsId = playerDao.findAllShip(playerId);
        return ! shipsId.isEmpty();
    }
    
    public boolean isSailorsEnough(BigInteger playerId) {
        List<BigInteger> shipsId = playerDao.findAllShip(playerId);
        if (shipsId.isEmpty()) return false;
        for (BigInteger shipId : shipsId) {
            Ship ship = shipDao.findShip(shipId);
            if (ship.getCurSailorsQuantity() < ship.getMaxSailorsQuantity() / 2) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isEmptyStock(BigInteger playerId) {
        return stockDao.getOccupiedVolume(playerId) == 0;
    }
    
    public City getRelocationCity(BigInteger playerId) throws PlayerNotFoundException {
        BigInteger cityId = travelManager.getRelocationCity(playerId);
        return cityDao.find(cityId);
    }
    
    public boolean isDecisionAccept(BigInteger playerId) throws PlayerNotFoundException {
        return travelManager.isDecisionWasMade(playerId);
    }
    
    public boolean isPlayerInTravel(BigInteger playerId) {
        return travelManager.isPlayerInTravel(playerId);
    }

    public boolean isParticipated(BigInteger playerId) throws PlayerNotFoundException {
        return travelManager.isParticipated(playerId);
    }
    
    public void clearStock(BigInteger playerId) {
        stockDao.deleteStock(playerId);
        stockDao.createStock(playerId);
    }

    public int getAutoDecisionTime() {
        return DELAY / 1000;
    }
    
    private void autoDecisionTimer(BigInteger playerId) {
        if (playerAutoDecision.get(playerId) != null) return;
        Runnable decisionTask = new AutoDecisionTask(new DecisionVisitor(playerId), DELAY);
        Thread decisionThread = new Thread(decisionTask);
        decisionThread.start();
        playerAutoDecision.put(playerId, decisionThread);
        LOG.debug("Auto decision timer started");
    }
    
    private class DecisionVisitor implements Visitor{
        BigInteger playerId;
        Object userName;
        
        public DecisionVisitor(BigInteger playerId) {
            this.playerId = playerId;
            this.userName = MDC.get("userName");
        }

        @Override
        public void visit() {
            MDC.put("userName", userName);
            
            LOG.debug("Reject attack by TIMEOUT");
            try {
                confirmAttack(playerId, false);
            } catch (PlayerNotFoundException | BattleStartException e) {
                LOG.warn("Timer could not do rejecting attack", e);
                return;
                
            } finally {
                MDC.remove("userName");
            }
        }
        
    }
    
}