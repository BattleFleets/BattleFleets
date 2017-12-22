package com.nctc2017.dao.Impl;


import com.nctc2017.bean.Ship;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseObject;
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


    @Test
    @Rollback(true)
    public void testShipFinding() {
        BigInteger i = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID,null);
        Ship s = shipDao.findShip(i);
        assertEquals(s.getTName(),"T_Caravela");
    }


}

