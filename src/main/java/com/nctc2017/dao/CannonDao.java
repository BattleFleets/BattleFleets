package com.nctc2017.dao;

import java.util.List;

import com.nctc2017.bean.Cannon;

public interface CannonDao {
	public void addCannon(Cannon cannon, int id);

	public void editCannon(Cannon cannon, int personId);

	public void deleteCannon(int personId);

	public Cannon find(int personId);

	public List<Cannon> findAll();
}
