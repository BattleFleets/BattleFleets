package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jdbc.support.oracle.SqlArrayValue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.nctc2017.dao.ExecutorDao;
import com.nctc2017.dao.utils.JdbcConverter;

import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

@Repository
@Qualifier("executorDao")
public class ExecutorDaoImpl implements ExecutorDao {
    private static final String CREATE_CANNON_FUNCTION_NAME = "CREATE_CANNON";
    private static final String CREATE_CANNON_PARAMETER_NAME = "ObjectIdTemplate";
    private static final String CALCULATE_DAMAGE_FUNCTION_NAME = "CALCULATE_DAMAGE";
    private static final String PLAYER_SHIP_ID = "playerShipId";
    private static final String ENEMY_SHIP_ID = "enemyShipId";
    private static final String DIMENSION = "dem";
    private static final String IN_LIST = "in_l";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean ifThingBelongToPlayer(BigInteger id, BigInteger idPerson) {
        // TODO implement here
        return false;
    }

    @Override
    public boolean calculateDamage(int[][] ammoCannon, BigInteger playerShipId, BigInteger idEnemyShip) throws SQLException {
        /*int[][] array2 = new int[ammoCannon.size()][];
        int counter = 0;
        for (List<Integer> integers : ammoCannon) {
            array2[counter] = integers.toArray(new int[integers.size()]);
        }*/
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName(CALCULATE_DAMAGE_FUNCTION_NAME)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(IN_LIST, OracleTypes.ARRAY, "LIST_I"))
                .declareParameters(
                        new SqlParameter(PLAYER_SHIP_ID, OracleTypes.NUMBER, "NUMBER"))
                .declareParameters(
                        new SqlParameter(ENEMY_SHIP_ID, OracleTypes.NUMBER, "NUMBER"))
                .declareParameters(
                        new SqlParameter(DIMENSION, OracleTypes.INTEGER, "INTEGER"));
        //Connection conn = jdbcTemplate.getDataSource().getConnection();
        //ArrayDescriptor desc = ArrayDescriptor.createDescriptor("STRINGLIST_LIST", conn);
        Integer[] inLine = new Integer[ammoCannon.length * ammoCannon[0].length];
        for (int i = 0; i < ammoCannon.length; i++) {
            for (int j = 0; j < ammoCannon[i].length; j++) {
                inLine[i * ammoCannon.length + j] = ammoCannon[i][j];
            }
        }
        SqlArrayValue<Integer> arr = new SqlArrayValue<>(inLine);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue(IN_LIST, arr)
                .addValue(PLAYER_SHIP_ID, JdbcConverter.toNumber(playerShipId))
                .addValue(ENEMY_SHIP_ID, JdbcConverter.toNumber(idEnemyShip))
                .addValue(DIMENSION, ammoCannon.length);
        Boolean success = call.executeFunction(Boolean.class, in);
        
        
        if (success != null)
            return success;
        throw new SQLDataException("Database return null when boolean expected");
    }

    @Override
    public BigInteger boarding(BigInteger idMyShip, BigInteger idEnemyShip) {
        // TODO implement here
        return null;
    }

    @Override
    public void moveCargoTo(BigInteger cargoId, BigInteger destinationId, int quantity) {
        // TODO implement here
    }

    @Override
    public String moveCargoToWinner(BigInteger shipWinnerId, BigInteger shipLosserId) {
        // TODO implement here
        return "";
    }

    @Override
    public BigInteger createCannon(BigInteger templateId) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName(CREATE_CANNON_FUNCTION_NAME);
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue(CREATE_CANNON_PARAMETER_NAME, 
                        JdbcConverter.toNumber(templateId));
        BigDecimal newCannonId = call.executeFunction(BigDecimal.class, in);
        if (newCannonId != null)
            return newCannonId.toBigInteger();
        return null;
    }

}