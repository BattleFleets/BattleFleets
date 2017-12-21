package com.nctc2017.dao.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;

import oracle.sql.NUMBER;

@Component("queryExecutor")
public class QueryExecutor {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Finds entity which id is specified as {@code entityId} and type's id as {@code entityTypeId}.
     * @param entityId - id of entity
     * @param entityTypeId - id of type of entity
     * @param extractor - object that will extract results
     * */
    public <T> T findEntity(BigInteger entityId, BigInteger entityTypeId, ResultSetExtractor<T> extractor) {
        return jdbcTemplate.query(Query.FIND_ANY_ENTITY, 
                new Object[] { JdbcConverter.toNumber(entityTypeId), JdbcConverter.toNumber(entityId),
                               JdbcConverter.toNumber(entityTypeId), JdbcConverter.toNumber(entityId) },
                extractor);
    }
    
    /**
     * Deletes entity which id is specified as {@code entityId} and type's id as {@code entityTypeId}.
     * @param entityId - id of entity
     * @param entityTypeId - id of type of entity
     * */
    public int delete(BigInteger entityId, BigInteger entityTypeId) {
        int rowsAffected = jdbcTemplate.update(Query.DELETE_OBJECT, 
                new Object[] {JdbcConverter.toNumber(entityId), 
                        JdbcConverter.toNumber(entityTypeId)});
        return rowsAffected;
    }
    
    /**
     * This method allows finds and return all entities with specific type, 
     * that contains in some container like Hold or Stock.
     * @param containerId - id of container
     * @param objTypeId - id of type of retrieving object
     * @param extractor - object that will extract results
     * */
    public <T> T getEntitiesFromContainer(BigInteger containerId, BigInteger objTypeId, ResultSetExtractor<T> extractor) {
        return  jdbcTemplate.query(Query.GET_ENTITIES_FROM_CONTAINER, 
                new Object[] {JdbcConverter.toNumber(objTypeId), 
                        JdbcConverter.toNumber(containerId), 
                        JdbcConverter.toNumber(objTypeId), 
                        JdbcConverter.toNumber(containerId) },
                extractor);
    }
    
    /**
     * Finds and return container with specific type, 
     * that belongs to some owner, like Hold belongs to Ship.
     * @param ownerId - id of owner
     * @param ownerTypeId - id of type of owner
     * @param extractor - object that will extract results
     * */
    public BigInteger findContainerByOwnerId(BigInteger ownerId, BigInteger ownerTypeId) {
        return jdbcTemplate.queryForObject(Query.FIND_CONTAINER_BY_OWNER_ID,
                new Object[] { JdbcConverter.toNumber(ownerTypeId), 
                        JdbcConverter.toNumber(ownerId) }, 
                BigDecimal.class).toBigIntegerExact();
    }
    
    public int putEntityToContainer(BigInteger containerId, BigInteger entityId, BigInteger containerTypeId){
        NUMBER containerIdNumber = JdbcConverter.toNumber(containerId);
        NUMBER entityIdNumber = JdbcConverter.toNumber(entityId);
        return jdbcTemplate.update(Query.PUT_ENTITY_TO_CONTAINER, 
                new Object[] {containerIdNumber, 
                        entityIdNumber,
                        containerIdNumber, 
                        containerIdNumber, 
                        JdbcConverter.toNumber(containerTypeId)});
    }
    
    public List<BigInteger> findAllEntitiesInConteinerByOwnerId(BigInteger ownerId, BigInteger ownerTypeId){
        List<BigDecimal> entitiesId = jdbcTemplate.queryForList(Query.FIND_ALL_IN_CONTAINER_BY_OWNER_ID,
                new Object[] { JdbcConverter.toNumber(ownerTypeId), 
                        JdbcConverter.toNumber(ownerId) }, 
                BigDecimal.class);
        
        List<BigInteger> entitiesIdInt = new ArrayList<>(entitiesId.size());
        for (BigDecimal bigDecimal : entitiesId) {
            entitiesIdInt.add(bigDecimal.toBigIntegerExact());
        }
        return entitiesIdInt;
    }
    
    
}
