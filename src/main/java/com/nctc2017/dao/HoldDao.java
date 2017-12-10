package com.nctc2017.dao;

import java.math.BigInteger;

public interface HoldDao {

    BigInteger findHold(BigInteger shipId);

    BigInteger getOccupiedVolume(BigInteger shipId);

    BigInteger createHold();

    void deleteHold(BigInteger holdId);

    boolean addCargo(BigInteger cargoId, BigInteger holdId);

    BigInteger createHold(BigInteger shipId);

}