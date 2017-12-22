package com.nctc2017.services;

import java.math.BigInteger;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.nctc2017.bean.Battle;
import com.nctc2017.bean.Player;
import com.nctc2017.bean.Ship;
import com.nctc2017.services.utils.BattleManager;

public class BattleService {
    
    @Autowired
    protected BattleManager battles;

    public boolean calculateDamage(List<List<Integer>> ammoCannon, BigInteger playerId) {
        // TODO implement here
        return false;
    }

    public void decreaseOfDistance(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        int dist = battle.getDistance();
        battle.setDistance(dist);
    }

    public int getDistance(BigInteger playerId) {
        // TODO implement here
        return 0;
    }

    public Player boarding(BigInteger playerId) {
        // TODO implement here
        return null;
    }

    public Player getEnemy(BigInteger playerId) {
        // TODO implement here
        return null;
    }

    public Ship getShipInBattle(BigInteger playerId) {
        // TODO implement here
        return null;
    }

    public Ship getEnemyShip(BigInteger playerId) {
        // TODO implement here
        return null;
    }

}