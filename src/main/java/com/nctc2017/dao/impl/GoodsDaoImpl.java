package com.nctc2017.dao.impl;

import com.nctc2017.bean.Goods;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.constants.Query;
import com.nctc2017.dao.GoodsDao;
import com.nctc2017.dao.utils.JdbcConverter;
import com.nctc2017.dao.utils.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsDaoImpl implements GoodsDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger createNewGoods(BigInteger goodsTemplateId, int quantity, int price) {
        String nextIdQuery = "SELECT obj_sq.NEXTVAL FROM dual";
        BigInteger newObjId = jdbcTemplate.queryForObject(nextIdQuery, BigDecimal.class).toBigInteger();

        PreparedStatementCreator psc = QueryBuilder.insert(DatabaseObject.GOODS_OBJTYPE_ID)
                .setSourceObjId(goodsTemplateId)
                .setAttribute(DatabaseAttribute.GOODS_QUANTITY, quantity + "")
                .setAttribute(DatabaseAttribute.GOODS_PURCHASE_PRICE, price + "")
                .build();
        jdbcTemplate.update(psc);
        return newObjId;
    }

    @Override
    public void increaseGoodsQuantity(BigInteger goodsId, int quantity) {
        Integer curQuantity = jdbcTemplate.queryForObject(Query.GET_ATTR_VALUE,
                new Object[]{JdbcConverter.toNumber(goodsId), JdbcConverter.toNumber(DatabaseAttribute.GOODS_QUANTITY)},
                Integer.class);
        if (curQuantity == null) return;
        curQuantity = curQuantity + quantity;
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(goodsId)
                .setAttribute(DatabaseAttribute.GOODS_QUANTITY, curQuantity + "")
                .build();
        jdbcTemplate.update(psc);
    }

    @Override
    public boolean decreaseGoodsQuantity(BigInteger goodsId, int quantity) {
        Integer curQuantity = jdbcTemplate.queryForObject(Query.GET_ATTR_VALUE,
                new Object[]{JdbcConverter.toNumber(goodsId), JdbcConverter.toNumber(DatabaseAttribute.GOODS_QUANTITY)},
                Integer.class);
        if (curQuantity == null)
            return false;

        curQuantity = curQuantity - quantity;

        if (curQuantity < 0)
            return false;

        PreparedStatementCreator psc;
        if (curQuantity == 0) {
            psc = QueryBuilder.delete(goodsId)
                    .build();
        } else {
            psc = QueryBuilder.updateAttributeValue(goodsId)
                    .setAttribute(DatabaseAttribute.GOODS_QUANTITY, curQuantity + "")
                    .build();
        }
        jdbcTemplate.update(psc);
        return true;
    }

    /**
     * Get goods rarity coefficient for specified goods template
     *
     * @param goodsTemplateObjectId
     * @return goods rarity coefficient if query was successful
     * - 1 otherwise
     */
    @Override
    public int getGoodsRaraty(BigInteger goodsTemplateObjectId) {
        Integer coef = jdbcTemplate.queryForObject(Query.GET_ATTR_VALUE,
                new Object[]{JdbcConverter.toNumber(goodsTemplateObjectId),
                        JdbcConverter.toNumber(DatabaseAttribute.TEMPLATE_GOODS_RARITY_COEF)},
                Integer.class);
        if (coef == null)
            return -1;
        else
            return coef;
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
        Object [] queryParams = new Object[] {JdbcConverter.toNumber(DatabaseObject.GOODS_OBJTYPE_ID),
                JdbcConverter.toNumber(containerId),
                JdbcConverter.toNumber(DatabaseObject.GOODS_OBJTYPE_ID),
                JdbcConverter.toNumber(containerId)};

        List<Goods> goodsList = jdbcTemplate.query(
                Query.GET_ENTITIES_FROM_CONTAINER, queryParams, new GoodsListExtractor());
        return goodsList;
    }

    private class GoodsListExtractor implements ResultSetExtractor<List<Goods>> {

        @Override
        public List<Goods> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            Map<String, String> goodsFieldValueMap;
            Map<BigInteger, Map<String, String>> goodsMap = new HashMap<>();
            BigInteger goodsId;
            if (resultSet.next()) {
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

            return null;
        }
    }


}