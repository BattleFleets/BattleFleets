package com.nctc2017.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;


import com.nctc2017.bean.Mast;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.MastDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("mastDao")
public class MastDaoImpl implements MastDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Mast findMast(int mastId) {
        if (mastId < 0)
            return null;
        Mast pickedUpMast = jdbcTemplate.query(Query.FIND_ANY_ENTITY,
                new Object[] { DatabaseObject.MAST_OBJTYPE_ID, mastId, DatabaseObject.MAST_OBJTYPE_ID, mastId },
                new MastExtractor(mastId));
        return pickedUpMast;
    }

    @Override
    public int createNewMast(int mastTemplateId) {
        // TODO implement here
        return 0;
    }


    @Override
    public void deleteMast(int mastId) {
        jdbcTemplate.update(Query.DELETE_ANY_OBJECT, new Object[] {mastId});
    }


    @Override
    public boolean updateCurMastSpeed(int mastId, int newMastSpeed) {
        // TODO implement here
        return false;
    }


    private List <Mast> getShipMastsFromAnywhere(int containerID) {
        List<Mast> pickedUpMasts = jdbcTemplate.query(Query.GET_ENTITIES_FROM_CONTAINER,
                new Object[] { DatabaseObject.MAST_OBJTYPE_ID, containerID, DatabaseObject.MAST_OBJTYPE_ID, containerID }, new MastListExtractor());
        return pickedUpMasts;
    }

    @Override
    public List<Mast> getShipMastsFromShip(int shipId) {
        return getShipMastsFromAnywhere(shipId);
    }

    @Override
    public List<Mast> getShipMastsFromStock(int stockId) {
        return getShipMastsFromAnywhere(stockId);
    }

    @Override
    public List<Mast> getShipMastsFromHold(int holdId) {
        return getShipMastsFromAnywhere(holdId);
    }


    @Override
    public int getCurMastSpeed(int mastId) {
        int result = findMast(mastId).getCurSpeed();
        return result;
    }


    @Override
    public String getMastName(int mastId) {
        String result = findMast(mastId).getTemplateName();
        return result;
    }


    @Override
    public int getSailyards(int mastId) {
        return 0;
    }


    @Override
    public int getMaxSpeed(int mastId) {
        int result = findMast(mastId).getSpeed();
        return result;
    }


    @Override
    public int getMastCost(int mastId) {
        int result = findMast(mastId).getCost();
        return result;
    }

    private static final String MAST_NAME = "MastName";
    private static final String MAX_SPEED = "Speed";
    private static final String Cur_MAST_SPEED = "CurMastSpeed";
    private static final String MAST_COST = "MastCost";
    private static final int QUANTITY = 1;

    private final class MastExtractor implements ResultSetExtractor<Mast> {
        private int mastId;


        public MastExtractor(int mastId){
            this.mastId = mastId;
        }

        @Override
        public Mast extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, String> papamMap = new HashMap<>();
            while(rs.next()){
                papamMap.put(rs.getString(1), rs.getString(2));
            }

            return new Mast(QUANTITY,
                    mastId,
                    papamMap.remove(MAST_NAME),
                    Integer.valueOf(papamMap.remove(MAX_SPEED)),
                    Integer.valueOf(papamMap.remove(Cur_MAST_SPEED)),
                    Integer.valueOf(papamMap.remove(MAST_COST)));
        }
    }

    private final class MastListExtractor implements ResultSetExtractor<List<Mast>> {

        @Override
        public List<Mast> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, String> papamMap;
            Map<Integer, Map<String, String>> mastMap = new HashMap<>();
            Integer idMast;
            while(rs.next()) {
                idMast = Integer.valueOf(rs.getString(1));
                papamMap = mastMap.get(idMast);
                if (papamMap == null){
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
            for (Entry<Integer, Map<String, String>> entry : mastMap.entrySet()) {
                nextParamMap = entry.getValue();
                nextMast = new Mast(QUANTITY,
                        entry.getKey(),
                        nextParamMap.remove(MAST_NAME),
                        Integer.valueOf(nextParamMap.remove(MAX_SPEED)),
                        Integer.valueOf(nextParamMap.remove(Cur_MAST_SPEED)),
                        Integer.valueOf(nextParamMap.remove(MAST_COST)));
                mastList.add(nextMast);
            }
            return mastList;
        }
    }

}