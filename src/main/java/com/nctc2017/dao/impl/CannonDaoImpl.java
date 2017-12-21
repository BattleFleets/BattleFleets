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

import javax.validation.constraints.NotNull;

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
import com.nctc2017.dao.utils.JdbcConverter;
import com.nctc2017.dao.utils.QueryExecutor;
import com.nctc2017.dao.utils.Validator;

@Repository
@Qualifier("cannonDao")
public class CannonDaoImpl implements CannonDao {
    
    private static Logger log = Logger.getLogger(CannonDaoImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private QueryExecutor queryExecutor; 

    @Override
    public Cannon findById(@NotNull BigInteger cannonId) {
        Cannon pickedUpCannon = queryExecutor.findEntity(cannonId, 
                DatabaseObject.CANNON_OBJTYPE_ID,
                new CannonExtractor(cannonId));
        if (pickedUpCannon == null){
            RuntimeException ex = new IllegalArgumentException("Wrong cannon object id = " + cannonId);
            log.log(Level.ERROR, "CannonDAO Exception while find by id ", ex);
            throw ex;
        }
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

    @Override
    public List<Cannon> getAllCannonFromStock(BigInteger stockId) {
        // TODO Validator.dbInstanceOf("stock", stockId, DatabaseObject.STOCK_OBJTYPE_ID);      
        return getAllCannonsFromAnywhere(stockId);
    }

    @Override
    public List<Cannon> getAllCannonFromHold(BigInteger holdId) {
        Validator.dbInstanceOf(jdbcTemplate, "hold", holdId, DatabaseObject.HOLD_OBJTYPE_ID);
        return getAllCannonsFromAnywhere(holdId);
    }

    @Override
    public List<Cannon> getAllCannonFromShip(BigInteger shipId) {
        Validator.dbInstanceOf(jdbcTemplate, "ship", shipId, DatabaseObject.SHIP_OBJTYPE_ID);
        return getAllCannonsFromAnywhere(shipId);
    }
    @Override
    public BigInteger createCannon(BigInteger cannonTemplateId) {
        return createCannon(cannonTemplateId, null); 
    }
    @Override
    public BigInteger createCannon(BigInteger cannonTemplateId, BigInteger containerOwnerId) {
        Validator.dbInstanceOf(jdbcTemplate,
                "cannon template", 
                cannonTemplateId, 
                DatabaseObject.CANNON_TEMPLATE_TYPE_ID);      
        
        BigDecimal newId = jdbcTemplate.queryForObject(Query.GET_NEXTVAL,BigDecimal.class);
        
        int rowsAffected = jdbcTemplate.update(Query.CREATE_NEW_ENTITY, 
                new Object[] {newId, 
                        JdbcConverter.toNumber(containerOwnerId),// == null ? null : containerOwnerId.longValueExact(),
                        JdbcConverter.toNumber(DatabaseObject.CANNON_OBJTYPE_ID), 
                        JdbcConverter.toNumber(cannonTemplateId), 
                        JdbcConverter.toNumber(cannonTemplateId),
                        JdbcConverter.toNumber(DatabaseAttribute.CANNON_NAME_ID)});
        if (rowsAffected == 0){
            RuntimeException ex = new IllegalStateException("No cannon created, expected one new cannon");
            log.log(Level.ERROR, "CannonDAO Exception while creating new entity of cannon.", ex);
            throw ex;
        }
        
        return newId.toBigIntegerExact();
    }

    @Override
    public void deleteCannon(BigInteger cannonId) {
        int rowsAffected = queryExecutor.delete(cannonId, DatabaseObject.CANNON_OBJTYPE_ID);
        if (rowsAffected == 0) 
            log.log(Level.WARN,"No cannon deleted with id = " + cannonId + ", expected one.");
    }

    private List<Cannon> getAllCannonsFromAnywhere(BigInteger containerId) {
        List<Cannon> pickedUpCannons = queryExecutor
                .getEntitiesFromContainer(containerId, 
                        DatabaseObject.CANNON_OBJTYPE_ID, 
                        new CannonListExtractor());
        return pickedUpCannons;
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