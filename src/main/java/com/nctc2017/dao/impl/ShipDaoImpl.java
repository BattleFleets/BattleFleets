package com.nctc2017.dao.impl;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.StartShipEquipment;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.HoldDao;
import com.nctc2017.dao.MastDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.dao.extractors.EntityExtractor;
import com.nctc2017.dao.extractors.EntityListExtractor;
import com.nctc2017.dao.extractors.ExtractingVisitor;
import com.nctc2017.dao.utils.JdbcConverter;
import com.nctc2017.dao.utils.QueryBuilder;
import com.nctc2017.dao.utils.QueryExecutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
@Qualifier("shipDao")
public class ShipDaoImpl implements ShipDao {

    private static final Logger log = Logger.getLogger(ShipDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private QueryExecutor queryExecutor;
    @Autowired
    MastDao mastDao;
    @Autowired
    CannonDao cannonDao;
    @Autowired
    HoldDao holdDao;


    @Override
    public Ship findShip(BigInteger shipId) {
        Ship pickedUpShip = queryExecutor.findEntity(shipId, DatabaseObject.SHIP_OBJTYPE_ID,
                new EntityExtractor<>(shipId, new ShipVisitor()));
        if (pickedUpShip == null) {
            throwIAExceptionWithLog("Cannot find Ship,wrong Ship object id =  ", shipId);
        }
        return pickedUpShip;
    }

    @Override
    public Ship findShipTemplate(BigInteger shipTemplId) {
        Ship pickedUpShip = queryExecutor.findEntity(shipTemplId, DatabaseObject.SHIP_TEMPLATE_OBJTYPE_ID,
                new EntityExtractor<>(shipTemplId, new ShipVisitor()));
        if (pickedUpShip == null) {
            throwIAExceptionWithLog("Cannot find Ship,wrong Ship object id =  ", shipTemplId);
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
        BigInteger startMastTemplateId = queryExecutor.findAttrByRef(DatabaseObject.SHIP_TEMPLATE_OBJTYPE_ID,
                shipTempId,
                DatabaseObject.MAST_TEMPLATE_OBJTYPE_ID,
                DatabaseAttribute.ATTR_SHIP_START_MAST_TYPE,
                BigInteger.class);

        BigInteger startCannonTemplateId = queryExecutor.findAttrByRef(DatabaseObject.SHIP_TEMPLATE_OBJTYPE_ID,
                shipTempId,
                DatabaseObject.CANNON_TEMPLATE_TYPE_ID,
                DatabaseAttribute.ATTR_SHIP_START_CANNON_TYPE,
                BigInteger.class);

        StartShipEquipment pickedUpShipEquip = queryExecutor.findEntity(shipTempId,
                DatabaseObject.SHIP_TEMPLATE_OBJTYPE_ID,
                new EntityExtractor<>(shipTempId, new StartShipEquipmentVisitor()));

        pickedUpShipEquip.setStartCannonType(startCannonTemplateId);
        pickedUpShipEquip.setStartMastType(startMastTemplateId);

        if (pickedUpShipEquip == null) {
            throwIAExceptionWithLog("Cannot find ShipTemplate,wrong ShipTemplate object id =  ", shipTempId);
        }
        return pickedUpShipEquip;
    }

    @Override
    public BigInteger createNewShip(BigInteger shipTemplateId, BigInteger playerID) {
        BigInteger newId = queryExecutor.getNextval();
        Ship shipT = findShipTemplate(shipTemplateId);
        StartShipEquipment shipEquipment = findStartShipEquip(shipTemplateId);

        PreparedStatementCreator psc = QueryBuilder
                .insert(DatabaseObject.SHIP_OBJTYPE_ID, newId)
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

        //While methods of multiCreating are not implemented TODO
        for (int i = 0; i < shipEquipment.getStartNumMast(); i++)
            mastDao.createNewMast(shipEquipment.getStartMastType(), newId);
        for (int i = 0; i < shipEquipment.getStartNumCannon(); i++)
            cannonDao.createCannon(shipEquipment.getStartCannonType(), newId);

        return newId;
    }

    @Override
    public boolean deleteShip(BigInteger shipId) {
        queryExecutor.delete(shipId, DatabaseObject.SHIP_OBJTYPE_ID);
        return true;
    }

    @Override
    public boolean updateShipName(BigInteger shipId, String newShipName) {
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(shipId)
                .setAttribute(DatabaseAttribute.ATTR_CURR_SHIP_NAME, newShipName)
                .build();
        jdbcTemplate.update(psc);
        return true;
    }

    @Override
    public boolean updateShipHealth(BigInteger shipId, int newhealthNumb) {
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(shipId)
                .setAttribute(DatabaseAttribute.ATTR_CURR_SHIP_HEALTH, newhealthNumb)
                .build();
        jdbcTemplate.update(psc);
        return true;
    }

    @Override
    public boolean updateShipSailorsNumber(BigInteger shipId, int newsailorsNumb) {
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(shipId)
                .setAttribute(DatabaseAttribute.ATTR_CURR_SHIP_SAILORS, newsailorsNumb)
                .build();
        jdbcTemplate.update(psc);
        return true;
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
        return s.getCost();
    }

    @Override
    public List<ShipTemplate> findAllShipTemplates() {
        List<ShipTemplate> result= queryExecutor.getAllEntitiesByType(DatabaseObject.SHIP_TEMPLATE_OBJTYPE_ID,
                new EntityListExtractor<>(new ShipTemplateVisitor()));
        return result;
    }


    public List<Ship> findAllShips(List<BigInteger> shipsId) {
        // TODO
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

    @Override
    public int getMaxShotDistance(BigInteger shipId) {
        return 0;
    }

    @Override
    public int getSpeed(BigInteger shipId) {
        return 0;
    }

    private final class ShipVisitor implements ExtractingVisitor<Ship> {
        @Override
        public Ship visit(BigInteger entityId, Map<String, String> papamMap) {
            ShipTemplate shipT = new ShipTemplate(
                    entityId,
                    papamMap.remove(ShipTemplate.T_SHIPNAME),
                    Integer.valueOf(papamMap.remove(ShipTemplate.T_MAX_HEALTH)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.T_MAX_SAILORS_QUANTITY)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.T_MAX_COST)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.MAX_MASTS_QUANTITY)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.MAX_CANNON_QUANTITY)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.MAX_CARRYING_LIMIT))
            );

            int curCarryLimit = shipT.getMaxCarryingLimit() - holdDao.getOccupiedVolume(entityId);

            return new Ship(
                    shipT,
                    entityId,
                    papamMap.remove(Ship.NAME),
                    JdbcConverter.parseInt(papamMap.remove(Ship.CUR_HEALTH)),
                    JdbcConverter.parseInt(papamMap.remove(Ship.CUR_SAILORS_QUANTITY)),
                    curCarryLimit
            );
        }
    }

    private final class ShipTemplateVisitor implements ExtractingVisitor<ShipTemplate> {
        @Override
        public ShipTemplate visit(BigInteger entityId, Map<String, String> papamMap) {
            ShipTemplate shipT = new ShipTemplate(
                    entityId,
                    papamMap.remove(ShipTemplate.T_SHIPNAME),
                    Integer.valueOf(papamMap.remove(ShipTemplate.T_MAX_HEALTH)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.T_MAX_SAILORS_QUANTITY)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.T_MAX_COST)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.MAX_MASTS_QUANTITY)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.MAX_CANNON_QUANTITY)),
                    Integer.valueOf(papamMap.remove(ShipTemplate.MAX_CARRYING_LIMIT))
            );
            return shipT;
        }
    }

    private final class StartShipEquipmentVisitor implements ExtractingVisitor<StartShipEquipment> {
        @Override
        public StartShipEquipment visit(BigInteger entityId, Map<String, String> papamMap) {
            return new StartShipEquipment(
                    entityId,
                    null,
                    null,
                    Integer.valueOf(papamMap.remove(StartShipEquipment.START_NUM_CANNON)),
                    Integer.valueOf(papamMap.remove(StartShipEquipment.START_NUM_MAST))
            );
        }
    }
}