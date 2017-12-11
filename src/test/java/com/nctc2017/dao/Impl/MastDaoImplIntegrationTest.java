package com.nctc2017.dao.Impl;

import com.nctc2017.bean.Cannon;
import com.nctc2017.bean.Mast;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.MastDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@Transactional
public class MastDaoImplIntegrationTest {

    @Autowired
    MastDao mastDao;

    @Test
    @Rollback(true)
    public void testDaoFinding() {
        // Given
        int createdId = mastDao.createNewMast(6, 27);
        //when
        Mast mast = mastDao.findMast(createdId);
        //then
        assertTrue(mast.getCost() > 0);
        assertTrue(mast.getCurSpeed() > 0);
        assertTrue(mast.getMaxSpeed() > 0);
        assertEquals("T_Mast1", mast.getTemplateName());
    }
    /*@Test
    @Rollback(true)
    public void deleteDaoMast() {
        // Given
        int mastId = 32;
        mastDao.deleteMast(mastId);
        //then

    }*/
    /*
    @Test
    @Transactional
    @Rollback(true)
    public void testCannonCreatedFail() {
        // Given
        int mortarTemplateId = 1;
        //when
        BigDecimal id = cannonDao.createCannon(mortarTemplateId);
        //then
        assertNull(id);
    }
    @Test
    @Transactional
    @Rollback(true)
    public void testBombardCreated() {
        // Given
        int mortarTemplateId = DatabaseObject.BOMBARD_TEMPLATE_ID;
        //when
        BigDecimal id = cannonDao.createCannon(mortarTemplateId);
        //then
        assertNotNull(id);
        assertTrue(id.intValue()>0);
    }
    @Test
    @Transactional
    @Rollback(true)
    public void testKulevrinFind() {
        // Given
        int kulevrinTemplateId = DatabaseObject.KULEVRIN_TEMPLATE_ID;
        //when
        BigDecimal id = cannonDao.createCannon(kulevrinTemplateId);
        Cannon cannon = cannonDao.findById(id.intValue());
        //then
        assertEquals(id.intValue(), cannon.getId());
        assertTrue(cannon.getCost()>0);
        assertTrue(cannon.getDamage()>0);
        assertTrue(cannon.getDistance()>0);
        assertEquals("Kulevrin", cannon.getName());
    }
    @Test
    @Transactional
    @Rollback(true)
    public void testMortarFind() {
        // Given
        int mortarTemplateId = DatabaseObject.MORTAR_TEMPLATE_ID;
        //when
        BigDecimal id = cannonDao.createCannon(mortarTemplateId);
        Cannon cannon = cannonDao.findById(id.intValue());
        //then
        assertEquals(id.intValue(), cannon.getId());
        assertTrue(cannon.getCost()>0);
        assertTrue(cannon.getDamage()>0);
        assertTrue(cannon.getDistance()>0);
        assertEquals("Mortar", cannon.getName());
    }
    @Test
    @Transactional
    @Rollback(true)
    public void testBombardFind() {
        // Given
        int mortarTemplateId = DatabaseObject.BOMBARD_TEMPLATE_ID;
        //when
        BigDecimal id = cannonDao.createCannon(mortarTemplateId);
        Cannon cannon = cannonDao.findById(id.intValue());
        //then
        assertEquals(id.intValue(), cannon.getId());
        assertTrue(cannon.getCost() > 0);
        assertTrue(cannon.getDamage() > 0);
        assertTrue(cannon.getDistance() > 0);
        assertEquals("Bombard", cannon.getName());
    }*/
}
