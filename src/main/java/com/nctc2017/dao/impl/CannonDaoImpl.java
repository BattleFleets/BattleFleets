package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nctc2017.bean.Cannon;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.extractors.EntityExtractor;
import com.nctc2017.dao.extractors.EntityListExtractor;
import com.nctc2017.dao.extractors.ExtractingVisitor;
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
                new EntityExtractor<>(cannonId, new CannonVisitor()));
        
        if (pickedUpCannon == null){
            RuntimeException ex = new IllegalArgumentException("Wrong cannon object id = " + cannonId);
            log.log(Level.ERROR, "CannonDAO Exception while find by id.", ex);
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
    public List<Cannon> getAllCannonFromStock(@NotNull BigInteger stockId) {
        Validator.dbInstanceOf(jdbcTemplate, "stock", stockId, DatabaseObject.STOCK_OBJTYPE_ID);      
        return getAllCannonsFromAnywhere(stockId);
    }

    @Override
    public List<Cannon> getAllCannonFromHold(@NotNull BigInteger holdId) {
        Validator.dbInstanceOf(jdbcTemplate, "hold", holdId, DatabaseObject.HOLD_OBJTYPE_ID);
        return getAllCannonsFromAnywhere(holdId);
    }

    @Override
    public List<Cannon> getAllCannonFromShip(@NotNull BigInteger shipId) {
        Validator.dbInstanceOf(jdbcTemplate, "ship", shipId, DatabaseObject.SHIP_OBJTYPE_ID);
        return getAllCannonsFromAnywhere(shipId);
    }
    
    @Override
    public BigInteger createCannon(@NotNull BigInteger cannonTemplateId) {
        return createCannon(cannonTemplateId, null); 
    }
    
    @Override
    public BigInteger createCannon(@NotNull BigInteger cannonTemplateId, BigInteger containerOwnerId) {
        Validator.dbInstanceOf(jdbcTemplate,
                "cannon template", 
                cannonTemplateId, 
                DatabaseObject.CANNON_TEMPLATE_TYPE_ID);      
        
        BigInteger newId = queryExecutor.getNextval();
        
        int rowsAffected = jdbcTemplate.update(Query.CREATE_NEW_ENTITY, 
                new Object[] {JdbcConverter.toNumber(newId), 
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
        
        return newId;
    }

    @Override
    public void deleteCannon(@NotNull BigInteger cannonId) {
        int rowsAffected = queryExecutor.delete(cannonId, DatabaseObject.CANNON_OBJTYPE_ID);
        if (rowsAffected == 0) 
            log.log(Level.WARN,"No cannon deleted with id = " + cannonId + ", expected one.");
    }

    private List<Cannon> getAllCannonsFromAnywhere(@NotNull BigInteger containerId) {
        List<Cannon> pickedUpCannons = queryExecutor
                .getEntitiesFromContainer(containerId, 
                        DatabaseObject.CANNON_OBJTYPE_ID, 
                        new EntityListExtractor<>(new CannonVisitor()));
        return pickedUpCannons;
    }
    
    private final class CannonVisitor implements ExtractingVisitor<Cannon> {

        @Override
        public Cannon visit(BigInteger entityId, Map<String, String> papamMap) {
            return new Cannon(entityId, 
                    papamMap.remove(Cannon.NAME), 
                    Integer.valueOf(papamMap.remove(Cannon.DAMAGE)),
                    Integer.valueOf(papamMap.remove(Cannon.DISTANCE)), 
                    Integer.valueOf(papamMap.remove(Cannon.COST)));
        }
        
    }
    
}