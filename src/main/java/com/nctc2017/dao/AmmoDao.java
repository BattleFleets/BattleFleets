package com.nctc2017.dao;

import java.util.*;

import com.nctc2017.bean.Ammo;

/**
 * 
 */
public class AmmoDao {

    /**
     * Default constructor
     */
    public AmmoDao() {
    }



    /**
     * @param int idAmmo 
     * @return
     */
    public Ammo findById(int idAmmo) {
        // TODO implement here
        return null;
    }

    /**
     * @param int id 
     * @return
     */
    public String getAmmoName(int id) {
        // TODO implement here
        return "";
    }

    /**
     * @param int id 
     * @return
     */
    public String getAmmoDamageType(int id) {
        // TODO implement here
        return "";
    }

    /**
     * @param int id 
     * @return
     */
    public int getAmmoCost(int id) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int id 
     * @return
     */
    public int getAmmoQuantity(int id) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int id 
     * @param int increaseNumber 
     * @return
     */
    public boolean increaseAmmoQuantity(int id, int increaseNumber) {
        // TODO implement here
        return false;
    }

    /**
     * @param int id 
     * @param int decreaseNumber 
     * @return
     */
    public boolean decreaseAmmoQuantity(int id, int decreaseNumber) {
        // TODO implement here
        return false;
    }

    /**
     * @param int ammoTemplateId 
     * @return
     */
    public int createAmmoAndGetId(int ammoTemplateId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int idStock 
     * @return
     */
    public List<Ammo> getAllAmmoFromStock(int idStock) {
        // TODO implement here
        return null;
    }

    /**
     * @param int idHold 
     * @return
     */
    public List<Ammo> getAllAmmoFromHold(int idHold) {
        // TODO implement here
        return null;
    }

}