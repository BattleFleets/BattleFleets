package com.nctc2017.services;


import com.nctc2017.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class LevelUpService {
    @Autowired
    PlayerDao playerDao;

    private static final int upPassiveIncome=50;
    private static final int upMaxShips=1;

    public int getCurrentLevel(BigInteger playerId) {
        return playerDao.getPlayerLevel(playerId);
    }

    public int levelUp(BigInteger playerId,int level) {
        playerDao.updateLevel(playerId,level);
        return getCurrentLevel(playerId);
    }

    public int getCurrentPoints(BigInteger playerId) {
        return playerDao.getPlayerPoints(playerId);
    }

    public int PointsUp(BigInteger playerId, int points) {
        playerDao.updatePoints(playerId,points);
        return getCurrentPoints(playerId);
    }

    public int getPassiveIncome(BigInteger playerId){
        return playerDao.getCurrentPassiveIncome(playerId);
    }

    public int incomeUp(BigInteger playerId) {
       int curPass=playerDao.getCurrentPassiveIncome(playerId);
       playerDao.updatePassiveIncome(playerId,curPass+upPassiveIncome);
       return playerDao.getCurrentPassiveIncome(playerId);
    }

    public int getMaxShips(BigInteger playerId){
        return playerDao.getCurrentMaxShips(playerId);
    }

    public int shipUp(BigInteger playerId) {
        int curMaxShips=playerDao.getCurrentMaxShips(playerId);
        playerDao.updateMaxShips(playerId,curMaxShips+upMaxShips);
        return playerDao.getCurrentMaxShips(playerId);
    }

}