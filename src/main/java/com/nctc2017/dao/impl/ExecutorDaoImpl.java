package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLDataException;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.UncategorizedSQLException;
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
    private static final Logger LOG = Logger.getLogger(ExecutorDaoImpl.class);
    private static final String CREATE_CANNON_FUNCTION_NAME = "CREATE_CANNON";
    private static final String MOVE_CARGO_TO_WINNER_FUNCTION_NAME = "MOVE_CARGO_TO_WINNER";
    private static final String MOVE_CARGO_TO_FUNCTION_NAME = "MOVE_CARGO_TO";
    private static final String CREATE_CANNON_PARAMETER_NAME = "ObjectIdTemplate";
    private static final String CALCULATE_DAMAGE_FUNCTION_NAME = "CALCULATE_DAMAGE";
    private static final String PLAYER_SHIP_ID = "playerShipId";
    private static final String ENEMY_SHIP_ID = "enemyShipId";
    private static final String DIMENSION = "dimension_";
    private static final String IN_LIST = "in_l";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean ifThingBelongToPlayer(BigInteger id, BigInteger idPerson) {
        // TODO implement here
        return false;
    }

    @Override
    public void calculateDamage(int[][] ammoCannon, BigInteger playerShipId, BigInteger idEnemyShip) throws SQLException {
        
        StringBuilder arrToStr = new StringBuilder();
        for (int i = 0; i < ammoCannon.length; i++) {
            for (int j = 0; j < ammoCannon[i].length; j++) {
                arrToStr.append(ammoCannon[i][j]);
                arrToStr.append(",");
            }
        }
        String arrInStr = arrToStr.toString();
        /*int[][] array2 = new int[ammoCannon.size()][];
        int counter = 0;
        for (List<Integer> integers : ammoCannon) {
            array2[counter] = integers.toArray(new int[integers.size()]);
        }*/
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate.getDataSource())
                .withProcedureName(CALCULATE_DAMAGE_FUNCTION_NAME);/*
                .declareParameters(new SqlOutParameter("RETURN", OracleTypes.BOOLEAN))
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter(IN_LIST, OracleTypes.VARCHAR),
                        new SqlParameter(PLAYER_SHIP_ID, OracleTypes.NUMBER),
                        new SqlParameter(ENEMY_SHIP_ID, OracleTypes.NUMBER),
                        new SqlParameter(DIMENSION, OracleTypes.INTEGER));*/
        //Connection conn = jdbcTemplate.getDataSource().getConnection();
        //ArrayDescriptor desc = ArrayDescriptor.createDescriptor("STRINGLIST_LIST", conn);
        //ArrayDescriptor intDesc = new ArrayDescriptor("LIST_I", conn);
        //ARRAY sqlInLine = new ARRAY(intDesc, conn, inLine);
        //SqlArrayValue<Integer> arr = new SqlArrayValue<>(inLine);
        //CHAR chars = new CHAR(arrInStr, new CharacterSet());
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue(IN_LIST, arrInStr)
                .addValue(PLAYER_SHIP_ID, JdbcConverter.toNumber(playerShipId))
                .addValue(ENEMY_SHIP_ID, JdbcConverter.toNumber(idEnemyShip))
                .addValue(DIMENSION, ammoCannon.length);
        
        try {
            call.execute(in);
        } catch (UncategorizedSQLException e) {
            LOG.error("Mistake on client side, may be incorrect ratio of ammunition to cannons "
                    + "or ammunition to quantity in hold", e);
            throw e.getSQLException();
        }
        
    }

    @Override
    public BigInteger boarding(BigInteger idMyShip, BigInteger idEnemyShip) {
        // TODO implement here
        return null;
    }

    @Override
    public String moveCargoTo(BigInteger cargoId, BigInteger destinationId, int quantity) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName(MOVE_CARGO_TO_FUNCTION_NAME);
        String result = call.executeFunction(String.class, cargoId, destinationId, quantity);
        return result;
    }

    @Override
    public String moveCargoToWinner(BigInteger shipWinnerId, BigInteger shipLosserId) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName(MOVE_CARGO_TO_WINNER_FUNCTION_NAME);
        String result = call.executeFunction(String.class, shipWinnerId, shipLosserId);
        return result;
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