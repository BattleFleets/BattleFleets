package com.nctc2017.services;

import java.util.*;

import com.nctc2017.bean.Thing;

/**
 * 
 */
public class CargoMovementService {

    /**
     * Default constructor
     */
    public CargoMovementService() {
    }

    /**
     * @param int shipFromId 
     * @param int shipToId 
     * @return
     */
    public boolean moveCargoBetweenPlayers(int shipFromId, int shipToId) {
        // TODO implement here
        return false;
    }

    /**
     * @param int cargoId 
     * @param int destinationId 
     * @param int quantity
     */
    public void moveCargoTo(int cargoId, int destinationId, int quantity) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @param int shipId 
     * @return
     */
    public List<Thing> getCargoFromHold(int playerId, int shipId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int playerId 
     * @return
     */
    public List<Thing> getCargoFromStock(int playerId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int playerId 
     * @param int shipId 
     * @return
     */
    public List<Thing> getCargoFromShip(int playerId, int shipId) {
        // TODO implement here
        return null;
    }

}