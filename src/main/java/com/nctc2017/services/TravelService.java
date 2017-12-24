package com.nctc2017.services;

import java.math.BigInteger;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.impl.HoldDaoImpl;
import com.nctc2017.services.utils.AutoDecisionTask;
import com.nctc2017.services.utils.BattleManager;
import com.nctc2017.services.utils.TravelManager;
import com.nctc2017.services.utils.Visitor;

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
    
    private Map<BigInteger, Thread> playerAutoDecision = new HashMap<>();

    public void relocate(BigInteger playerId, BigInteger cityId) {
        Player player = playerDao.findPlayerById(playerId);
        BigInteger curCityId = playerDao.getPlayerCity(playerId);
        if (curCityId.equals(cityId)) {
            RuntimeException ex = new IllegalArgumentException("You cannot move to the same city.");
            LOG.warn("Exception while relocating from city with id = " + curCityId 
                    + "to city with id = " + cityId, ex);
            throw ex;
        }
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

    public boolean isEnemyOnHorizon(BigInteger playerId) {
        if(travelManager.prepareEnemyFor(playerId)) { 
            autoDecisionTimer(playerId);
            return true;
        } else {
            return false;
        }
    }

    public int resumeRelocateTime(BigInteger playerId) {
        return travelManager.continueTravel(playerId);
    }

    public void confirmAttack(BigInteger playerId, boolean decision) {
        playerAutoDecision.get(playerId).interrupt();
        if (decision) {
            BigInteger enemyId = travelManager.getEnemyId(playerId);
            battleManager.newBattleBetween(playerId, enemyId);
        } else {
            travelManager.friendly(playerId);
        }
    }

    public boolean isBattleStart(BigInteger playerId) {
        BigInteger enemyId = battleManager.getEnemyId(playerId);
        if (enemyId == null)
            return false;
        else
            return true;
    }
    
    private int autoDecisionTimer(BigInteger playerId) {
        Runnable decisionTask = new AutoDecisionTask(new DecisionVisitor(playerId), DELAY);
        Thread decisionThread = new Thread(decisionTask);
        decisionThread.start();
        playerAutoDecision.put(playerId, decisionThread);
        return getAutoDecisionTime() / 1000;
    }
    
    public int getAutoDecisionTime() {
        return DELAY / 1000;
    }
    
    private class DecisionVisitor implements Visitor{
        BigInteger playerId;
        public DecisionVisitor(BigInteger playerId) {
            this.playerId = playerId;
        }

        @Override
        public void visit() {
            confirmAttack(playerId, true);
        }
        
    }
    
}