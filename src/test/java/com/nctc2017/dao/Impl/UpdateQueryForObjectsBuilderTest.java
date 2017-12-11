package com.nctc2017.dao.Impl;

import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.impl.UpdateQueryForObjectsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@Transactional
public class UpdateQueryForObjectsBuilderTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Rollback(true)
    public void testInsertOnlyObject() {
        PreparedStatementCreator actualPreparedStmt = UpdateQueryForObjectsBuilder.newInsertBuilder(BigInteger.valueOf(DatabaseObject.CANNON_OBJTYPE_ID))
                .setSourceObjId(BigInteger.valueOf(DatabaseObject.BOMBARD_TEMPLATE_ID))
                .build();
        int expectedUpdatedRowsCount = 1;
        assertTrue(jdbcTemplate.update(actualPreparedStmt) == expectedUpdatedRowsCount);
    }

    @Test
    @Rollback(true)
    public void insertObjectWithAttributesValues() {
        PreparedStatementCreator actualPreparedStmt = UpdateQueryForObjectsBuilder.newInsertBuilder(DatabaseObject.GOODS_OBJTYPE_ID)
                .setSourceObjId(DatabaseObject.WOOD_TEMPLATE_ID)
                .setAttribute(DatabaseAttribute.GOODS_PURCHASE_PRICE, "40")
                .setAttribute(DatabaseAttribute.GOODS_QUANTITY, "350")
                .build();
        int expectedUpdatedRowsCount = 3;
        assertTrue(jdbcTemplate.update(actualPreparedStmt) == expectedUpdatedRowsCount);
    }

    @Test
    @Rollback(true)
    public void testUpdateParentId() {
        PreparedStatementCreator actualPreparedStmt = UpdateQueryForObjectsBuilder.newUpdateParentIdBuilder(new BigInteger("59"), new BigInteger("46")).build();
        int expectedUpdatedRowsCount = 1;
        assertTrue(jdbcTemplate.update(actualPreparedStmt) == expectedUpdatedRowsCount);
    }

    @Test
    @Rollback(true)
    public void testUpdateAttrValues() {
        PreparedStatementCreator actualPreparedStmt = UpdateQueryForObjectsBuilder.newUpdateAttributeValueBuilder(new BigInteger("59"))
                .setAttribute(DatabaseAttribute.GOODS_PURCHASE_PRICE, "40")
                .setAttribute(DatabaseAttribute.GOODS_QUANTITY, "350")
                .setAttribute(DatabaseAttribute.GOODS_PURCHASE_PRICE, "50")
                .build();
        int expectedUpdatedRowsCount = 1; //because of begin-end query
        assertTrue(jdbcTemplate.update(actualPreparedStmt) == expectedUpdatedRowsCount);
    }

    @Test
    @Rollback(true)
    public void testDeleteObject() {
        PreparedStatementCreator actualPreparedStmt = UpdateQueryForObjectsBuilder.newDeleteBuilder(new BigInteger("59"))
                .build();
        int expectedUpdatedRowsCount = 1;
        assertTrue(jdbcTemplate.update(actualPreparedStmt) == expectedUpdatedRowsCount);
    }

}
