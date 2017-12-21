package com.nctc2017.dao.Impl;


import com.nctc2017.bean.Mast;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.MastDao;
import org.junit.Ignore;
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
public class MastDaoImplIntegrationTest {

    @Autowired
    MastDao mastDao;

    /*These constants is used until problem with BigInteger is solved*/
    private static final BigInteger TEST_MASt_OBJECT_ID = BigInteger.valueOf(32);
    private static final BigInteger TEST_MASt_OBJECT_ID_INCORRECT = BigInteger.valueOf(12);
    private static final BigInteger EXC_VALUE = new BigInteger("9223372036854775809");

    @Test
    @Ignore
    @Rollback(true)
    public void testDaoFinding() {
        // given
        BigInteger id = mastDao.createNewMast(DatabaseObject.MAST1_TEMPLATE_OBJECT_ID, null);
        //when TODO
        Mast mast = mastDao.findMast(id);
        //then
        assertTrue(mast.getCost() > 0);
        assertTrue(mast.getCurSpeed() > 0);
        assertTrue(mast.getMaxSpeed() > 0);
        assertEquals("T_Mast1", mast.getTemplateName());
    }

    @Test
    @Rollback(true)
    public void testDaoDeleting() {
        mastDao.deleteMast(TEST_MASt_OBJECT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void testDaoDeletingIllegalArgument() {
        mastDao.deleteMast(TEST_MASt_OBJECT_ID_INCORRECT);
    }

    @Test(expected = ArithmeticException.class)
    @Rollback(true)
    public void testDaoDeletingArithmeticalException() {
        mastDao.deleteMast(EXC_VALUE);
    }

    /*@Test
    @Rollback(true)
    public void testDaoCreating() {
        // Given
        BigInteger createdId = mastDao.createNewMast(DatabaseObject.MAST_TEMPLATE_OBJTYPE_ID,
                shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID));
        //when
        Mast mast = mastDao.findMast(createdId);
        //then
        assertTrue(mast.getCost() > 0);
        assertTrue(mast.getCurSpeed() > 0);
        assertTrue(mast.getMaxSpeed() > 0);
        assertEquals("T_Mast1", mast.getTemplateName());
    }

    @Test
    @Rollback(true)
    public void updateMastSpeed() {
        mastDao.updateCurMastSpeed(TEST_MASt_OBJECT_ID,4);
        assertEquals(mastDao.findMast(TEST_MASt_OBJECT_ID).getCurSpeed(),4);
    }*/

}
