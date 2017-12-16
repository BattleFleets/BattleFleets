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
}
