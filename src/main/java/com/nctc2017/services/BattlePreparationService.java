package com.nctc2017.services;

import java.util.*;

import com.nctc2017.bean.Battles;
import com.nctc2017.bean.Ship;

public class BattlePreparationService {
    protected Battles battles;    
    
    public boolean escape(int playerId) {
        // TODO implement here
        return false;
    }

    public ArrayList<Ship> getShips(int playerId) {
        // TODO implement here
        return null;
    }

    public ArrayList<Ship> getEnemyShips(int playerId) {
        // TODO implement here
        return null;
    }

    public void chooseShip(int shipId) {
        // TODO implement here
    }

    public boolean waitForEnemyReady(int playerId) {
        // TODO implement here
        return false;
    }

    public int autoChoiceShipTimer() {
        // TODO implement here
        return 0;
    }

}