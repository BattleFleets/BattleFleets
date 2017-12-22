package com.nctc2017.dao.Impl;


import com.nctc2017.bean.Ship;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.MastDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@Transactional
public class ShipDaoImplTest {

    @Autowired
    ShipDao shipDao;
    @Autowired
    MastDao mastDao;
    @Autowired
    CannonDao cannonDao;
    @Autowired
    PlayerDao playerDao;

    private static String SHIP_TEMPALTE_CARAVELA_NAME = "T_Caravela";

    @Test
    @Rollback(true)
    public void testShipFinding() {
        BigInteger createdID = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, null);
        Ship s = shipDao.findShip(createdID);
        assertEquals(SHIP_TEMPALTE_CARAVELA_NAME, s.getTName());
        assertTrue(s.getMaxHealth() > 0);
        assertTrue(s.getMaxCarryingLimit() > 0);
        assertTrue(s.getMaxCannonQuantity() > 0);
        assertTrue(s.getMaxMastsQuantity() > 0);
        assertTrue(s.getMaxSailorsQuantity() > 0);
    }

    @Test
    @Rollback(true)
    public void testShipCreating() {
        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, null);
        Ship s = shipDao.findShip(createdId);
        assertEquals(SHIP_TEMPALTE_CARAVELA_NAME, s.getTName());
        assertTrue(s.getMaxHealth() == s.getCurHealth());
        assertTrue(s.getMaxCarryingLimit() == s.getCurCarryingLimit());
        assertTrue(s.getMaxSailorsQuantity() == s.getCurSailorsQuantity());
    }

    @Test
    @Rollback(true)
    public void testShipDelete() {
        // Given
        BigInteger createdShip = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, null);
        // when
        shipDao.deleteShip(createdShip);
        // then ok
    }

    @Test
    @Rollback(true)
    public void changeShipName() {
        String expectedName = "Drakkar";
        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, null);
        shipDao.updateShipName(createdId, expectedName);

        Ship createdShip = shipDao.findShip(createdId);

        assertEquals(expectedName, createdShip.getCurName());
    }

    @Test
    @Rollback(true)
    public void changeShipHealth() {
        int expectedHealth = 10;
        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, null);
        shipDao.updateShipHealth(createdId, expectedHealth);

        Ship createdShip = shipDao.findShip(createdId);

        assertEquals(expectedHealth, createdShip.getCurHealth());
    }

    @Test
    @Rollback(true)
    public void changeShipSailorNumb() {
        int expectedSailorNumb = 1;

        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, null);
        shipDao.updateShipSailorsNumber(createdId, expectedSailorNumb);

        Ship createdShip = shipDao.findShip(createdId);
        assertEquals(expectedSailorNumb, createdShip.getCurSailorsQuantity());
    }

}

