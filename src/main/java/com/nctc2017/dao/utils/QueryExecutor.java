package com.nctc2017.dao.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

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
    public <T> T findEntity(@NotNull BigInteger entityId, @NotNull BigInteger entityTypeId, @NotNull ResultSetExtractor<T> extractor) {
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
    public int delete(@NotNull BigInteger entityId, @NotNull BigInteger entityTypeId) {
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
    public <T> T getEntitiesFromContainer(@NotNull BigInteger containerId, @NotNull BigInteger objTypeId, @NotNull ResultSetExtractor<T> extractor) {
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
     * @param containerTypeId- id of type of container
     * */
    public BigInteger findContainerByOwnerId(@NotNull BigInteger containerTypeId, @NotNull BigInteger ownerId, @NotNull BigInteger ownerTypeId) {
        return jdbcTemplate.queryForObject(Query.FIND_CONTAINER_BY_OWNER_ID,
                new Object[] { JdbcConverter.toNumber(containerTypeId),
                        JdbcConverter.toNumber(ownerId),
                        JdbcConverter.toNumber(ownerTypeId) },
                BigDecimal.class).toBigIntegerExact();
    }
    
    /**
     * This method allows insert object to container and returns 1 if operation is success.
     * If entity is containing in another container, it will be moved to container specified in this method.
     * @param containerId - id of container
     * @param entityId - id of entity
     * @param containerTypeId - id of type of container
     * @return 1 if operation is success
     * */
    public int putEntityToContainer(@NotNull BigInteger containerId, @NotNull BigInteger entityId, @NotNull BigInteger containerTypeId){
        NUMBER containerIdNumber = JdbcConverter.toNumber(containerId);
        NUMBER entityIdNumber = JdbcConverter.toNumber(entityId);
        return jdbcTemplate.update(Query.PUT_ENTITY_TO_CONTAINER, 
                new Object[] {containerIdNumber, 
                        entityIdNumber,
                        containerIdNumber, 
                        containerIdNumber, 
                        JdbcConverter.toNumber(containerTypeId)});
    }
    
    public List<BigInteger> findAllEntitiesInContainerByOwnerId(@NotNull BigInteger containerTypeId, @NotNull BigInteger ownerId, @NotNull BigInteger ownerTypeId){
        List<BigDecimal> entitiesId = jdbcTemplate.queryForList(Query.FIND_ALL_IN_CONTAINER_BY_OWNER_ID,
                new Object[] { JdbcConverter.toNumber(containerTypeId),
                        JdbcConverter.toNumber(ownerId),
                        JdbcConverter.toNumber(ownerTypeId) },
                BigDecimal.class);
        
        List<BigInteger> entitiesIdInt = new ArrayList<>(entitiesId.size());
        for (BigDecimal bigDecimal : entitiesId) {
            entitiesIdInt.add(bigDecimal.toBigIntegerExact());
        }
        return entitiesIdInt;
    }
    
    
}
