package com.nctc2017.services;

import java.util.*;

import com.nctc2017.bean.Battles;
import com.nctc2017.bean.Ship;

/**
 * 
 */
public class BattlePreparationService {
    /**
     * 
     */
    protected Battles battles;    
    /**
     * Default constructor
     */
    public BattlePreparationService() {
    }
    
    /**
     * @param int playerId 
     * @return
     */
    public boolean escape(int playerId) {
        // TODO implement here
        return false;
    }

    /**
     * @param int playerId 
     * @return
     */
    public ArrayList<Ship> getShips(int playerId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int playerId 
     * @return
     */
    public ArrayList<Ship> getEnemyShips(int playerId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int shipId
     */
    public void chooseShip(int shipId) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @return
     */
    public boolean waitForEnemyReady(int playerId) {
        // TODO implement here
        return false;
    }

    /**
     * @return
     */
    public int autoChoiceShipTimer() {
        // TODO implement here
        return 0;
    }

}