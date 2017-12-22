package com.nctc2017.services;


import com.nctc2017.bean.Ship;
import com.nctc2017.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class LevelUpService {
@Autowired
PlayerDao playerDao;

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

    public int getPointsUp(BigInteger playerId, int points) {
         playerDao.updatePoints(playerId,points);
        return getCurrentLevel(playerId);
    }

    public int incomeUp(BigInteger playerId) {
        // TODO implement here
        return 0;
    }

    public Ship shipUp(BigInteger playerId) {
        // TODO implement here
        return null;
    }

}