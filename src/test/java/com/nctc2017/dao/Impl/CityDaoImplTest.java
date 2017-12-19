package com.nctc2017.dao.Impl;

import com.nctc2017.bean.City;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.dao.CityDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@Transactional
public class CityDaoImplTest {
    @Autowired
    CityDao cityDao;

    @Test
    @Rollback(true)
    public void find() throws Exception {
        City city=cityDao.find(new BigInteger("69"));
        assertEquals(city.getCityName(),"Nassau");
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void findFailed() throws Exception {
        City city=cityDao.find(new BigInteger("83"));
    }

    @Test
    @Rollback(true)
    public void findAll() throws Exception {
        //TODO
        List<City> cities=cityDao.findAll();
        assertEquals(cities.get(0).getCityName(),"Nassau");
        assertEquals(cities.get(1).getCityName(),"Port Royal");

    }

}