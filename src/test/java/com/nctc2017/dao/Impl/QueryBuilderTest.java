package com.nctc2017.dao.Impl;

import com.nctc2017.bean.Cannon;
import com.nctc2017.bean.Goods;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.GoodsDao;
import com.nctc2017.dao.utils.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
@Transactional
public class QueryBuilderTest {

    @Autowired
    private CannonDao cannonDao;
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Rollback(true)
    public void testInsertOnlyObject() {
        BigInteger objectId = jdbcTemplate.queryForObject(Query.GET_NEXTVAL, BigDecimal.class).toBigInteger();
        Cannon expectedCannon = new Cannon(objectId, "Bombard", 2, 15, 1000);

        PreparedStatementCreator actualPreparedStmt = QueryBuilder.insert(DatabaseObject.CANNON_OBJTYPE_ID, objectId)
                .setSourceObjId(DatabaseObject.BOMBARD_TEMPLATE_ID)
                .build();
        jdbcTemplate.update(actualPreparedStmt);

        Cannon resultCannon = cannonDao.findById(objectId);

        assertEquals(expectedCannon.getThingId(), resultCannon.getThingId());
        assertEquals(expectedCannon.getCost(), resultCannon.getCost());
        assertEquals(expectedCannon.getDamage(), resultCannon.getDamage());
        assertEquals(expectedCannon.getDistance(), resultCannon.getDistance());
        assertEquals(expectedCannon.getName(), resultCannon.getName());
    }

    @Test
    @Rollback(true)
    public void insertObjectWithAttributesValues() {

        BigInteger objectId = jdbcTemplate.queryForObject(Query.GET_NEXTVAL, BigDecimal.class).toBigInteger();
        Goods expectedGoods = new Goods(objectId, "Wood", 350, 40, 1);

        PreparedStatementCreator actualPreparedStmt = QueryBuilder.insert(DatabaseObject.GOODS_OBJTYPE_ID, objectId)
                .setSourceObjId(DatabaseObject.WOOD_TEMPLATE_ID)
                .setAttribute(DatabaseAttribute.GOODS_PURCHASE_PRICE, "40")
                .setAttribute(DatabaseAttribute.GOODS_QUANTITY, "350")
                .build();

        jdbcTemplate.update(actualPreparedStmt);

        Goods resultGoods = goodsDao.findById(objectId);
        assertEquals(expectedGoods.getThingId(), resultGoods.getThingId());
        assertEquals(expectedGoods.getQuantity(), resultGoods.getQuantity());
        assertEquals(expectedGoods.getPurchasePrice(), resultGoods.getPurchasePrice());
        assertEquals(expectedGoods.getName(), resultGoods.getName());
        assertEquals(expectedGoods.getRarity(), resultGoods.getRarity());
    }

    @Test (expected = IllegalArgumentException.class)
    @Rollback(true)
    public void insertObjectWithNullObjectId() {
        PreparedStatementCreator actualPreparedStmt = QueryBuilder.insert(DatabaseObject.GOODS_OBJTYPE_ID, null)
                .build();

        jdbcTemplate.update(actualPreparedStmt);
    }

    @Test
    @Rollback(true)
    public void testUpdateParentId() {
        BigInteger objectId = new BigInteger("59");
        BigInteger parentId = new BigInteger("46");

        PreparedStatementCreator actualPreparedStmt = QueryBuilder.updateParent(objectId, parentId).build();
        jdbcTemplate.update(actualPreparedStmt);

        String parentIdQuery = "select parent_id from objects where object_id = 59";
        BigInteger resultParentId = jdbcTemplate.queryForObject(parentIdQuery, BigDecimal.class).toBigInteger();

        assertEquals(parentId, resultParentId);
    }

    @Test
    @Rollback(true)
    public void testUpdateParentIdToNull() {
        BigInteger objectId = new BigInteger("59");
        BigInteger parentId = null;

        PreparedStatementCreator actualPreparedStmt = QueryBuilder.updateParent(objectId, parentId).build();
        jdbcTemplate.update(actualPreparedStmt);

        String parentIdQuery = "select parent_id from objects where object_id = 59";
        BigDecimal idValue = jdbcTemplate.queryForObject(parentIdQuery, BigDecimal.class);
        BigInteger resultParentId = (idValue == null) ? null : idValue.toBigInteger();

        assertNull(resultParentId);
    }

    @Test
    @Rollback(true)
    public void testUpdateAttrValues() {
        BigInteger objectId = new BigInteger("59");
        Goods expectedGoods = new Goods(objectId, "Wood", 350, 50, 1);

        PreparedStatementCreator actualPreparedStmt = QueryBuilder.updateAttributeValue(objectId)
                .setAttribute(DatabaseAttribute.GOODS_PURCHASE_PRICE, "40")
                .setAttribute(DatabaseAttribute.GOODS_QUANTITY, "350")
                .setAttribute(DatabaseAttribute.GOODS_PURCHASE_PRICE, "50")
                .build();
        jdbcTemplate.update(actualPreparedStmt);

        Goods resultGoods = goodsDao.findById(objectId);
        assertEquals(expectedGoods.getThingId(), resultGoods.getThingId());
        assertEquals(expectedGoods.getQuantity(), resultGoods.getQuantity());
        assertEquals(expectedGoods.getPurchasePrice(), resultGoods.getPurchasePrice());
        assertEquals(expectedGoods.getName(), resultGoods.getName());
        assertEquals(expectedGoods.getRarity(), resultGoods.getRarity());
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void testDeleteObject() {
        BigInteger objectId = new BigInteger("59");

        PreparedStatementCreator actualPreparedStmt = QueryBuilder.delete(new BigInteger("59")).build();
        jdbcTemplate.update(actualPreparedStmt);

        Goods resultGoods = goodsDao.findById(objectId);
    }

}
