package com.nctc2017.dao.impl;

import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.StockDao;
import com.nctc2017.dao.utils.JdbcConverter;
import com.nctc2017.dao.utils.QueryBuilder;
import com.nctc2017.dao.utils.QueryExecutor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;


@Repository
@Qualifier("stockDao")
public class StockDaoImpl implements StockDao {

    private static final Logger log = Logger.getLogger(StockDaoImpl.class);
    private static final String CHECK_EXISTENCE_QUERY = "SELECT count(*) FROM objects " +
            "WHERE object_id = ? " +
            "AND source_id = ? " +
            "AND parent_id = ?;";


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private QueryExecutor queryExecutor;


    @Override
    public BigInteger findStockId(BigInteger playerId) {
        try {
            BigInteger stockId = queryExecutor.findContainerByOwnerId(DatabaseObject.STOCK_OBJTYPE_ID, playerId, DatabaseObject.PLAYER_OBJTYPE_ID);
            return stockId;

        } catch (EmptyResultDataAccessException e) {
            RuntimeException ex = new IllegalArgumentException("Wrong player object id to find Stock. Id = " + playerId);
            log.log(Level.ERROR, "StockDao Exception while finding stock by player id.", ex);
            throw ex;
        }
    }

    @Override
    public BigInteger createStock(BigInteger playerId) {
        if(stockExists(playerId)){
            RuntimeException e = new IllegalStateException("Stock already exists. One player cannot have more than 1 stock at the time.");
            log.log(Level.ERROR, "StockDAO Exception while creating new stock.", e);
            throw e;
        }

        BigInteger newObjId = jdbcTemplate.queryForObject(Query.GET_NEXTVAL, BigDecimal.class).toBigInteger();

        PreparedStatementCreator psc = QueryBuilder.insert(DatabaseObject.STOCK_OBJTYPE_ID, newObjId)
                .setParentId(playerId)
                .build();

        int rowsAffected = jdbcTemplate.update(psc);
        if (rowsAffected == 0) {
            RuntimeException e = new IllegalStateException("No stock was created, one expected.");
            log.log(Level.ERROR, "StockDAO Exception while creating new stock.", e);
            throw e;
        }
        return newObjId;
    }

    @Override
    public void deleteStock(BigInteger playerId) {
        BigInteger stockId = findStockId(playerId);

        PreparedStatementCreator psc = QueryBuilder.delete(stockId).build();

        int rowsAffected = jdbcTemplate.update(psc);
        if (rowsAffected == 0) {
            RuntimeException e = new IllegalStateException("No stock was deleted, one expected.");
            log.log(Level.ERROR, "StockDAO Exception while deleting stock.", e);
            throw e;
        }

    }

    private boolean stockExists(BigInteger playerId) {
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM objects " +
                        "WHERE parent_id = ? and object_type_id = ?",
                new Object[] {JdbcConverter.toNumber(playerId),
                        JdbcConverter.toNumber(DatabaseObject.STOCK_OBJTYPE_ID) }, Integer.class);
        return count != 0;
    }

    @Override
    public void addCargo(BigInteger cargoId, BigInteger playerId) {
        BigInteger stockId = findStockId(playerId);

        PreparedStatementCreator psc = QueryBuilder.updateParent(cargoId, stockId).build();

        int rowsAffected = jdbcTemplate.update(psc);
        if (rowsAffected == 0) {
            RuntimeException e = new IllegalStateException("No cargo was added, one expected.");
            log.log(Level.ERROR, "StockDAO Exception while adding cargo.", e);
            throw e;
        }

    }

    @Override
    public boolean isSuchCargoInStock(BigInteger cargoId, BigInteger cargoTemplateId, BigInteger stockId){
        int count = jdbcTemplate.queryForObject(CHECK_EXISTENCE_QUERY,
                new Object[]{JdbcConverter.toNumber(cargoId),
                        JdbcConverter.toNumber(cargoTemplateId),
                        JdbcConverter.toNumber(stockId)},
                Integer.class);
        return count != 0;
    }



}