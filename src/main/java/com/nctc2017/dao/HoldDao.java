package com.nctc2017.dao;

public interface HoldDao {

	int findHold(int shipId);

	int getOccupiedVolume(int shipId);

	int createHold();

	void deleteHold(int holdId);

	boolean addCargo(int cargoId, int holdId);

}