package com.nctc2017.services;

import java.util.*;

import com.nctc2017.bean.Battles;
import com.nctc2017.bean.Player;
import com.nctc2017.bean.Ship;

/**
 * 
 */
public class BattleService {
    /**
     * 
     */
    protected Battles battles;
    
    /**
     * Default constructor
     */
    public BattleService() {
    }
    /**
     * 
     */
    public boolean calculateDamage(List<List<Integer>> ammoCannon, int idPlayer) {
        // TODO implement here
		return false;
    }

    /**
     * @param int idPlayer 
     * @return
     */
    public void decreaseOfDistance(int idPlayer) {
        // TODO implement here
    }

    /**
     * @param int idPlayer 
     * @return
     */
    public int getDistance(int idPlayer) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int idPlayer 
     * @return
     */
    public Player boarding(int idPlayer) {
        // TODO implement here
        return null;
    }

    /**
     * @param int idPlayer 
     * @return
     */
    public Player getEnemy(int idPlayer) {
        // TODO implement here
        return null;
    }

    /**
     * @param int idPlayer 
     * @return
     */
    public Ship getShipInBattle(int idPlayer) {
        // TODO implement here
        return null;
    }

    /**
     * @param int idPlayer 
     * @return
     */
    public Ship getEnemyShip(int idPlayer) {
        // TODO implement here
        return null;
    }

}