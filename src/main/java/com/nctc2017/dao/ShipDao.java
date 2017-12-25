package com.nctc2017.dao;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;

import java.math.BigInteger;
import java.util.List;

public interface ShipDao {

    Ship findShip(BigInteger shipId);

    ShipTemplate findShipTemplate(BigInteger shipId);

    BigInteger createNewShip(BigInteger shipTemplateId,BigInteger playerId);

    boolean deleteShip(BigInteger shipId);

    boolean updateShipName(BigInteger shipId, String newShipName);

    boolean updateShipHealth(BigInteger shipId, int newHealthNumb);

    boolean updateShipSailorsNumber(BigInteger shipId, int newSailorsNumb);

    String getCurrentShipName(BigInteger shipId);

    int getCurrentShipHealth(BigInteger shipId);

    int getCurrentShipSailors(BigInteger shipId);

    int getHealthLimit(BigInteger shipId);

    int getCarryingLimit(BigInteger shipId);

    int getCannonLimit(BigInteger shipId);

    int getMastLimit(BigInteger shipId);

    int getSailorLimit(BigInteger shipId);

    int getShipCost(BigInteger shipId);

    List<ShipTemplate> findAllShipTemplates();

    List<Ship> findAllShips(List<BigInteger> shipsId);

    boolean setMastOnShip(BigInteger mastId, BigInteger shipId);

    boolean setCannonOnShip(BigInteger cannonId, BigInteger shipId);

    int getMaxShotDistance(BigInteger shipId);
    
    int getSpeed(BigInteger shipId);

}