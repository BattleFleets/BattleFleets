package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import java.util.Map.Entry;
import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.StartShipEquipment;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.HoldDao;
import com.nctc2017.dao.MastDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.dao.utils.JdbcConverter;
import com.nctc2017.dao.utils.QueryBuilder;
import com.nctc2017.dao.utils.QueryExecutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("shipDao")
public class ShipDaoImpl implements ShipDao {

    private static final Logger log = Logger.getLogger(ShipDaoImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MastDao mastDao;
    @Autowired
    HoldDao holdDao;
    @Autowired
    private QueryExecutor queryExecutor;

    @Override
    public Ship findShip(BigInteger shipId) {
        Ship pickedUpShip = queryExecutor.findEntity(shipId,DatabaseObject.SHIP_OBJTYPE_ID,new ShipExtractor(shipId));
        if (pickedUpShip == null) {
            throwIAExceptionWithLog("Cannot find Ship,wrong Ship object id =  ",shipId);
        }
        return pickedUpShip;
    }

    private Ship findShipTemplate(BigInteger shipTemplId) {
        Ship pickedUpShip = queryExecutor.findEntity(shipTemplId,DatabaseObject.SHIP_TEMPLATE_OBJTYPE_ID,
                new ShipExtractor(shipTemplId));
        if (pickedUpShip == null) {
            throwIAExceptionWithLog("Cannot find Ship,wrong Ship object id =  ",shipTemplId);
        }
        return pickedUpShip;
    }

    private void throwIAExceptionWithLog(String message, BigInteger unknownId) {
        RuntimeException e =
                new IllegalArgumentException(message + unknownId);
        log.error("Exception: ", e);
        throw e;
    }

    private StartShipEquipment findStartShipEquip(BigInteger shipTempId) {
        StartShipEquipment pickedUpShipEquip = queryExecutor.findEntity(shipTempId,DatabaseObject.SHIP_TEMPLATE_OBJTYPE_ID,
                new ShipEqupmentExtr(shipTempId));
        if (pickedUpShipEquip == null) {
            throwIAExceptionWithLog("Cannot find ShipTemplate,wrong ShipTemplate object id =  ", shipTempId);
        }
        return pickedUpShipEquip;
    }

    @Override
    public BigInteger createNewShip(BigInteger shipTemplateId, BigInteger playerID) {
        BigDecimal newId = jdbcTemplate.queryForObject(Query.GET_NEXTVAL, BigDecimal.class);
        Ship shipT = findShipTemplate(shipTemplateId);
        StartShipEquipment shipEquipment = findStartShipEquip(shipTemplateId);

        //While methods of multiCreating are not implemented TODO
        for(int i = 0;i < shipEquipment.getStartNumMast();i++)
            mastDao.createNewMast(shipEquipment.getStartMastType(),newId.toBigIntegerExact());
        for(int i = 0;i < shipEquipment.getStartNumCannon();i++)
            mastDao.createNewMast(shipEquipment.getStartCannonType(),newId.toBigIntegerExact());

        PreparedStatementCreator psc = QueryBuilder
                .insert(DatabaseObject.SHIP_OBJTYPE_ID)
                .setParentId(playerID)
                .setSourceObjId(shipTemplateId)
                .setAttribute(DatabaseAttribute.ATTR_CURR_SHIP_NAME,
                        String.valueOf(shipT.getTName()))
                .setAttribute(DatabaseAttribute.ATTR_CURR_SHIP_HEALTH,
                        String.valueOf(shipT.getMaxHealth()))
                .setAttribute(DatabaseAttribute.ATTR_CURR_SHIP_SAILORS,
                        String.valueOf(shipT.getMaxSailorsQuantity()))
                .build();

        jdbcTemplate.update(psc);
        return newId.toBigIntegerExact();
    }

    @Override
    public boolean deleteShip(BigInteger shipId) {
        new QueryExecutor().delete(shipId,DatabaseObject.SHIP_OBJTYPE_ID);
        return false;
    }

    @Override
    public boolean updateShipName(BigInteger shipId, int newShipName) {
        // TODO implement here
        return false;
    }

    @Override
    public boolean updateShipHealth(BigInteger shipId, int newhealthNumb) {
        // TODO implement here
        return false;
    }

    @Override
    public boolean updateShipSailorsNumber(BigInteger shipId, int newsailorsNumb) {
        // TODO implement here
        return false;
    }

    @Override
    public String getCurrentShipName(BigInteger shipId) {
        Ship s = findShip(shipId);
        return s.getCurName();
    }

    @Override
    public int getCurrentShipHealth(BigInteger shipId) {
        Ship s = findShip(shipId);
        return s.getCurHealth();
    }

    @Override
    public int getCurrentShipSailors(BigInteger shipId) {
        Ship s = findShip(shipId);
        return s.getCurSailorsQuantity();
    }

    @Override
    public int getHealthLimit(BigInteger shipId) {
        Ship s = findShip(shipId);
        return s.getMaxHealth();
    }

    @Override
    public int getCarryingLimit(BigInteger shipId) {
        Ship s = findShip(shipId);
        return s.getMaxCarryingLimit();
    }

    @Override
    public int getCannonLimit(BigInteger shipId) {
        Ship s = findShip(shipId);
        return s.getMaxCannonQuantity();
    }

    @Override
    public int getMastLimit(BigInteger shipId) {
        Ship s = findShip(shipId);
        return s.getMaxMastsQuantity();
    }

    @Override
    public int getSailorLimit(BigInteger shipId) {
        Ship s = findShip(shipId);
        return s.getMaxSailorsQuantity();
    }

    @Override
    public int getShipCost(BigInteger shipId) {
        Ship s = findShip(shipId);
        //TODO
        return 0;
    }

    @Override
    public List<ShipTemplate> findAllShipTemplates() {
        return null;
    }

    @Override
    public List<Ship> findAllShips(List<Integer> shipsId) {
        return null;
    }

    @Override
    public boolean setMastOnShip(BigInteger mastId, BigInteger shipId) {
        int rowsAffected = new QueryExecutor().putEntityToContainer(shipId, mastId, DatabaseObject.SHIP_OBJTYPE_ID);
        if (rowsAffected == 0) {
                log.warn("Can not put mast: " + mastId + " on the ship " + shipId);
                return false;
            }
        return true;
    }

    @Override
    public boolean setCannonOnShip(BigInteger cannonId, BigInteger shipId) {
        int rowsAffected = new QueryExecutor().putEntityToContainer(shipId, cannonId, DatabaseObject.SHIP_OBJTYPE_ID);
        if (rowsAffected == 0) {
            log.warn("Can not put cannon: " + cannonId + " on the ship " + shipId);
            return false;
        }
        return true;
    }

    private final class ShipEqupmentExtr implements ResultSetExtractor<StartShipEquipment> {
        private BigInteger shipTId;

        public ShipEqupmentExtr(BigInteger shipTId) {
            this.shipTId = shipTId;
        }

        @Override
        public StartShipEquipment extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, String> papamMap = new HashMap<>();
            while (rs.next()) {
                papamMap.put(rs.getString(1), rs.getString(2));
            }
            return new StartShipEquipment(
                    shipTId,
                    new BigInteger(papamMap.remove(StartShipEquipment.START_CANNON_TYPE)),
                    new BigInteger(papamMap.remove(StartShipEquipment.START_MAST_TYPE)),
                    Integer.valueOf(papamMap.remove(StartShipEquipment.START_NUM_CANNON)),
                    Integer.valueOf(papamMap.remove(StartShipEquipment.START_NUM_MAST))
            );
        }
    }

    private final class ShipExtractor implements ResultSetExtractor<Ship> {
        private BigInteger shipId;

        public ShipExtractor(BigInteger shipId) {
            this.shipId = shipId;
        }

        @Override
        public Ship extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, String> papamMap = new HashMap<>();
            while (rs.next()) {
                papamMap.put(rs.getString(1), rs.getString(2));
            }
            ShipTemplate shipT = new ShipTemplate(
                    papamMap.remove(ShipTemplate.T_SHIPNAME),
                    Integer.valueOf(papamMap.remove(ShipTemplate.T_MAX_HEALTH)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.T_MAX_SAILORS_QUANTITY)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.T_MAX_COST)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.MAX_MASTS_QUANTITY)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.MAX_CANNON_QUANTITY)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.MAX_CARRYING_LIMIT))
            );

            int curCarryLimit = shipT.getMaxCarryingLimit() - holdDao.getOccupiedVolume(shipId);

            return new Ship(
                    shipT,
                    shipId,
                    papamMap.remove(Ship.NAME),
                    JdbcConverter.parseInt(papamMap.remove(Ship.CUR_HEALTH)),
                    JdbcConverter.parseInt(papamMap.remove(Ship.CUR_SAILORS_QUANTITY)),
                    curCarryLimit
            );
        }
    }

    private final class ShipListExtractor implements ResultSetExtractor<List<Ship>> {

        @Override
        public List<Ship> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, String> papamMap;
            Map<BigDecimal, Map<String, String>> shipMap = new HashMap<>();
            BigDecimal idShip;
            while (rs.next()) {
                idShip = rs.getBigDecimal(1);
                papamMap = shipMap.get(idShip);
                if (papamMap == null) {
                    papamMap = new HashMap<>();
                    papamMap.put(rs.getString(2), rs.getString(3));
                    shipMap.put(idShip, papamMap);
                } else {
                    papamMap.put(rs.getString(2), rs.getString(3));
                }
            }

            List<Ship> shipList = new ArrayList<>(shipMap.size());
            Ship nextShip;
            Map<String, String> nextParamMap;
            for (Entry<BigDecimal, Map<String, String>> entry : shipMap.entrySet()) {
                nextParamMap = entry.getValue();
                BigInteger shipId = entry.getKey().toBigInteger();
                ShipTemplate shipT = new ShipTemplate(
                        nextParamMap.remove(ShipTemplate.T_SHIPNAME),
                        Integer.valueOf(nextParamMap.remove(ShipTemplate.T_MAX_HEALTH)),
                        Integer.valueOf(nextParamMap.remove(ShipTemplate.T_MAX_SAILORS_QUANTITY)),
                        Integer.valueOf(nextParamMap.remove(ShipTemplate.T_MAX_COST)),
                        Integer.valueOf(nextParamMap.remove(ShipTemplate.MAX_MASTS_QUANTITY)),
                        Integer.valueOf(nextParamMap.remove(ShipTemplate.MAX_CANNON_QUANTITY)),
                        Integer.valueOf(nextParamMap.remove(ShipTemplate.MAX_CARRYING_LIMIT))
                );

                int curCarryLimit = shipT.getMaxCarryingLimit()-holdDao.getOccupiedVolume(shipId);
                nextShip = new Ship(
                        shipT,
                        shipId,
                        nextParamMap.remove(Ship.NAME),
                        JdbcConverter.parseInt(nextParamMap.remove(Ship.CUR_HEALTH)),
                        JdbcConverter.parseInt(nextParamMap.remove(Ship.CUR_SAILORS_QUANTITY)),
                        curCarryLimit);
                shipList.add(nextShip);
            }
            return shipList;
        }
    }
}