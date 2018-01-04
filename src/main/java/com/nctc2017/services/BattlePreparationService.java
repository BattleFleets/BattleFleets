package com.nctc2017.services;

import java.math.BigInteger;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Battle;
import com.nctc2017.bean.Cannon;
import com.nctc2017.bean.Ship;
import com.nctc2017.controllers.BattlesController;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.exception.BattleEndException;
import com.nctc2017.services.utils.AutoDecisionTask;
import com.nctc2017.services.utils.BattleManager;
import com.nctc2017.services.utils.Visitor;

@Service
@Transactional
public class BattlePreparationService {
    private static final Logger LOG = Logger.getLogger(BattlePreparationService.class);
    private static final int DELAY = 60000;
    
    @Autowired
    protected BattleManager battles;    
    
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ShipDao shipDao;
    @Autowired
    private CannonDao cannonDao;
    
    private Random randomShip = new Random(System.currentTimeMillis());
    private Map<BigInteger, Thread> playerChoiceShipTimer = new HashMap<>();
    
    public boolean escape(BigInteger playerId) {
        BigInteger enemyId = battles.getEnemyId(playerId);
        if (fleetSpeed(playerId) > fleetSpeed(enemyId)) {
            battles.endBattle(playerId);
            return true;
        }
        return false;
    }
    
    private int fleetSpeed(BigInteger playerId) {
        return playerDao.getFleetSpeed(playerId);
    }
    
    public List<ShipWrapper> getShipsExtraInfo(BigInteger playerId) {
        List<BigInteger> listShipsId = playerDao.findAllShip(playerId);
        List<ShipWrapper> shipInfo = new ArrayList<ShipWrapper>();
        for (BigInteger shipId : listShipsId) {
            shipInfo.add(new ShipWrapper(
                    shipDao.findShip(shipId), 
                    cannonDao.getCurrentQuantity(shipId)));
        }
        return shipInfo;
    }
    
    public List<Ship> getShips(BigInteger playerId) {
        List<BigInteger> listShipsId = playerDao.findAllShip(playerId);
        return shipDao.findAllShips(listShipsId);
    }

    public List<Ship> getEnemyShips(BigInteger playerId) {
        BigInteger enemyId = battles.getEnemyId(playerId);
        return getShips(enemyId);
    }

    public void chooseShip(BigInteger playerId, BigInteger shipId) {
        Thread timer = playerChoiceShipTimer.get(playerId);
        if (timer != null && timer.isAlive()) {
            timer.interrupt();
            LOG.debug("Player_" + playerId + " auto ship timer stoped. ");
        }
        Battle battle = battles.getBattle(playerId);
        battle.setShipId(playerId, shipId);
        int maxDist = shipDao.getMaxShotDistance(shipId);
        if (battle.getDistance() < maxDist) {
            battle.setDistance(maxDist);
            LOG.debug("Player_" + playerId + " reset distance " + maxDist);
        }
        LOG.debug("Player_" + playerId + " chose ship " + shipId);
    }

    public void setReady(BigInteger playerId) {
        Battle battle = battles.getBattle(playerId);
        battle.setReady(playerId, true);
        LOG.debug("Player_" + playerId + " Ready to fight!");
    }
    
    public boolean waitForEnemyReady(BigInteger playerId) throws BattleEndException {
        Battle battle = battles.getBattle(playerId);
        if (battle == null) {
            BattleEndException ex = new BattleEndException("Your enemy run away.");
                LOG.warn("Battle not found! ", ex);
            throw ex;
        }
        boolean ready = battle.isEnemyReady(playerId);
        LOG.debug("Player_" + playerId + " ask for enemy ready. Ready: " + ready);
        return ready;
    }

    public int autoChoiceShipTimer(BigInteger playerId) {
        Runnable decisionTask = new AutoDecisionTask(new ShipVisitor(playerId), DELAY);
        Thread decisionThread = new Thread(decisionTask);
        decisionThread.start();
        LOG.debug("Player_" + playerId + " auto choice ship timer started");
        return DELAY/1000;
    }
    
    public List<BigInteger> getShipsLeftBattle(BigInteger playerId) {
        return battles.getBattle(playerId).getShipsLeftBattle(playerId);
    }
    
    private class ShipVisitor implements Visitor{
        
        private BigInteger playerId;
        
        public ShipVisitor(BigInteger playerId) {
            this.playerId = playerId;
        }

        @Override
        public void visit() {
            LOG.debug("Ship choosing TIMEOUT for Player_" + playerId);
            List<BigInteger> ships = playerDao.findAllShip(playerId);
            ships.removeAll(getShipsLeftBattle(playerId));
            LOG.debug("Player_" + playerId + "has ships: " + ships.size());
            BigInteger shipId = ships.get(randomShip.nextInt(ships.size()));
            LOG.debug("Player_" + playerId + " choose ship with id=" + shipId);
            chooseShip(playerId, shipId);
            setReady(playerId);
        }
        
    }
    
    public class ShipWrapper {
        private Ship ship;
        private Map<String, String> cannons;
        public ShipWrapper(Ship ship, Map<String, String> cannons) {
            this.ship = ship;
            this.cannons = cannons;
        }
        public Ship getShip() {
            return ship;
        }
        public Map<String, String> getCannons() {
            return cannons;
        }
        
    }
}