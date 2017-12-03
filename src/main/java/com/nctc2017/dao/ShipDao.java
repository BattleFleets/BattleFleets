package com.nctc2017.dao;

import java.util.*;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;

/**
 * 
 */
public class ShipDao {

    /**
     * Default constructor
     */
    public ShipDao() {
    }

    /**
     * @param int shipId 
     * @return
     */
    public Ship findShip(int shipId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int shipTemplateId 
     * @return
     */
    public int createNewShip(int shipTemplateId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int shipId 
     * @return
     */
    public boolean deleteShip(int shipId) {
        // TODO implement here
        return false;
    }

    /**
     * @param int shipId 
     * @param int newShipName 
     * @return
     */
    public boolean updateShipName(int shipId, int newShipName) {
        // TODO implement here
        return false;
    }

    /**
     * @param int shipId 
     * @param int newhealthNumb 
     * @return
     */
    public boolean updateShipHealth(int shipId, int newhealthNumb) {
        // TODO implement here
        return false;
    }

    /**
     * @param int shipId 
     * @param int newsailorsNumb 
     * @return
     */
    public boolean updateShipSailorsNumber(int shipId, int newsailorsNumb) {
        // TODO implement here
        return false;
    }

    /**
     * @param int shipId 
     * @return
     */
    public String getCurrentShipName(int shipId) {
        // TODO implement here
        return "";
    }

    /**
     * @param int shipId 
     * @return
     */
    public int getCurrentShipHealth(int shipId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int shipId 
     * @return
     */
    public int getCurrentShipSailors(int shipId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int shipId 
     * @return
     */
    public int getHealthLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int shipId 
     * @return
     */
    public int getCarryingLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int shipId 
     * @return
     */
    public int getCannonLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int shipId 
     * @return
     */
    public int getMastLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int shipId 
     * @return
     */
    public int getSailorLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int shipId 
     * @return
     */
    public int getShipCost(int shipId) {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public List<ShipTemplate> findAllShipTemplates() {
        // TODO implement here
        return null;
    }

    /**
     * 
     */
    public List<Ship> findAllShips(List<Integer> shipsId) {
        // TODO implement here
		return null;
    }

    /**
     * @param int mastId 
     * @param int shipId 
     * @return
     */
    public boolean setMastOnShip(int mastId, int shipId) {
        // TODO implement here
        return false;
    }

    /**
     * @param int cannonId 
     * @param int shipId 
     * @return
     */
    public boolean setCannonOnShip(int cannonId, int shipId) {
        // TODO implement here
        return false;
    }

}