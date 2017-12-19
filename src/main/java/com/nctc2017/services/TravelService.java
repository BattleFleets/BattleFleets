package com.nctc2017.services;

import java.math.BigInteger;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.services.utils.BattleManager;
import com.nctc2017.services.utils.TravelManager;

@Service
public class TravelService {

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
        return travelManager.prepareEnemyFor(playerId);
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

    public int getAutoDecisionTime() {
        return AutoDecisionTask.DELAY;
    }
    
    public int autoDecisionTimer(BigInteger playerId) {
        Runnable decisionTask = new AutoDecisionTask(playerId);
        Thread decisionThread = new Thread(decisionTask);
        decisionThread.start();
        playerAutoDecision.put(playerId, decisionThread);
        return 0;
    }

    private class AutoDecisionTask implements Runnable{
        private static final int DELAY = 50000;
        private BigInteger playerId;

        public AutoDecisionTask(BigInteger playerId) {
            this.playerId = playerId;
        }

        @Override
        public void run() {
            try {
                this.wait(DELAY);
            } catch (InterruptedException e) {
                return;
            }
            confirmAttack(playerId, true);
        }
        
    }
}