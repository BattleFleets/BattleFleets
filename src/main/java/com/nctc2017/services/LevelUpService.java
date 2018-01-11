package com.nctc2017.services;


import com.nctc2017.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class LevelUpService {
    @Autowired
    PlayerDao playerDao;

    private static final int upPassiveIncome = 50;
    private static final int upMaxShips = 1;
    private static final int upNxtLvl = 5;
    private static final int factor = 100;
    private static final double ratio = 1.1;
    private static final int updatelvl = 1;


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
        int curPoints = getCurrentPoints(playerId);
        double maxCurPoints = Math.ceil(factor*Math.pow(ratio, curLvl));
        return (int)(maxCurPoints-curPoints);
    }

    public void pointsUp(BigInteger playerId, int points) {
       double diff = 0;
       int curLvl = getCurrentLevel(playerId);
       int newPoints = points + getCurrentPoints(playerId);
       double maxCurPoints = Math.ceil(factor*Math.pow(ratio, curLvl));
       if(newPoints<maxCurPoints){
           playerDao.updatePoints(playerId, points);
       }
       else{
         diff = newPoints-maxCurPoints;
         levelUp(playerId,curLvl+updatelvl);
         pointsUp(playerId, (int)diff);
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