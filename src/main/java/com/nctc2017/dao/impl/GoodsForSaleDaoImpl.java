package com.nctc2017.dao.impl;

import com.nctc2017.bean.GoodsForSale;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.GoodsForSaleDao;
import com.nctc2017.dao.extractors.EntityListExtractor;
import com.nctc2017.dao.extractors.ExtractingVisitor;
import com.nctc2017.dao.utils.QueryExecutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Qualifier("goodsForSaleDao")
public class GoodsForSaleDaoImpl implements GoodsForSaleDao {

    private static final Logger log = Logger.getLogger(GoodsForSaleDaoImpl.class);

    @Autowired
    private QueryExecutor queryExecutor;

    @Override
    public List<GoodsForSale> findAllByTypeId(BigInteger templateTypeId, GoodsForSale.GoodsType type) {
        List<GoodsForSale> goodsList = queryExecutor.getAllEntitiesByType(templateTypeId,
                new EntityListExtractor<ArrayList<GoodsForSale>, GoodsForSale>(new GoodsForSaleVisitor(type)));
        return goodsList;
    }

    @Override
    public List<GoodsForSale> findAll() {
        List<GoodsForSale>  goods = new ArrayList<>();

        goods.addAll(findAllByTypeId(
                DatabaseObject.CANNON_TEMPLATE_TYPE_ID,
                GoodsForSale.GoodsType.CANNON));

        goods.addAll(findAllByTypeId(
                DatabaseObject.MAST_TEMPLATE_OBJTYPE_ID,
                GoodsForSale.GoodsType.MAST));

        goods.addAll(findAllByTypeId(
                DatabaseObject.GOODS_TEMPLATE_TYPE_ID,
                GoodsForSale.GoodsType.GOODS));

        goods.addAll(findAllByTypeId(
                DatabaseObject.AMMO_TEMPLATE_TYPE_ID,
                GoodsForSale.GoodsType.AMMO));

        return goods;
    }

    private final class GoodsForSaleVisitor implements ExtractingVisitor<GoodsForSale> {

        private GoodsForSale.GoodsType type;

        public GoodsForSaleVisitor(GoodsForSale.GoodsType type) {
            this.type = type;
        }

        @Override
        public GoodsForSale visit(BigInteger entityId, Map<String, String> papamMap) {
            String name;
            String description;
            GoodsForSale goods;

            switch (type) {

                case GOODS:
                    name = papamMap.get("GoodsName");
                    description = "";
                    goods = new GoodsForSale(entityId, name, description, type);
                    goods.setGoodsRarity(Integer.valueOf(papamMap.get("RarityCoef")));
                    break;

                case AMMO:
                    name = papamMap.get("AmmoName");
                    description = "Damage type: " + papamMap.get("DamageType");
                    goods = new GoodsForSale(entityId, name, description, type);
                    goods.setBuyingPrice(Integer.valueOf(papamMap.get("AmmoCost")));
                    goods.setSalePrice(goods.getBuyingPrice()/2);
                    break;

                case MAST:
                    name = papamMap.get("MastName");
                    description = "Sailyards count: " + papamMap.get("Sailyards") +
                            ", speed: " + papamMap.get("Speed");
                    goods = new GoodsForSale(entityId, name, description, type);
                    goods.setBuyingPrice(Integer.valueOf(papamMap.get("MastCost")));
                    goods.setSalePrice(goods.getBuyingPrice()/2);
                    break;

                case CANNON:
                    name = papamMap.get("CannonName");
                    description = "Damage: " + papamMap.get("Damage") +
                            ", distance: " + papamMap.get("Distance");
                    goods = new GoodsForSale(entityId, name, description, type);
                    goods.setBuyingPrice(Integer.valueOf(papamMap.get("CannonCost")));
                    goods.setSalePrice(goods.getBuyingPrice()/2);
                    break;

                default:
                    RuntimeException e = new IllegalArgumentException("Wrong object type " + type.name());
                    log.error("GoodsForSaleDao Exception while choosing object type", e);
                    throw e;

            }
            return goods;
        }
    }
}
