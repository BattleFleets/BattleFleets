package com.nctc2017.dao.impl;

import com.nctc2017.bean.Goods;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.GoodsDao;
import com.nctc2017.dao.utils.JdbcConverter;
import com.nctc2017.dao.utils.QueryBuilder;
import com.nctc2017.dao.utils.QueryExecutor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Qualifier("goodsDao")
public class GoodsDaoImpl implements GoodsDao {

    private static final Logger logger = Logger.getLogger(GoodsDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QueryExecutor queryExecutor;

    @Override
    public BigInteger createNewGoods(BigInteger goodsTemplateId, int quantity, int price) {
        BigInteger newObjId = jdbcTemplate.queryForObject(Query.GET_NEXTVAL, BigDecimal.class).toBigInteger();

        PreparedStatementCreator psc = QueryBuilder.insert(DatabaseObject.GOODS_OBJTYPE_ID, newObjId)
                .setSourceObjId(goodsTemplateId)
                .setAttribute(DatabaseAttribute.GOODS_QUANTITY, String.valueOf(quantity))
                .setAttribute(DatabaseAttribute.GOODS_PURCHASE_PRICE, String.valueOf(price))
                .build();
        jdbcTemplate.update(psc);
        return newObjId;
    }

    @Override
    public Goods findById(@NotNull BigInteger goodsId) {
        Goods goods = queryExecutor.findEntity(goodsId,
                DatabaseObject.GOODS_OBJTYPE_ID,
                new GoodsExtractor(goodsId));
        if (goods == null) {
            RuntimeException e = new IllegalArgumentException("Wrong goods object id = " + goodsId);
            logger.log(Level.ERROR, "GoodsDao Exception while find by id ", e);
            throw e;
        }
        return goods;
    }

    @Override
    public void increaseGoodsQuantity(@NotNull BigInteger goodsId, int quantity) {
        Integer curQuantity = jdbcTemplate.queryForObject(Query.GET_ATTR_VALUE,
                new Object[]{JdbcConverter.toNumber(goodsId), JdbcConverter.toNumber(DatabaseAttribute.GOODS_QUANTITY)},
                Integer.class);
        if (curQuantity == null) {
            logger.log(Level.ERROR, "There is no quantity value for object with id = " + goodsId);
            return;
        }
        curQuantity = curQuantity + quantity;
        updateGoodsQuantity(goodsId, curQuantity);
    }

    @Override
    public void decreaseGoodsQuantity(@NotNull BigInteger goodsId, int quantity) {
        Integer curQuantity = jdbcTemplate.queryForObject(Query.GET_ATTR_VALUE,
                new Object[]{JdbcConverter.toNumber(goodsId), JdbcConverter.toNumber(DatabaseAttribute.GOODS_QUANTITY)},
                Integer.class);
        if (curQuantity == null) {
            logger.log(Level.ERROR, "There is no quantity value for object with id = " + goodsId);
            return;
        }
        curQuantity = curQuantity - quantity;
        updateGoodsQuantity(goodsId, curQuantity);
    }


    /**
     * set quantity value = quantity
     *
     * @param goodsId  goods which quantity is updating
     * @param quantity new quantity value to be set
     */
    @Override
    public void updateGoodsQuantity(@NotNull BigInteger goodsId, int quantity) {
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(goodsId)
                .setAttribute(DatabaseAttribute.GOODS_QUANTITY, String.valueOf(quantity))
                .build();
        jdbcTemplate.update(psc);
    }

    @Override
    public void deleteGoods(@NotNull BigInteger goodsId) {
        PreparedStatementCreator psc = QueryBuilder.delete(goodsId)
                .build();
        jdbcTemplate.update(psc);
    }

    /**
     * Get goods rarity coefficient for specified goods template
     *
     * @param goodsTemplateObjectId
     * @return goods rarity coefficient if query was successful
     * - 1 otherwise
     */
    @Override
    public int getGoodsRarity(@NotNull BigInteger goodsTemplateObjectId) {
        Integer coef = jdbcTemplate.queryForObject(Query.GET_ATTR_VALUE,
                new Object[]{JdbcConverter.toNumber(goodsTemplateObjectId),
                        JdbcConverter.toNumber(DatabaseAttribute.TEMPLATE_GOODS_RARITY_COEF)},
                Integer.class);
        if (coef == null) {
            logger.log(Level.ERROR, "There is no rarity value for object with id = " + goodsTemplateObjectId);
            return -1;
        } else
            return coef;
    }

    @Override
    public int getGoodsQuantity(@NotNull BigInteger goodsId) {
        Integer quantity = jdbcTemplate.queryForObject(Query.GET_ATTR_VALUE,
                new Object[]{JdbcConverter.toNumber(goodsId),
                        JdbcConverter.toNumber(DatabaseAttribute.GOODS_QUANTITY)},
                Integer.class);
        if (quantity == null) {
            logger.log(Level.ERROR, "There is no quantity value for object with id = " + quantity);
            return -1;
        } else
            return quantity;
    }


    @Override
    public List<Goods> getAllGoodsFromStock(BigInteger stockId) {
        return getAllGoods(stockId);
    }

    @Override
    public List<Goods> getAllGoodsFromHold(BigInteger holdId) {
        return getAllGoods(holdId);
    }

    private List<Goods> getAllGoods(BigInteger containerId) {
        Object[] queryParams = new Object[]{JdbcConverter.toNumber(DatabaseObject.GOODS_OBJTYPE_ID),
                JdbcConverter.toNumber(containerId),
                JdbcConverter.toNumber(DatabaseObject.GOODS_OBJTYPE_ID),
                JdbcConverter.toNumber(containerId)};

        List<Goods> goodsList = jdbcTemplate.query(
                Query.GET_ENTITIES_FROM_CONTAINER, queryParams, new GoodsListExtractor());
        return goodsList;
    }

    private final class GoodsExtractor implements ResultSetExtractor<Goods> {

        private BigInteger goodsId;

        GoodsExtractor(BigInteger goodsId) {
            this.goodsId = goodsId;
        }

        @Override
        public Goods extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            if (!resultSet.isBeforeFirst()) return null;

            Map<String, String> goodsFieldValueMap = new HashMap<>(4);
            while (resultSet.next()) {
                goodsFieldValueMap.put(resultSet.getString(1), resultSet.getString(2));
            }

            return new Goods(goodsId,
                    goodsFieldValueMap.remove(Goods.NAME),
                    Integer.valueOf(goodsFieldValueMap.remove(Goods.QUANTITY)),
                    Integer.valueOf(goodsFieldValueMap.remove(Goods.PRICE)),
                    Integer.valueOf(goodsFieldValueMap.remove(Goods.RARITY)));
        }
    }

    private class GoodsListExtractor implements ResultSetExtractor<List<Goods>> {

        @Override
        public List<Goods> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Map<String, String> goodsFieldValueMap;
            Map<BigInteger, Map<String, String>> goodsMap = new HashMap<>();
            BigInteger goodsId;
            while (resultSet.next()) {
                goodsId = resultSet.getBigDecimal(1).toBigInteger();
                goodsFieldValueMap = goodsMap.get(goodsId);
                if (goodsFieldValueMap == null) {
                    goodsFieldValueMap = new HashMap<>(4);
                    goodsMap.put(goodsId, goodsFieldValueMap);
                }
                goodsFieldValueMap.put(resultSet.getString(2), resultSet.getString(3));
            }

            List<Goods> goods = new ArrayList<>(goodsMap.size());
            Goods newGoods;
            Map<String, String> fieldValueMap;
            for (Map.Entry<BigInteger, Map<String, String>> entry : goodsMap.entrySet()) {
                fieldValueMap = entry.getValue();
                newGoods = new Goods(entry.getKey(),
                        fieldValueMap.remove(Goods.NAME),
                        Integer.valueOf(fieldValueMap.remove(Goods.QUANTITY)),
                        Integer.valueOf(fieldValueMap.remove(Goods.PRICE)),
                        Integer.valueOf(fieldValueMap.remove(Goods.RARITY)));
                goods.add(newGoods);
            }
            return goods;
        }
    }


}