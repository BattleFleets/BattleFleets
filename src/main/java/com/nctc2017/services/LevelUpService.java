package com.nctc2017.services;


import com.nctc2017.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class LevelUpService {
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ScoreService scoreService;

    private static final int upPassiveIncome = 50;
    private static final int upMaxShips = 1;
    private static final int upNxtLvl = 5;
    private static final int factor = 100;
    private static final double ratio = 1.1;
    private static final int updatelvl = 1;
    private static final int zero = 0;


    public int getCurrentLevel(BigInteger playerId) {
        return playerDao.getPlayerLevel(playerId);
    }

    public void levelUp(BigInteger playerId,int level) {
        playerDao.updateLevel(playerId, level);
    }

    public int getCurrentPoints(BigInteger playerId) {
        return playerDao.getPlayerPoints(playerId);
    }

    public int getPointsToNxtLevel(BigInteger playerId){
        int curLvl = getCurrentLevel(playerId);
        double points = Math.floor(factor*Math.pow(ratio, curLvl-1));
        return (int)(points);
    }

    public void pointsUp(BigInteger playerId, int points) {
       double diff;
       int curLvl = getCurrentLevel(playerId);
       if(curLvl!=scoreService.getMaxLvl()) {
           int newPoints = points + getCurrentPoints(playerId);
           double maxCurPoints = Math.ceil(factor * Math.pow(ratio, curLvl - 1));
           if (newPoints < maxCurPoints) {
               playerDao.updatePoints(playerId, newPoints);
           } else {
               diff = newPoints - maxCurPoints;
               levelUp(playerId, curLvl + updatelvl);
               playerDao.updatePoints(playerId, zero);
               if (diff != 0) {
                   pointsUp(playerId, (int) diff);
               }
           }
       }
    }

    public int getPassiveIncome(BigInteger playerId){
        return playerDao.getCurrentPassiveIncome(playerId);
    }

    public void incomeUp(BigInteger playerId) {
       int curPass = playerDao.getCurrentPassiveIncome(playerId);
       playerDao.updatePassiveIncome(playerId,curPass+upPassiveIncome);
    }

    public int getMaxShips(BigInteger playerId){
        return playerDao.getCurrentMaxShips(playerId);
    }

    public void shipUp(BigInteger playerId) {
        int curMaxShips = playerDao.getCurrentMaxShips(playerId);
        playerDao.updateMaxShips(playerId,curMaxShips+upMaxShips);
    }

    public int getNextLevel(BigInteger playerId){
        return playerDao.getNextPlayerLevel(playerId);
    }

    public void updateNxtLvl(BigInteger playerId){
        playerDao.updateNxtLvl(playerId,getNextLevel(playerId)+upNxtLvl);
    }

    public String getLogin(BigInteger playerId){
        return playerDao.getPlayerLogin(playerId);
    }

}