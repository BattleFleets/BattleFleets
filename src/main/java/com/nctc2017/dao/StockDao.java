package com.nctc2017.dao;

import java.math.BigInteger;

public interface StockDao {

    BigInteger findStockId(BigInteger playerId);

    BigInteger createStock(BigInteger playerId);

    void deleteStock(BigInteger playerId);

    void addCargo(BigInteger cargoId, BigInteger playerId);

    boolean isSuchCargoInStock(BigInteger cargoId, BigInteger cargoTemplateId, BigInteger stockId);

}