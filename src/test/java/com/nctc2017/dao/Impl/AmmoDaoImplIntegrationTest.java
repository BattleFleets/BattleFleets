package com.nctc2017.dao.Impl;

import java.math.BigInteger;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Ammo;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.AmmoDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@Transactional
public class AmmoDaoImplIntegrationTest {
    
    @Autowired
    private AmmoDao ammoDao;
    
    @Test
    @Rollback(true)
    public void testCannonballCreateThenFindOk() {
        // Given
        int quantity = 13;
        // When
        BigInteger ammoId = ammoDao.createAmmo(DatabaseObject.CANNONBALL_TEMPLATE_OBJECT_ID, quantity);
        // Then
        Ammo ammo = ammoDao.findById(ammoId);
        assertEquals(ammo.getQuantity(), quantity);
        assertEquals(ammo.getThingId(), ammoId);
        assertTrue(ammo.getDamageType().toLowerCase().indexOf("hull") != -1);
    }
    
    @Test
    @Rollback(true)
    public void testChainCreateThenFindOk() {
        // Given
        int quantity = 23;
        // When
        BigInteger ammoId = ammoDao.createAmmo(DatabaseObject.CHAIN_TEMPLATE_OBJECT_ID, quantity);
        // Then
        Ammo ammo = ammoDao.findById(ammoId);
        assertEquals(ammo.getQuantity(), quantity);
        assertEquals(ammo.getThingId(), ammoId);
        assertTrue(ammo.getDamageType().toLowerCase().indexOf("mast") != -1);
    }
    
    @Test
    @Rollback(true)
    public void testBuckshotCreateThenFindOk() {
        // Given
        int quantity = 19;
        // When
        BigInteger ammoId = ammoDao.createAmmo(DatabaseObject.BUCKSHOT_TEMPLATE_OBJECT_ID, quantity);
        // Then
        Ammo ammo = ammoDao.findById(ammoId);
        assertEquals(ammo.getQuantity(), quantity);
        assertEquals(ammo.getThingId(), ammoId);
        assertTrue(ammo.getDamageType().toLowerCase().indexOf("crew") != -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void testFindFailWhenWrongId() {
        // Given
        BigInteger ammoId = BigInteger.ONE;
        // When
        ammoDao.findById(ammoId);
        // Then Exception
    }
    
    @Test
    @Rollback(true)
    public void testGetAmmoQuantitySuccess() {
        // Given
        int quantity = 17;
        BigInteger ammoId = ammoDao.createAmmo(DatabaseObject.BUCKSHOT_TEMPLATE_OBJECT_ID, quantity);
        // When
        int quantityRes = ammoDao.getAmmoQuantity(ammoId);
        assertEquals(quantityRes, quantity);
        // Then Exception
    }
    
    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void testGetAmmoQuantityFailWhenWrongId() {
        // Given
        BigInteger ammoId = BigInteger.ONE;
        // When
        ammoDao.getAmmoQuantity(ammoId);
        // Then Exception
    }
    
    @Test
    @Rollback(true)
    public void testIncreaseAmmoQuantitySuccess() {
        // When
        int quantity = 41;
        int increase = 9;
        BigInteger ammoId = ammoDao.createAmmo(DatabaseObject.BUCKSHOT_TEMPLATE_OBJECT_ID, quantity);
        // Then
        boolean res = ammoDao.increaseAmmoQuantity(ammoId, increase);
        int quantityRes = ammoDao.getAmmoQuantity(ammoId);
        // Then
        assertTrue(res);
        assertEquals(quantity + increase, quantityRes);
    }
    
    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void testIncreaseAmmoQuantityFailWhenWrongId() {
        int increase = 9;
        // When
        BigInteger ammoId =BigInteger.ONE;
        // Then
        ammoDao.increaseAmmoQuantity(ammoId, increase);
        // Then Exception
        
    }
}
