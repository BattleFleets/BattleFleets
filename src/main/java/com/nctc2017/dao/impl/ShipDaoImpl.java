package com.nctc2017.dao.impl;

import java.math.BigInteger;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.dao.utils.JdbcConverter;

public class ShipDaoImpl implements ShipDao {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public Ship findShip(BigInteger shipId) {
        // TODO implement here
        return null;
    }

    @Override
    public BigInteger createNewShip(BigInteger shipTemplateId) {
        // TODO implement here
        return null;
    }

    @Override
    public boolean deleteShip(int shipId) {
        // TODO implement here
        return false;
    }

    @Override
    public boolean updateShipName(int shipId, int newShipName) {
        // TODO implement here
        return false;
    }

    @Override
    public boolean updateShipHealth(int shipId, int newhealthNumb) {
        // TODO implement here
        return false;
    }

    @Override
    public boolean updateShipSailorsNumber(int shipId, int newsailorsNumb) {
        // TODO implement here
        return false;
    }

    @Override
    public String getCurrentShipName(int shipId) {
        // TODO implement here
        return "";
    }

    @Override
    public int getCurrentShipHealth(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
    public int getCurrentShipSailors(BigInteger shipId) {
        // TODO implement here
        return 0;
    }

    @Override
    public int getHealthLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
    public int getCarryingLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
    public int getCannonLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
    public int getMastLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
    public int getSailorLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
    public int getShipCost(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
    public List<ShipTemplate> findAllShipTemplates() {
        // TODO implement here
        return null;
    }

    @Override
    public List<Ship> findAllShips(List<BigInteger> shipsId) {
        // TODO implement here
        return null;
    }

    @Override
    public boolean setMastOnShip(int mastId, int shipId) {
        // TODO implement here
        return false;
    }

    @Override
    public boolean setCannonOnShip(int cannonId, int shipId) {
        // TODO implement here
        return false;
    }
    
    @Override
    public int getMaxShotDistance(BigInteger shipId) {
        return jdbcTemplate.queryForObject(Query.GET_MAX_SHOT_DISTANCE,
                new Object[]{JdbcConverter.toNumber(shipId),
                        JdbcConverter.toNumber(DatabaseAttribute.CANNON_DISTANCE)},
                Integer.class);
    }

    @Override
    public int getSpeed(BigInteger shipId) {
        return jdbcTemplate.queryForObject(Query.GET_CURRENT_SPEED,
                new Object[]{JdbcConverter.toNumber(shipId),
                        JdbcConverter.toNumber(DatabaseObject.MAST_OBJTYPE_ID),
                        JdbcConverter.toNumber(DatabaseAttribute.ATTR_CURR_MAST_SPEED_ID)},
                Integer.class);
    }
}