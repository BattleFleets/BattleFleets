package com.nctc2017.dao.Impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.dao.HoldDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@Transactional
public class HoldDaoImplIntegrationTest {
    @Autowired
    HoldDao holdDao;
    
    @Test
    @Rollback(true)
    public void testCreateHold() {
        // When
        BigInteger id = holdDao.createHold();
        
        // Then
        assertNotNull(id);
        assertTrue(id.longValueExact() > 0L);
    }
    
    @Test
    @Ignore
    @Rollback(true)
    public void testCreateHoldForShip() {
        //TODO create ship
        // When
        BigInteger id = holdDao.createHold();
        
        // Then
        assertNotNull(id);
        assertTrue(id.longValueExact() > 0L);
    }
    
    @Test
    @Rollback(true)
    public void testDeleteHold() {
        // Given
        BigInteger id = holdDao.createHold();
        
        // When
        holdDao.deleteHold(id);
        // Then okay
    }
    
    @Test
    @Rollback(true)
    public void testDeleteHoldFail() {
        // Given
        BigInteger id = BigInteger.ONE;
        
        // When
        holdDao.deleteHold(id);
        // Then okay
    }
    
    @Test
    @Ignore
    @Rollback(true)
    public void testAddCargo() {
        // Given
        //TODO create cargo
        BigInteger id = holdDao.createHold();
        
        // When
        //holdDao.addCargo(cargoId, holdId);
        // Then okay
    }
    
    @Test
    @Ignore
    @Rollback(true)
    public void testAddCargoToInvalidHold() {
        // Given
        //TODO create cargo
        BigInteger holdId = BigInteger.ONE;
        
        // When
        //holdDao.addCargo(cargoId, holdId);
        // Then ?
    }
    
    @Test
    @Ignore
    @Rollback(true)
    public void testGetOccupiedVolume() {
        // Given
        //TODO create cargo
        //TODO create ship
        BigInteger id = holdDao.createHold();
        
        // When
        //holdDao.addCargo(cargoId, holdId);
        //holdDao.addCargo(cargoId, holdId);
        //holdDao.getOccupiedVolume(shipId);
        // Then ?
    }
    
    @Test
    @Rollback(true)
    public void testGetOccupiedVolumeInvalidShipId() {
        // Given
        BigInteger invalidShipId = BigInteger.ONE;
        // When
        holdDao.getOccupiedVolume(invalidShipId);
        // Then ?
    }
    
    @Test
    @Ignore
    @Rollback(true)
    public void testFindHold() {
        // Given
        //TODO create ship
        BigInteger id = holdDao.createHold();
        // When
        //holdDao.findHold(shipId)
        // Then ?
    }

    @Rollback(true)
    @Test(expected = IllegalArgumentException.class)
    public void testFindHoldByInvalidShipId() {
        // Given
        //TODO create ship
        BigInteger invalidShipId = BigInteger.ONE;
        // When
        holdDao.findHold(invalidShipId);
        // Then ?
    }
}