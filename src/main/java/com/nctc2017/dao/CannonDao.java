package com.nctc2017.dao;

import java.util.List;

import com.nctc2017.bean.Cannon;

public interface CannonDao {

	/**
	 * @param int cannonId 
	 * @return
	 */
	Cannon findById(int cannonId);

	/**
	 * @param int cannonId 
	 * @return
	 */
	String getName(int cannonId);

	/**
	 * @param int cannonId 
	 * @return
	 */
	int getCost(int cannonId);

	/**
	 * @param int cannonId 
	 * @return
	 */
	int getDistance(int cannonId);

	/**
	 * @param int cannonId 
	 * @return
	 */
	int getDamage(int cannonId);

	/**
	 * @param int stockId 
	 * @return
	 */
	List<Cannon> getAllCannonFromStock(int stockId);

	/**
	 * @param int holdId 
	 * @return
	 */
	List<Cannon> getAllCannonFromHold(int holdId);

	/**
	 * @param int shipId 
	 * @return
	 */
	List<Cannon> getAllCannonFromShip(int shipId);

	/**
	 * @param int cannonTemplateId 
	 * @return
	 */
	int createCannon(int cannonTemplateId);

	/**
	 * @param int cannonId 
	 * @return
	 */
	void deleteCannon(int cannonId);

}