package com.nctc2017.dao;

import java.math.BigInteger;
import java.util.List;

public interface ExecutorDao {

    boolean ifThingBelongToPlayer(int id, int idPerson);

    boolean calculateDamage(List<List<Integer>> ammoCannon, int idMyShip, int idEnemyShip);

    int boarding(int idMyShip, int idEnemyShip);

    void moveCargoTo(int cargoId, int destinationId, int quantity);

    String moveCargoToWinner(int shipWinnerId, int shipLosserId);

    BigInteger createCannon(int templateId);

}