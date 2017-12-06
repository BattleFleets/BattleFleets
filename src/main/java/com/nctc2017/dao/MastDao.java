package com.nctc2017.dao;

import java.util.List;

import com.nctc2017.bean.Mast;

public interface MastDao {

	Mast findMast(int mastId);

	int createNewMast(int mastTemplateId);

	void deleteMast(int mastId);

	boolean updateCurMastSpeed(int mastId, int newMastSpeed);

	List<Mast> getShipMastsFromShip(int shipId);

	List<Mast> getShipMastsFromStock(int stockId);

	List<Mast> getShipMastsFromHold(int holdId);

	int getCurMastSpeed(int mastId);

	String getMastName(int mastId);

	int getSailyards(int mastId);

	int getMaxSpeed(int mastId);

	int getMastCost(int mastId);

}