package com.nctc2017.dao;

import java.math.BigInteger;
import java.util.List;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;

public interface ShipDao {

    Ship findShip(BigInteger shipId);

    BigInteger createNewShip(BigInteger shipTemplateId,BigInteger playerId);

    boolean deleteShip(BigInteger shipId);

    boolean updateShipName(BigInteger shipId, int newShipName);

    boolean updateShipHealth(BigInteger shipId, int newhealthNumb);

    boolean updateShipSailorsNumber(BigInteger shipId, int newsailorsNumb);

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

    List<Ship> findAllShips(List<Integer> shipsId);

    boolean setMastOnShip(BigInteger mastId, BigInteger shipId);

    boolean setCannonOnShip(BigInteger cannonId, BigInteger shipId);

}