package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.nctc2017.bean.Cannon;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.ExecutorDao;

@Repository
@Qualifier("cannonDao")
public class CannonDaoImpl implements CannonDao{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final int objectType = DatabaseObject.cannonObjTypeId;
	@Autowired
	@Qualifier("executorDao")
	private ExecutorDao executor;
	
	@Override
	public Cannon findById(int cannonId) {
		Cannon pickedUpCannon = jdbcTemplate.query(Query.findAnyEntity,
				new Object[] { objectType, cannonId, objectType, cannonId }, new CannonExtractor(cannonId));
		return pickedUpCannon;
	}

	@Override
    public String getName(int cannonId) {
        // TODO implement here
        return "";
    }

	@Override
    public int getCost(int cannonId) {
        // TODO implement here
        return 0;
    }

	@Override
    public int getDistance(int cannonId) {
        // TODO implement here
        return 0;
    }

	@Override
    public int getDamage(int cannonId) {
        // TODO implement here
        return 0;
    }
	private List<Cannon> getAllCannonsFromAnywhere(int containerId){
		List<Cannon> pickedUpCannons = jdbcTemplate.query(Query.getEntitiesFromContainer,
				new Object[] { objectType, containerId, objectType, containerId }, new CannonListExtractor());
		return pickedUpCannons;
	}
	@Override
    public List<Cannon> getAllCannonFromStock(int stockId) {
		return getAllCannonsFromAnywhere(stockId);
    }

	@Override
    public List<Cannon> getAllCannonFromHold(int holdId) {
		return getAllCannonsFromAnywhere(holdId);
    }

	@Override
    public List<Cannon> getAllCannonFromShip(int shipId) {
		return getAllCannonsFromAnywhere(shipId);
    }

	@Override
    public int createCannon(int cannonTemplateId) {
		return executor.createCannon(cannonTemplateId);
    }

	@Override
    public void deleteCannon(int cannonId) {
        // TODO implement here
    }
	
	private final class CannonExtractor implements ResultSetExtractor<Cannon> {
		private int cannonId;
		public CannonExtractor(int cannonId){
			this.cannonId = cannonId;
		}
		@Override
		public Cannon extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<String, String> papamMap = new HashMap<>(4);
			while(rs.next()){
				papamMap.put(rs.getString(1), rs.getString(2));
			}
			return new Cannon(cannonId, 
					papamMap.remove("CanonName"), 
					Integer.valueOf(papamMap.remove("Damage")),
					Integer.valueOf(papamMap.remove("Distance")), 
					Integer.valueOf(papamMap.remove("CannonCost")));
		}
	}
	
	private final class CannonListExtractor implements ResultSetExtractor<List<Cannon>> {
		@Override
		public List<Cannon> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<String, String> papamMap;
			Map<Integer, Map<String, String>> cannonMap = new HashMap<>();
			Integer idCannon;
			while(rs.next()) {
				idCannon = Integer.valueOf(rs.getString(1));
				papamMap = cannonMap.get(idCannon);
				if (papamMap == null){
					papamMap = new HashMap<>(4);
					papamMap.put(rs.getString(2), rs.getString(3));
					cannonMap.put(idCannon, papamMap);
				} else {
					papamMap.put(rs.getString(2), rs.getString(3));
				}
			}
			List<Cannon> cannonList = new ArrayList<>(cannonMap.size());
			Cannon nextCannon;
			Map<String, String> nextParamMap;
			for (Entry<Integer, Map<String, String>> entry : cannonMap.entrySet()) {
				nextParamMap = entry.getValue();
				nextCannon = new Cannon(entry.getKey(), 
						nextParamMap.remove("CanonName"), 
						Integer.valueOf(nextParamMap.remove("Damage")),
						Integer.valueOf(nextParamMap.remove("Distance")), 
						Integer.valueOf(nextParamMap.remove("CannonCost")));
				cannonList.add(nextCannon);
			}
			return cannonList;
		}
	}
}