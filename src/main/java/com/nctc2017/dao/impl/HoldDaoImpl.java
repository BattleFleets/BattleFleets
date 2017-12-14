package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.HoldDao;

@Repository
@Qualifier("holdDao")
public class HoldDaoImpl implements HoldDao {

    private static Logger log = Logger.getLogger(HoldDaoImpl.class.getName());
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger findHold(BigInteger shipId) {
        BigDecimal holdId;
        try {
            holdId = jdbcTemplate.queryForObject(Query.FIND_CONTAINER_BY_OWNER_ID,
                    new Object[] { DatabaseObject.SHIP_OBJTYPE_ID, shipId.longValueExact() },
                    BigDecimal.class);
            return holdId.toBigInteger();
        } catch (EmptyResultDataAccessException e) {
            throwRuntimeException(new IllegalArgumentException("Wrong ship object id to find Hold. Id = " + shipId));
        }
        return null; // never happens
    }

    @Override
    public BigInteger getOccupiedVolume(BigInteger shipId) {
        List<BigDecimal> entitiesId = jdbcTemplate.queryForList(Query.FIND_ALL_IN_CONTAINER_BY_OWNER_ID,
                new Object[] { DatabaseObject.SHIP_OBJTYPE_ID, shipId.longValueExact() },
                BigDecimal.class);
        //if (entitiesId == null) throwRuntimeException(new IllegalArgumentException("Wrong ship object id to find Hold. Id = " + shipId));

        return BigInteger.valueOf(entitiesId.size());
    }

    @Override
    public BigInteger createHold() {
        return createHold(null);
    }

    @Override
    public BigInteger createHold(BigInteger shipId) {
        int rowsAffected = jdbcTemplate.update(Query.CRATE_NEW_CONTAINER,
                new Object[] {shipId == null ? null : shipId.longValueExact(),
                        DatabaseObject.HOLD_OBJTYPE_ID,
                        DatabaseObject.HOLD_OBJTYPE_ID});
        if (rowsAffected == 0) throwRuntimeException(new IllegalStateException("No effect on database"));

        return jdbcTemplate.queryForObject(Query.GET_CURRVAL,BigDecimal.class).toBigInteger();
    }

    @Override
    public void deleteHold(BigInteger holdId) {
        int rowsAffected = jdbcTemplate.update(Query.DELETE_OBJECT,
                new Object[] {holdId.longValueExact(), DatabaseObject.HOLD_OBJTYPE_ID});
        if (rowsAffected == 0) log.log(Level.WARN,"Nothing to delete from database");
    }

    @Override
    public boolean addCargo(BigInteger cargoId, BigInteger holdId) {
        Long holdIdLong = holdId.longValueExact();
        Long cargoIdLong = cargoId.longValueExact();
        int rowsAffected = jdbcTemplate.update(Query.PUT_ENTITY_TO_CONTAINER,
                new Object[] {holdIdLong, cargoIdLong,
                        holdIdLong, holdIdLong, DatabaseObject.HOLD_OBJTYPE_ID});
        if (rowsAffected == 1)
            return true;
        if (rowsAffected == 0)
            return false;
        else
            throwRuntimeException(new IllegalStateException("Cargo was put into several holds"));
        return false;
    }

    private void throwRuntimeException(RuntimeException ex) {
        log.log(Level.ERROR, "DAOException: ", ex);
        throw ex;
    }
}
