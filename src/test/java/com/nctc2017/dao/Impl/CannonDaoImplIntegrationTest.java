package com.nctc2017.dao.Impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Cannon;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.CannonDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@Transactional
public class CannonDaoImplIntegrationTest {
    @Autowired
    CannonDao cannonDao;

    @Test
    @Rollback(true)
    public void testKulevrinCreate() {
        // Given
        BigInteger kulevrinTemplateId = BigInteger.valueOf(DatabaseObject.KULEVRIN_TEMPLATE_ID);
        // when
        BigInteger id = cannonDao.createCannon(kulevrinTemplateId);
        // then
        assertNotNull(id);
        assertTrue(id.intValue() > 0);
    }

    @Test
    @Rollback(true)
    public void testMortarCreate() {
        // Given
        BigInteger mortarTemplateId = BigInteger.valueOf(DatabaseObject.MORTAR_TEMPLATE_ID);
        // when
        BigInteger id = cannonDao.createCannon(mortarTemplateId);
        // then
        assertNotNull(id);
        assertTrue(id.intValue() > 0);
    }

    @Rollback(true)
    @Test(expected = IllegalArgumentException.class)
    public void testCannonCreateFail() {
        // Given
        BigInteger wrongTemplateId = BigInteger.ONE;
        // when
        cannonDao.createCannon(wrongTemplateId);
    }

    @Test
    @Rollback(true)
    public void testBombardCreate() {
        // Given
        BigInteger bombardTemplateId = BigInteger.valueOf(DatabaseObject.BOMBARD_TEMPLATE_ID);
        // when
        BigInteger id = cannonDao.createCannon(bombardTemplateId);
        // then
        assertNotNull(id);
        assertTrue(id.intValue() > 0);
    }

    @Test
    @Rollback(true)
    public void testKulevrinFind() {
        // Given
        BigInteger kulevrinTemplateId = BigInteger.valueOf(DatabaseObject.KULEVRIN_TEMPLATE_ID);
        // when
        BigInteger id = cannonDao.createCannon(kulevrinTemplateId);
        Cannon cannon = cannonDao.findById(id);
        // then
        assertEquals(id, cannon.getThingId());
        assertTrue(cannon.getCost() > 0);
        assertTrue(cannon.getDamage() > 0);
        assertTrue(cannon.getDistance() > 0);
        assertEquals("Kulevrin", cannon.getName());
    }

    @Test
    @Rollback(true)
    public void testMortarFind() {
        // Given
        BigInteger mortarTemplateId = BigInteger.valueOf(DatabaseObject.MORTAR_TEMPLATE_ID);
        // when
        BigInteger id = cannonDao.createCannon(mortarTemplateId);
        Cannon cannon = cannonDao.findById(id);
        // then
        assertEquals(id, cannon.getThingId());
        assertTrue(cannon.getCost() > 0);
        assertTrue(cannon.getDamage() > 0);
        assertTrue(cannon.getDistance() > 0);
        assertEquals("Mortar", cannon.getName());
    }

    @Test
    @Rollback(true)
    public void testBombardFind() {
        // Given
        BigInteger bombardTemplateId = BigInteger.valueOf(DatabaseObject.BOMBARD_TEMPLATE_ID);
        // when
        BigInteger id = cannonDao.createCannon(bombardTemplateId);
        Cannon cannon = cannonDao.findById(id);
        // then
        assertEquals(id, cannon.getThingId());
        assertTrue(cannon.getCost() > 0);
        assertTrue(cannon.getDamage() > 0);
        assertTrue(cannon.getDistance() > 0);
        assertEquals("Bombard", cannon.getName());
    }

    @Test
    @Rollback(true)
    public void testCannonDelete() {
        // Given
        BigInteger bombardTemplateId = BigInteger.valueOf(DatabaseObject.BOMBARD_TEMPLATE_ID);
        // when
        BigInteger id = cannonDao.createCannon(bombardTemplateId);
        cannonDao.deleteCannon(id);
        // then ok
    }

    @Test
    @Rollback(true)
    public void testCannonDeleteFail() {
        // Given
        BigInteger id = new BigInteger("1");
        // when
        cannonDao.deleteCannon(id);
        // then ok
    }

    @Rollback(true)
    @Test(expected = IllegalArgumentException.class)
    public void testCannonFindFail() {
        // Given
        BigInteger wrongCannonId = new BigInteger("1");
        // when
        cannonDao.findById(wrongCannonId);
        // then
    }
}
