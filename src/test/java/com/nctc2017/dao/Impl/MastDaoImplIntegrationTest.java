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

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@Transactional
public class MastDaoImplIntegrationTest {

    @Autowired
    MastDao mastDao;

    private static BigInteger TEST_MASt_OBJECT_ID = BigInteger.valueOf(32);
    private static BigInteger TEST_MASt_OBJECT_ID_INCORRECT = BigInteger.valueOf(12);
    private static BigInteger Exc = new BigInteger("9223372036854775809");

    @Test
    @Rollback(true)
    public void testDaoFinding() {
        //when
        //BigInteger m = mastDao.createNewMast(DatabaseObject.MAST_TEMPLATE_OBJTYPE_ID,new BigInteger("27"));
        Mast mast = mastDao.findMast(TEST_MASt_OBJECT_ID);
        //then
        assertTrue(mast.getCost() > 0);
        assertTrue(mast.getCurSpeed() > 0);
        assertTrue(mast.getMaxSpeed() > 0);
        assertEquals("T_Mast1", mast.getTemplateName());
    }
   /* @Test
    @Rollback(true)
    public void testDaoCreating() {
        // Given
        int createdId = mastDao.createNewMast(6, 27);
        //when
        Mast mast = mastDao.findMast(createdId);
        //then
        assertTrue(mast.getCost() > 0);
        assertTrue(mast.getCurSpeed() > 0);
        assertTrue(mast.getMaxSpeed() > 0);
        assertEquals("T_Mast1", mast.getTemplateName());
    }*/
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
        mastDao.deleteMast(Exc);
    }

}
