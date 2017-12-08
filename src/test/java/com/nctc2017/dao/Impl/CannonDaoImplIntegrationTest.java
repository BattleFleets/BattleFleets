package com.nctc2017.dao.Impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
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
@ContextConfiguration(classes = {ApplicationConfig.class})
public class CannonDaoImplIntegrationTest {
	@Autowired
	CannonDao cannonDao;

	@Test
	@Transactional
	@Rollback(true)
	public void testKulevrinCreated() {
		// Given
		int kulevrinTemplateId = DatabaseObject.kulevrinTemplateId;
		//when
		BigDecimal id = cannonDao.createCannon(kulevrinTemplateId);
		//then
		assertNotNull(id);
		assertTrue(id.intValue()>0);
	}
	@Test
	@Transactional
	@Rollback(true)
	public void testMortarCreated() {
		// Given
		int mortarTemplateId = DatabaseObject.mortarTemplateId;
		//when
		BigDecimal id = cannonDao.createCannon(mortarTemplateId);
		//then
		assertNotNull(id);
		assertTrue(id.intValue()>0);
	}
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
		int mortarTemplateId = DatabaseObject.bombardTemplateId;
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
		int kulevrinTemplateId = DatabaseObject.kulevrinTemplateId;
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
		int mortarTemplateId = DatabaseObject.mortarTemplateId;
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
		int mortarTemplateId = DatabaseObject.bombardTemplateId;
		//when
		BigDecimal id = cannonDao.createCannon(mortarTemplateId);
		Cannon cannon = cannonDao.findById(id.intValue());
		//then
		assertEquals(id.intValue(), cannon.getId());
		assertTrue(cannon.getCost()>0);
		assertTrue(cannon.getDamage()>0);
		assertTrue(cannon.getDistance()>0);
		assertEquals("Bombard", cannon.getName());
	}
}
