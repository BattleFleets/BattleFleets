package com.nctc2017.dao;

import java.util.*;

import com.nctc2017.bean.Cannon;

/**
 * 
 */
public class CannonDaoImpl implements CannonDao {

    /**
     * Default constructor
     */
    public CannonDaoImpl() {
    }



    /**
     * @param int cannonId 
     * @return
     */
    public Cannon findById(int cannonId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int cannonId 
     * @return
     */
    public String getName(int cannonId) {
        // TODO implement here
        return "";
    }

    /**
     * @param int cannonId 
     * @return
     */
    public int getCost(int cannonId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int cannonId 
     * @return
     */
    public int getDistance(int cannonId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int cannonId 
     * @return
     */
    public int getDamage(int cannonId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int stockId 
     * @return
     */
    public List<Cannon> getAllCannonFromStock(int stockId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int holdId 
     * @return
     */
    public List<Cannon> getAllCannonFromHold(int holdId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int shipId 
     * @return
     */
    public List<Cannon> getAllCannonFromShip(int shipId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int cannonTemplateId 
     * @return
     */
    public int createCannon(int cannonTemplateId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int cannonId 
     * @return
     */
    public void deleteCannon(int cannonId) {
        // TODO implement here
    }

}