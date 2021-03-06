package com.nctc2017.dao.utils;

import oracle.sql.NUMBER;

import java.math.BigInteger;
import java.sql.SQLException;

public final class JdbcConverter {

    public static NUMBER toNumber(BigInteger value) {
        if (value == null)
            return null;

        NUMBER numValue;
        try {
            numValue = new NUMBER(value);
        } catch (SQLException e) {
            throw new RuntimeException(e.getErrorCode() + " error code. " + e.getMessage());
        }
        return numValue;
    }

    /*This method should convert null to 0, instead of NumberFormatException*/
    public static int parseInt(String value) {
        try {
            return Integer.valueOf(value);
        } catch(NumberFormatException nfe) {
            return 0;
        }
    }

    public static BigInteger parseBigIneger(String value) {
        try {
            return new BigInteger(value);
        } catch(Exception e) {
            return null;
        }
    }
}
