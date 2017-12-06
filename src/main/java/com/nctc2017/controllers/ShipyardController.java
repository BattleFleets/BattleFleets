package com.nctc2017.controllers;

import java.util.*;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.Thing;
public class ShipyardController {


    public void buyShip(int playerId, int shipTemplateId) {
        // TODO implement here
    }

    public void isEnoughMoneyForShip(int shipTemplateId) {
        // TODO implement here
    }

    public List<ShipTemplate> getAllShipTemplates() {
        // TODO implement here
        return null;
    }

    public List<Ship> getAllPlayerShips(int playerId) {
        // TODO implement here
        return null;
    }

    public void sellShip(int shipId, int playerId) {
        // TODO implement here
    }

    public List<Thing> getCargoFromHold(int playerId, int shipId) {
        // TODO implement here
        return null;
    }

    public List<Thing> getCargoFromStock(int playerId) {
        // TODO implement here
        return null;
    }

    public List<Thing> getCargoFromShip(int playerId, int shipId) {
        // TODO implement here
        return null;
    }

    public void moveCargoTo(int cargoId, int destinationId, int quantity) {
        // TODO implement here
    }

    public void repairShip(int shipId, int playerId) {
        // TODO implement here
    }

}