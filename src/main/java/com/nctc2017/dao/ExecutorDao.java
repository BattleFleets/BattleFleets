package com.nctc2017.dao;

import java.math.BigInteger;
import java.sql.SQLDataException;
import java.util.List;

public interface ExecutorDao {

    boolean ifThingBelongToPlayer(BigInteger id, BigInteger idPerson);

    boolean calculateDamage(List<List<Integer>> ammoCannon, BigInteger idMyShip, BigInteger idEnemyShip) throws SQLDataException;

    BigInteger boarding(BigInteger idMyShip, BigInteger idEnemyShip);

    void moveCargoTo(BigInteger cargoId, BigInteger destinationId, int quantity);

    String moveCargoToWinner(BigInteger shipWinnerId, BigInteger shipLosserId);

    BigInteger createCannon(BigInteger templateId);

}