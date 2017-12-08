package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.nctc2017.dao.ExecutorDao;
@Repository
@Qualifier("executorDao")
public class ExecutorDaoImpl implements ExecutorDao {
	public static final String createCannonFunctionName = "CREATE_CANNON";
	public static final String createCannonParameterName = "ObjectIdTemplate";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    @Override
	public boolean ifThingBelongToPlayer(int id, int idPerson) {
        // TODO implement here
        return false;
    }

    @Override
	public boolean calculateDamage(List<List<Integer>> ammoCannon, int idMyShip, int idEnemyShip){
        // TODO implement here	
    	return false;
    }

    @Override
	public int boarding(int idMyShip, int idEnemyShip) {
        // TODO implement here
        return 0;
    }

    @Override
	public void moveCargoTo(int cargoId, int destinationId, int quantity) {
        // TODO implement here
    }

    @Override
	public String moveCargoToWinner(int shipWinnerId, int shipLosserId) {
        // TODO implement here
        return "";
    }

	@Override
	public BigDecimal createCannon(int templateId) {
		SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName(createCannonFunctionName);
		SqlParameterSource in = new MapSqlParameterSource().addValue(createCannonParameterName, templateId); 
    	BigDecimal newCannonId = call.executeFunction(BigDecimal.class, in);
		return newCannonId;
	}

}