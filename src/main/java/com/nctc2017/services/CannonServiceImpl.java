package com.nctc2017.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nctc2017.bean.Cannon;
import com.nctc2017.dao.CannonDao;

@Service("cannonService")
public class CannonServiceImpl implements CannonService {
	@Autowired
	CannonDao cannonDao;

	@Override
	public void addCannon(Cannon cannon) {
		cannonDao.addCannon(cannon, 27);
	}

	@Override
	public void editCannon(Cannon cannon, int personId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCannon(int personId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Cannon find(int personId) {
		Cannon cannon = cannonDao.find(personId);
		return cannon;
	}

	@Override
	public List<Cannon> findAll() {
		List<Cannon> list = cannonDao.findAll();
		return list;
	}

}
