package com.nctc2017.dao.impl;

import java.util.*;

import com.nctc2017.bean.Ammo;
import com.nctc2017.dao.AmmoDao;


public class AmmoDaoImpl implements AmmoDao {
    
    @Override
	public Ammo findById(int idAmmo) {
        // TODO implement here
        return null;
    }

    
    @Override
	public String getAmmoName(int id) {
        // TODO implement here
        return "";
    }

    @Override
	public String getAmmoDamageType(int id) {
        // TODO implement here
        return "";
    }

    @Override
	public int getAmmoCost(int id) {
        // TODO implement here
        return 0;
    }

    @Override
	public int getAmmoQuantity(int id) {
        // TODO implement here
        return 0;
    }

    @Override
	public boolean increaseAmmoQuantity(int id, int increaseNumber) {
        // TODO implement here
        return false;
    }

    @Override
	public boolean decreaseAmmoQuantity(int id, int decreaseNumber) {
        // TODO implement here
        return false;
    }

    @Override
	public int createAmmoAndGetId(int ammoTemplateId) {
        // TODO implement here
        return 0;
    }

    @Override
	public List<Ammo> getAllAmmoFromStock(int idStock) {
        // TODO implement here
        return null;
    }

    @Override
	public List<Ammo> getAllAmmoFromHold(int idHold) {
        // TODO implement here
        return null;
    }

}