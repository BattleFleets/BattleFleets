package com.nctc2017.dao;

import java.util.*;

import com.nctc2017.bean.Mast;

/**
 * 
 */
public class MastDao {

    /**
     * Default constructor
     */
    public MastDao() {
    }



    /**
     * @param int mastId 
     * @return
     */
    public Mast findMast(int mastId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int mastTemplateId 
     * @return
     */
    public int createNewMast(int mastTemplateId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int mastId 
     * @return
     */
    public void deleteMast(int mastId) {
        // TODO implement here
    }

    /**
     * @param int mastId 
     * @param int newMastSpeed 
     * @return
     */
    public boolean updateCurMastSpeed(int mastId, int newMastSpeed) {
        // TODO implement here
        return false;
    }

    /**
     * @param int shipId 
     * @return
     */
    public List <Mast> getShipMastsFromShip(int shipId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int stockId 
     * @return
     */
    public List <Mast> getShipMastsFromStock(int stockId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int holdId 
     * @return
     */
    public List <Mast> getShipMastsFromHold(int holdId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int mastId 
     * @return
     */
    public int getCurMastSpeed(int mastId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int mastId 
     * @return
     */
    public String getMastName(int mastId) {
        // TODO implement here
        return "";
    }

    /**
     * @param int mastId 
     * @return
     */
    public int getSailyards(int mastId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int mastId 
     * @return
     */
    public int getMaxSpeed(int mastId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int mastId 
     * @return
     */
    public int getMastCost(int mastId) {
        // TODO implement here
        return 0;
    }

}