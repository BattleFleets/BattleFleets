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

    List<Cannon> getAllCannonFromStock(int stockId);

    List<Cannon> getAllCannonFromHold(int holdId);

    List<Cannon> getAllCannonFromShip(int shipId);

    BigInteger createCannon(int cannonTemplateId);

    void deleteCannon(BigInteger id);

    BigInteger createCannon(int cannonTemplateId, BigInteger containerOwnerId);

}