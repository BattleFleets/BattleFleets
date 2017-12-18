package com.nctc2017.dao;

import java.math.BigInteger;
import java.util.List;

import com.nctc2017.bean.Ammo;

public interface AmmoDao {

    Ammo findById(BigInteger AmmoId);

    String getAmmoName(BigInteger AmmoId);

    String getAmmoDamageType(BigInteger AmmoId);

    int getAmmoCost(BigInteger AmmoId);

    int getAmmoQuantity(BigInteger AmmoId);

    boolean increaseAmmoQuantity(BigInteger AmmoId, int increaseNumber);

    boolean decreaseAmmoQuantity(BigInteger AmmoId, int decreaseNumber);

    List<Ammo> getAllAmmoFromStock(BigInteger stockId);

    List<Ammo> getAllAmmoFromHold(BigInteger holdId);

    BigInteger createAmmo(BigInteger ammoTemplateId, int quantity);

}