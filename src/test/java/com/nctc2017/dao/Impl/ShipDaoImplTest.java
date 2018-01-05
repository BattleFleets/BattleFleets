package com.nctc2017.dao.Impl;


import com.nctc2017.bean.*;
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
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
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
    private static String SHIP_TEMPALTE_CARRACA_NAME = "T_Caracca";
    private static String SHIP_TEMPALTE_GALION_NAME = "T_Galion";
    private static String SHIP_TEMPALTE_CLIPPER_NAME = "T_Clipper";
    private static String SHIP_TEMPALTE_FREGATA_NAME = "T_Fregata";

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
    public void testShipCaravellaCreating() {
        int expectedNumberOfCannonInCaravella = 20;
        String typeOfCannon = "Mortar";
        int expectedNumberOfMastInCaravella = 3;
        String typeOfMast = "T_Mast1";

        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, null);
        List<Cannon> cannonsInCreatedId = cannonDao.getAllCannonFromShip(createdId);
        List<Mast> mastsInCreatedId = mastDao.getShipMastsFromShip(createdId);
        Ship s = shipDao.findShip(createdId);


        assertTrue(s.getMaxHealth() == s.getCurHealth());
        assertTrue(s.getMaxCarryingLimit() == s.getCurCarryingLimit());
        assertTrue(s.getMaxSailorsQuantity() == s.getCurSailorsQuantity());

        assertEquals(typeOfMast,mastsInCreatedId.get(0).getTemplateName());
        assertEquals(expectedNumberOfMastInCaravella, mastsInCreatedId.size());
        assertEquals(typeOfCannon,cannonsInCreatedId.get(0).getName());
        assertEquals(expectedNumberOfCannonInCaravella, cannonsInCreatedId.size());
        assertEquals(SHIP_TEMPALTE_CARAVELA_NAME, s.getTName());
    }

    @Test
    @Rollback(true)
    public void testShipCarracaCreating() {
        int expectedNumberOfCannonInCaravella = 12;
        String typeOfCannon = "Bombard";
        int expectedNumberOfMastInCaravella = 3;
        String typeOfMast = "T_Mast2";

        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_CARАССА_OBJECT_ID, null);
        List<Cannon> cannonsInCreatedId = cannonDao.getAllCannonFromShip(createdId);
        List<Mast> mastsInCreatedId = mastDao.getShipMastsFromShip(createdId);
        Ship s = shipDao.findShip(createdId);


        assertTrue(s.getMaxHealth() == s.getCurHealth());
        assertTrue(s.getMaxCarryingLimit() == s.getCurCarryingLimit());
        assertTrue(s.getMaxSailorsQuantity() == s.getCurSailorsQuantity());

        assertEquals(typeOfMast,mastsInCreatedId.get(0).getTemplateName());
        assertEquals(expectedNumberOfMastInCaravella, mastsInCreatedId.size());
        assertEquals(typeOfCannon,cannonsInCreatedId.get(0).getName());
        assertEquals(expectedNumberOfCannonInCaravella, cannonsInCreatedId.size());
        assertEquals(SHIP_TEMPALTE_CARRACA_NAME, s.getTName());
    }

    @Test
    @Rollback(true)
    public void testShipGalionCreating() {
        int expectedNumberOfCannonInCaravella = 15;
        String typeOfCannon = "Kulevrin";
        int expectedNumberOfMastInCaravella = 3;
        String typeOfMast = "T_Mast4";

        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_GALION_OBJECT_ID, null);
        List<Cannon> cannonsInCreatedId = cannonDao.getAllCannonFromShip(createdId);
        List<Mast> mastsInCreatedId = mastDao.getShipMastsFromShip(createdId);
        Ship s = shipDao.findShip(createdId);


        assertTrue(s.getMaxHealth() == s.getCurHealth());
        assertTrue(s.getMaxCarryingLimit() == s.getCurCarryingLimit());
        assertTrue(s.getMaxSailorsQuantity() == s.getCurSailorsQuantity());

        assertEquals(typeOfMast,mastsInCreatedId.get(0).getTemplateName());
        assertEquals(expectedNumberOfMastInCaravella, mastsInCreatedId.size());
        assertEquals(typeOfCannon,cannonsInCreatedId.get(0).getName());
        assertEquals(expectedNumberOfCannonInCaravella, cannonsInCreatedId.size());
        assertEquals(SHIP_TEMPALTE_GALION_NAME, s.getTName());
    }

    @Test
    @Rollback(true)
    public void testShipClipperCreating() {
        int expectedNumberOfCannonInCaravella = 15;
        String typeOfCannon = "Bombard";
        int expectedNumberOfMastInCaravella = 3;
        String typeOfMast = "T_Mast3";

        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_CLIPPER_OBJECT_ID, null);
        List<Cannon> cannonsInCreatedId = cannonDao.getAllCannonFromShip(createdId);
        List<Mast> mastsInCreatedId = mastDao.getShipMastsFromShip(createdId);
        Ship s = shipDao.findShip(createdId);


        assertTrue(s.getMaxHealth() == s.getCurHealth());
        assertTrue(s.getMaxCarryingLimit() == s.getCurCarryingLimit());
        assertTrue(s.getMaxSailorsQuantity() == s.getCurSailorsQuantity());

        assertEquals(typeOfMast,mastsInCreatedId.get(0).getTemplateName());
        assertEquals(expectedNumberOfMastInCaravella, mastsInCreatedId.size());
        assertEquals(typeOfCannon,cannonsInCreatedId.get(0).getName());
        assertEquals(expectedNumberOfCannonInCaravella, cannonsInCreatedId.size());
        assertEquals(SHIP_TEMPALTE_CLIPPER_NAME, s.getTName());
    }

    @Test
    @Rollback(true)
    public void testShipFregataCreating() {
        int expectedNumberOfCannonInCaravella = 22;
        String typeOfCannon = "Kulevrin";
        int expectedNumberOfMastInCaravella = 3;
        String typeOfMast = "T_Mast3";

        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_FREGATA_OBJECT_ID, null);
        List<Cannon> cannonsInCreatedId = cannonDao.getAllCannonFromShip(createdId);
        List<Mast> mastsInCreatedId = mastDao.getShipMastsFromShip(createdId);
        Ship s = shipDao.findShip(createdId);


        assertTrue(s.getMaxHealth() == s.getCurHealth());
        assertTrue(s.getMaxCarryingLimit() == s.getCurCarryingLimit());
        assertTrue(s.getMaxSailorsQuantity() == s.getCurSailorsQuantity());

        assertEquals(typeOfMast,mastsInCreatedId.get(0).getTemplateName());
        assertEquals(expectedNumberOfMastInCaravella, mastsInCreatedId.size());
        assertEquals(typeOfCannon,cannonsInCreatedId.get(0).getName());
        assertEquals(expectedNumberOfCannonInCaravella, cannonsInCreatedId.size());
        assertEquals(SHIP_TEMPALTE_FREGATA_NAME, s.getTName());
    }

    @Test
    @Rollback(true)
    public void testTemplateFinding() {
        int expectedNumberOfTemplates = 5;
        List<ShipTemplate> shipTempList = shipDao.findAllShipTemplates();

        assertEquals(expectedNumberOfTemplates, shipTempList.size());
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

    @Test
    @Rollback
    public void getShipsFeateresByRequest() {
        BigInteger createdId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, null);
        Ship findShip = shipDao.findShip(createdId);

        int actualMaxSailorQuantity = shipDao.getSailorLimit(createdId);
        int actualCurrentShipSailors = shipDao.getCurrentShipSailors(createdId);
        int actualCurrentShipHealth = shipDao.getCurrentShipHealth(createdId);
        int actualShipCost = shipDao.getShipCost(createdId);
        int actualCannonLimit = shipDao.getCannonLimit(createdId);
        int actualMastLimit = shipDao.getMastLimit(createdId);
        String actualCurrentShipName = shipDao.getCurrentShipName(createdId);
        int actualCarryingLimit = shipDao.getCarryingLimit(createdId);

        assertEquals(findShip.getMaxSailorsQuantity(), actualMaxSailorQuantity);
        assertEquals(findShip.getCurSailorsQuantity(), actualCurrentShipSailors);
        assertEquals(findShip.getCurHealth(), actualCurrentShipHealth);
        assertEquals(findShip.getCost(), actualShipCost);
        assertEquals(findShip.getMaxCannonQuantity(), actualCannonLimit);
        assertEquals(findShip.getMaxMastsQuantity(), actualMastLimit);
        assertEquals(findShip.getCurName(), actualCurrentShipName);
        assertEquals(findShip.getMaxCarryingLimit(), actualCarryingLimit);
    }
    @Test
    @Rollback(true)
    public void findAllShips() throws Exception{
        playerDao.addNewPlayer("Tony", "1111", "Stark@gmail.com");
        Player tony = playerDao.findPlayerByLogin("Tony");
        shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, tony.getPlayerId());
        List<BigInteger> shipsId = playerDao.findAllShip(tony.getPlayerId());
        List<Ship> ships = shipDao.findAllShips(shipsId);
        assertEquals(ships.size(), 1);
    }

}

