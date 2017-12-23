package com.nctc2017.dao.impl;

import com.nctc2017.bean.Mast;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.MastDao;
import com.nctc2017.dao.extractors.EntityExtractor;
import com.nctc2017.dao.extractors.ExtractingVisitor;
import com.nctc2017.dao.utils.JdbcConverter;
import com.nctc2017.dao.utils.QueryBuilder;
import com.nctc2017.dao.utils.QueryExecutor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Repository
@Qualifier("mastDao")
public class MastDaoImpl implements MastDao {

    private static final Logger log = Logger.getLogger(MastDaoImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    QueryExecutor queryExecutor;

    @Override
    public Mast findMast(@NotNull BigInteger mastId) {
        Mast pickedUpMast = queryExecutor.findEntity(mastId, DatabaseObject.MAST_OBJTYPE_ID,
                new EntityExtractor<>(mastId, new MastsVisitor()));
        if (pickedUpMast == null) {
            IllegalArgumentException e =
                    new IllegalArgumentException("Cannot find Mast,wrong mast object  id = " + mastId);
            log.log(Level.ERROR, "Exception: ", e);
            throw e;
        }
        return pickedUpMast;
    }

    private Mast findMastTemplate(@NotNull BigInteger mastTemplateId) {
        Mast pickedUpMast = queryExecutor.findEntity(mastTemplateId, DatabaseObject.MAST_TEMPLATE_OBJTYPE_ID,
                new EntityExtractor<>(mastTemplateId, new MastsVisitor()));
        if (pickedUpMast == null) {
            IllegalArgumentException e =
                    new IllegalArgumentException("Cannot find MastTemplate,wrong mastTemplateId = " + mastTemplateId);
            log.log(Level.ERROR, "Exception: ", e);
            throw e;
        }
        return pickedUpMast;
    }


    @Override
    public BigInteger createNewMast(BigInteger mastTemplateId, BigInteger containerOwnerId) {
        BigInteger newId = queryExecutor.getNextval();
        Mast templ = findMastTemplate(mastTemplateId);

        PreparedStatementCreator psc = QueryBuilder
                .insert(DatabaseObject.MAST_OBJTYPE_ID, newId)
                .setParentId(containerOwnerId)
                .setSourceObjId(mastTemplateId)
                .setAttribute(DatabaseAttribute.ATTR_CURR_MAST_SPEED_ID,
                        String.valueOf(findMastTemplate(mastTemplateId).getMaxSpeed()))
                .build();
        jdbcTemplate.update(psc);
        return newId;
    }


    @Override
    public void deleteMast(BigInteger mastId) {
        int numberOfDelRow = 0;
        try {
            numberOfDelRow = jdbcTemplate.update(Query.DELETE_OBJECT,
                    new Object[]{mastId.longValueExact(), DatabaseObject.MAST_OBJTYPE_ID.longValueExact()});
        } catch (ArithmeticException e) {
            log.log(Level.ERROR, "Arithmetical exception.Can not delete, id is to big: ", e);
            throw e;
        }

        if (numberOfDelRow == 0) {
            IllegalArgumentException ex = new IllegalArgumentException("Cant delete mast,wrong mastID = " + mastId);
            log.log(Level.ERROR, "Exception:", ex);
            throw ex;
        }

    }


    @Override
    public boolean updateCurMastSpeed(BigInteger mastId, int newMastSpeed) {
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(mastId)
                .setAttribute(DatabaseAttribute.ATTR_CURR_MAST_SPEED_ID, newMastSpeed)
                .build();
        jdbcTemplate.update(psc);
        return true;
    }


    private List<Mast> getShipMastsFromAnywhere(BigInteger containerID) {
        List<Mast> pickedUpMasts = jdbcTemplate.query(Query.GET_ENTITIES_FROM_CONTAINER,
                new Object[]{DatabaseObject.MAST_OBJTYPE_ID, containerID, DatabaseObject.MAST_OBJTYPE_ID, containerID},
                new MastListExtractor());
        if (pickedUpMasts == null) {
            log.log(Level.INFO, "Wrong containerID / Empty container");
        }
        return pickedUpMasts;
    }

    @Override
    public List<Mast> getShipMastsFromShip(BigInteger shipId) {
        return getShipMastsFromAnywhere(shipId);
    }

    @Override
    public List<Mast> getShipMastsFromStock(BigInteger stockId) {
        return getShipMastsFromAnywhere(stockId);
    }

    @Override
    public List<Mast> getShipMastsFromHold(BigInteger holdId) {
        return getShipMastsFromAnywhere(holdId);
    }


    @Override
    public int getCurMastSpeed(BigInteger mastId) {
        int result = findMast(mastId).getCurSpeed();
        return result;
    }


    @Override
    public String getMastName(BigInteger mastId) {
        String result = findMast(mastId).getTemplateName();
        return result;
    }


    @Override
    public int getSailyards(int mastId) {
        return 0;
    }


    @Override
    public int getMaxSpeed(BigInteger mastId) {
        int result = findMast(mastId).getMaxSpeed();
        return result;
    }


    @Override
    public int getMastCost(BigInteger mastId) {
        int result = findMast(mastId).getCost();
        return result;
    }


    private final class MastsVisitor implements ExtractingVisitor<Mast> {

        @Override
        public Mast visit(BigInteger entityId, Map<String, String> papamMap) {
            return new Mast(Mast.QUANTITY,
                    entityId,
                    papamMap.remove(Mast.MAST_NAME),
                    Integer.valueOf(papamMap.remove(Mast.MAX_SPEED)),
                    JdbcConverter.parseInt(papamMap.remove(Mast.Cur_MAST_SPEED)),
                    Integer.valueOf(papamMap.remove(Mast.MAST_COST)));
        }
    }

    private final class MastListExtractor implements ResultSetExtractor<List<Mast>> {

        @Override
        public List<Mast> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, String> papamMap;
            Map<BigInteger, Map<String, String>> mastMap = new HashMap<>();
            BigInteger idMast;
            while (rs.next()) {
                idMast = rs.getBigDecimal(1).toBigInteger();
                papamMap = mastMap.get(idMast);
                if (papamMap == null) {
                    papamMap = new HashMap<>();
                    papamMap.put(rs.getString(2), rs.getString(3));
                    mastMap.put(idMast, papamMap);
                } else {
                    papamMap.put(rs.getString(2), rs.getString(3));
                }
            }

            List<Mast> mastList = new ArrayList<>(mastMap.size());
            Mast nextMast;
            Map<String, String> nextParamMap;
            for (Entry<BigInteger, Map<String, String>> entry : mastMap.entrySet()) {
                nextParamMap = entry.getValue();
                nextMast = new Mast(Mast.QUANTITY,
                        entry.getKey(),
                        nextParamMap.remove(Mast.MAST_NAME),
                        Integer.valueOf(nextParamMap.remove(Mast.MAX_SPEED)),
                        Integer.valueOf(nextParamMap.remove(Mast.Cur_MAST_SPEED)),
                        Integer.valueOf(nextParamMap.remove(Mast.MAST_COST)));
                mastList.add(nextMast);
            }
            return mastList;
        }
    }

}