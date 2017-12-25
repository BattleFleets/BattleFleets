package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLDataException;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.nctc2017.dao.ExecutorDao;
import com.nctc2017.dao.utils.JdbcConverter;


@Repository
@Qualifier("executorDao")
public class ExecutorDaoImpl implements ExecutorDao {
    private static final String CREATE_CANNON_FUNCTION_NAME = "CREATE_CANNON";
    private static final String MOVE_CARGO_TO_WINNER_FUNCTION_NAME = "MOVE_CARGO_TO_WINNER";
    private static final String CREATE_CANNON_PARAMETER_NAME = "ObjectIdTemplate";
    private static final String CALCULATE_DAMAGE_FUNCTION_NAME = "calculate_damage";
    private static final String PLAYER_SHIP_ID = "playerShipId";
    private static final String ENEMY_SHIP_ID = "enemyShipId";
    private static final String IN_LIST = "in_list";
    private static Logger log = Logger.getLogger(ExecutorDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean ifThingBelongToPlayer(BigInteger id, BigInteger idPerson) {
        // TODO implement here
        return false;
    }

    @Override
    public boolean calculateDamage(int[][] ammoCannon, BigInteger playerShipId, BigInteger idEnemyShip) throws SQLDataException {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate)
                .withFunctionName(CALCULATE_DAMAGE_FUNCTION_NAME);
        SqlParameterSource in = new MapSqlParameterSource().addValue(IN_LIST, ammoCannon)
                .addValue(PLAYER_SHIP_ID, JdbcConverter.toNumber(playerShipId))
                .addValue(ENEMY_SHIP_ID, JdbcConverter.toNumber(idEnemyShip));
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
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName(MOVE_CARGO_TO_WINNER_FUNCTION_NAME);
        try {
            String result = call.executeFunction(String.class, shipWinnerId, shipLosserId);
            return result;
        }
        catch (DataIntegrityViolationException e){
            RuntimeException ex = new IllegalArgumentException("Wrong id " + shipWinnerId + " or " + shipLosserId, e);
            log.error("ExecutorDAO Exception while call moveCargoToWinner.", ex);
            throw ex;
        }
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