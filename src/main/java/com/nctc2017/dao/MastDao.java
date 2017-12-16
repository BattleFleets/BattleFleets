package com.nctc2017.dao;

import java.math.BigInteger;
import java.util.List;

import com.nctc2017.bean.Mast;

public interface MastDao {

    Mast findMast(BigInteger mastId);

    BigInteger createNewMast(BigInteger mastTemplateId, BigInteger containerOwnerID);

    void deleteMast(BigInteger mastId);

    boolean updateCurMastSpeed(BigInteger mastId, int newMastSpeed);

    List<Mast> getShipMastsFromShip(int shipId);

    List<Mast> getShipMastsFromStock(int stockId);

    List<Mast> getShipMastsFromHold(int holdId);

    int getCurMastSpeed(BigInteger mastId);

    String getMastName(BigInteger mastId);

    int getSailyards(int mastId);

    int getMaxSpeed(BigInteger mastId);

    int getMastCost(BigInteger mastId);

}