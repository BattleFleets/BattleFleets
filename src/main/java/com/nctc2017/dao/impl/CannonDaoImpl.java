package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.nctc2017.bean.Cannon;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.ExecutorDao;

@Repository
@Qualifier("cannonDao")
public class CannonDaoImpl implements CannonDao {
    
    private static Logger log = Logger.getLogger(CannonDaoImpl.class.getName());
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("executorDao")
    private ExecutorDao executor;

    @Override
    public Cannon findById(BigInteger cannonId) {
        Cannon pickedUpCannon = jdbcTemplate.query(Query.FIND_ANY_ENTITY,
                new Object[] { DatabaseObject.CANNON_OBJTYPE_ID, cannonId.longValueExact(), 
                               DatabaseObject.CANNON_OBJTYPE_ID, cannonId.longValueExact() },
                new CannonExtractor(cannonId));
        if (pickedUpCannon == null) throwRuntimeException(new IllegalArgumentException("Wrong cannon object id = " + cannonId));
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

    private List<Cannon> getAllCannonsFromAnywhere(int containerId) {
        List<Cannon> pickedUpCannons = jdbcTemplate.query(Query.GET_ENTITIES_FROM_CONTAINER, 
                new Object[] {DatabaseObject.CANNON_OBJTYPE_ID, containerId, 
                              DatabaseObject.CANNON_OBJTYPE_ID, containerId },
                new CannonListExtractor());
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
    public BigInteger createCannon(int cannonTemplateId) {
        return createCannon(cannonTemplateId, null); 
    }
    @Override
    public BigInteger createCannon(int cannonTemplateId, BigInteger containerOwnerId) {
        try{
        jdbcTemplate.queryForObject(Query.CHECK_OBJECT, 
                new Object[] {cannonTemplateId, DatabaseObject.CANNON_TEMPLATE_TYPE_ID},  
                BigDecimal.class);
        } catch (DataAccessException e) {
            throwRuntimeException(new IllegalArgumentException("Wrong cannon template id = " + cannonTemplateId));
        }
        int rowsAffected = jdbcTemplate.update(Query.CREATE_NEW_ENTITY, 
                new Object[] {containerOwnerId ==null ? null : containerOwnerId.longValueExact(),
                        DatabaseObject.CANNON_OBJTYPE_ID, 
                        cannonTemplateId, cannonTemplateId,
                        DatabaseAttribute.CANNON_NAME_ID});
        if (rowsAffected == 0) throwRuntimeException(new IllegalStateException("No effect on database"));
        
        return jdbcTemplate.queryForObject(Query.GET_CURRVAL,BigDecimal.class).toBigInteger();
    }

    @Override
    public void deleteCannon(BigInteger cannonId) {
        int rowsAffected = jdbcTemplate.update(Query.DELETE_ENTITY, 
                new Object[] {cannonId.longValueExact(), DatabaseObject.CANNON_OBJTYPE_ID});
        if (rowsAffected == 0) log.log(Level.WARN,"Nothing to delete from database");
    }
    
    private void throwRuntimeException(RuntimeException ex) {
        log.log(Level.ERROR, "DAOException: ", ex);
        throw ex;
    }

    private final class CannonExtractor implements ResultSetExtractor<Cannon> {
        
        private BigInteger cannonId;

        public CannonExtractor(BigInteger cannonId) {
            this.cannonId = cannonId;
        }

        @Override
        public Cannon extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.isBeforeFirst()) return null;
            
            Map<String, String> papamMap = new HashMap<>(4);
            while (rs.next()) {
                papamMap.put(rs.getString(1), rs.getString(2));
            }
            
            return new Cannon(cannonId, 
                    papamMap.remove(Cannon.NAME), 
                    Integer.valueOf(papamMap.remove(Cannon.DAMAGE)),
                    Integer.valueOf(papamMap.remove(Cannon.DISTANCE)), 
                    Integer.valueOf(papamMap.remove(Cannon.COST)));
        }
    }

    private final class CannonListExtractor implements ResultSetExtractor<List<Cannon>> {
        
        @Override
        public List<Cannon> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, String> papamMap;
            Map<BigDecimal, Map<String, String>> cannonMap = new HashMap<>();
            BigDecimal idCannon;
            while (rs.next()) {
                idCannon = rs.getBigDecimal(1);
                papamMap = cannonMap.get(idCannon);
                if (papamMap == null) {
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
            for (Entry<BigDecimal, Map<String, String>> entry : cannonMap.entrySet()) {
                nextParamMap = entry.getValue();
                nextCannon = new Cannon(entry.getKey().toBigInteger(), 
                        nextParamMap.remove(Cannon.NAME),
                        Integer.valueOf(nextParamMap.remove(Cannon.DAMAGE)),
                        Integer.valueOf(nextParamMap.remove(Cannon.DISTANCE)),
                        Integer.valueOf(nextParamMap.remove(Cannon.COST)));
                cannonList.add(nextCannon);
            }
            return cannonList;
        }
    }
}