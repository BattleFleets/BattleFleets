package com.nctc2017.dao;

import java.math.BigInteger;
import java.util.List;

import com.nctc2017.bean.Cannon;

public interface CannonDao {

    Cannon findById(BigInteger cannonId);

    String getName(int cannonId);

    int getCost(int cannonId);

    int getDistance(int cannonId);

    int getDamage(int cannonId);

    List<Cannon> getAllCannonFromStock(BigInteger stockId);

    List<Cannon> getAllCannonFromHold(BigInteger holdId);

    List<Cannon> getAllCannonFromShip(BigInteger shipId);

    BigInteger createCannon(BigInteger cannonTemplateId);

    void deleteCannon(BigInteger id);

    BigInteger createCannon(BigInteger cannonTemplateId, BigInteger containerOwnerId);

}