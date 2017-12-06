package com.nctc2017.dao;

import java.util.List;

import com.nctc2017.bean.Ammo;

public interface AmmoDao {

	Ammo findById(int idAmmo);

	String getAmmoName(int id);

	String getAmmoDamageType(int id);

	int getAmmoCost(int id);

	int getAmmoQuantity(int id);

	boolean increaseAmmoQuantity(int id, int increaseNumber);

	boolean decreaseAmmoQuantity(int id, int decreaseNumber);

	int createAmmoAndGetId(int ammoTemplateId);

	List<Ammo> getAllAmmoFromStock(int idStock);

	List<Ammo> getAllAmmoFromHold(int idHold);

}