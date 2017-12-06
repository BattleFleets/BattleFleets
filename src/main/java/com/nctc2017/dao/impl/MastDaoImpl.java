package com.nctc2017.dao.impl;

import java.util.*;

import com.nctc2017.bean.Mast;
import com.nctc2017.dao.MastDao;

public class MastDaoImpl implements MastDao {

    @Override
	public Mast findMast(int mastId) {
        // TODO implement here
        return null;
    }

    @Override
	public int createNewMast(int mastTemplateId) {
        // TODO implement here
        return 0;
    }


    @Override
	public void deleteMast(int mastId) {
        // TODO implement here
    }


    @Override
	public boolean updateCurMastSpeed(int mastId, int newMastSpeed) {
        // TODO implement here
        return false;
    }


    @Override
	public List <Mast> getShipMastsFromShip(int shipId) {
        // TODO implement here
        return null;
    }


    @Override
	public List <Mast> getShipMastsFromStock(int stockId) {
        // TODO implement here
        return null;
    }


    @Override
	public List <Mast> getShipMastsFromHold(int holdId) {
        // TODO implement here
        return null;
    }


    @Override
	public int getCurMastSpeed(int mastId) {
        // TODO implement here
        return 0;
    }


    @Override
	public String getMastName(int mastId) {
        // TODO implement here
        return "";
    }


    @Override
	public int getSailyards(int mastId) {
        // TODO implement here
        return 0;
    }


    @Override
	public int getMaxSpeed(int mastId) {
        // TODO implement here
        return 0;
    }


    @Override
	public int getMastCost(int mastId) {
        // TODO implement here
        return 0;
    }

}