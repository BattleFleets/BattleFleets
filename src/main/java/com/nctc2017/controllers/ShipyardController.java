package com.nctc2017.controllers;

import java.util.*;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.Thing;

/**
 * 
 */
public class ShipyardController {

    /**
     * Default constructor
     */
    public ShipyardController() {
    }

    /**
     * @param int playerId 
     * @param int shipTemplateId
     */
    public void buyShip(int playerId, int shipTemplateId) {
        // TODO implement here
    }

    /**
     * @param int shipTemplateId
     */
    public void isEnoughMoneyForShip(int shipTemplateId) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<ShipTemplate> getAllShipTemplates() {
        // TODO implement here
        return null;
    }

    /**
     * @param int playerId 
     * @return
     */
    public List<Ship> getAllPlayerShips(int playerId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int shipId 
     * @param int playerId 
     * @return
     */
    public void sellShip(int shipId, int playerId) {
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

    /**
     * @param int cargoId 
     * @param int destinationId 
     * @param int quantity
     */
    public void moveCargoTo(int cargoId, int destinationId, int quantity) {
        // TODO implement here
    }

    /**
     * @param int shipId 
     * @param int playerId
     */
    public void repairShip(int shipId, int playerId) {
        // TODO implement here
    }

}