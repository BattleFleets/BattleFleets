package com.nctc2017.dao;

import java.util.*;

/**
 * 
 */
public class ExecutorDao {

    /**
     * Default constructor
     */
    public ExecutorDao() {
    }

    /**
     * @param int id 
     * @param int idPerson 
     * @return
     */
    public boolean ifThingBelongToPlayer(int id, int idPerson) {
        // TODO implement here
        return false;
    }

    /**
     * 
     */
    public boolean calculateDamage(List<List<Integer>> ammoCannon, int idMyShip, int idEnemyShip){
        // TODO implement here	
    	return false;
    }

    /**
     * @param int idMyShip 
     * @param int idEnemyShip 
     * @return
     */
    public int boarding(int idMyShip, int idEnemyShip) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int cargoId 
     * @param int destinationId 
     * @param int quantity
     */
    public void moveCargoTo(int cargoId, int destinationId, int quantity) {
        // TODO implement here
    }

    /**
     * @param int shipWinnerId 
     * @param int shipLosserId 
     * @return
     */
    public String moveCargoToWinner(int shipWinnerId, int shipLosserId) {
        // TODO implement here
        return "";
    }

}