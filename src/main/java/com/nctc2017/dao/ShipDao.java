package com.nctc2017.dao;

import java.math.BigInteger;
import java.util.List;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;

public interface ShipDao {

    Ship findShip(int shipId);

    BigInteger createNewShip(BigInteger shipTemplateId);

    boolean deleteShip(int shipId);

    boolean updateShipName(int shipId, int newShipName);

    boolean updateShipHealth(int shipId, int newhealthNumb);

    boolean updateShipSailorsNumber(int shipId, int newsailorsNumb);

    String getCurrentShipName(int shipId);

    int getCurrentShipHealth(int shipId);

    int getCurrentShipSailors(int shipId);

    int getHealthLimit(int shipId);

    int getCarryingLimit(int shipId);

    int getCannonLimit(int shipId);

    int getMastLimit(int shipId);

    int getSailorLimit(int shipId);

    int getShipCost(int shipId);

    List<ShipTemplate> findAllShipTemplates();

    List<Ship> findAllShips(List<Integer> shipsId);

    boolean setMastOnShip(int mastId, int shipId);

    boolean setCannonOnShip(int cannonId, int shipId);

}