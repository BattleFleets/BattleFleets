package com.nctc2017.services;

import java.util.List;

import com.nctc2017.bean.Cannon;

public interface CannonService {
	public void addCannon(Cannon cannon);
	 
	public void editCannon(Cannon cannon, int personId);
	 
	public void deleteCannon(int personId);
	 
	public Cannon find(int personId);
	 
	public List <Cannon> findAll();
}
