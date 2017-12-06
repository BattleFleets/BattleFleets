package com.nctc2017.dao.impl;

import java.util.*;

import com.nctc2017.dao.ExecutorDao;
public class ExecutorDaoImpl implements ExecutorDao {

    @Override
	public boolean ifThingBelongToPlayer(int id, int idPerson) {
        // TODO implement here
        return false;
    }

    @Override
	public boolean calculateDamage(List<List<Integer>> ammoCannon, int idMyShip, int idEnemyShip){
        // TODO implement here	
    	return false;
    }

    @Override
	public int boarding(int idMyShip, int idEnemyShip) {
        // TODO implement here
        return 0;
    }

    @Override
	public void moveCargoTo(int cargoId, int destinationId, int quantity) {
        // TODO implement here
    }

    @Override
	public String moveCargoToWinner(int shipWinnerId, int shipLosserId) {
        // TODO implement here
        return "";
    }

}