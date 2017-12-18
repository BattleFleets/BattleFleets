package com.nctc2017.dao.impl;

import java.math.BigInteger;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.nctc2017.bean.Ammo;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.AmmoDao;
import com.nctc2017.dao.extractors.EntityExtractor;
import com.nctc2017.dao.extractors.EntityListExtractor;
import com.nctc2017.dao.extractors.ExtractingVisitor;
import com.nctc2017.dao.utils.QueryBuilder;
import com.nctc2017.dao.utils.QueryExecutor;

public class AmmoDaoImpl implements AmmoDao {

    @Autowired
    private QueryExecutor queryExecutor;

    @Override
    public Ammo findById(BigInteger ammoId) {
        Ammo ammo = queryExecutor.findEntity(ammoId, 
                DatabaseObject.AMMO_OBJTYPE_ID, 
                new EntityExtractor<>(ammoId, new AmmoVisitor()));
        return ammo;
    }

    @Override
    public String getAmmoName(BigInteger ammoId) {
        return queryExecutor.getAttrValue(ammoId, DatabaseAttribute.AMMO_NAME, String.class);
    }

    @Override
    public String getAmmoDamageType(BigInteger ammoId) {
        return queryExecutor.getAttrValue(ammoId, DatabaseAttribute.AMMO_DAMAGE_TYPE, String.class);
    }

    @Override
    public int getAmmoCost(BigInteger ammoId) {
        return queryExecutor.getAttrValue(ammoId, DatabaseAttribute.AMMO_COST, Integer.class);
    }

    @Override
    public int getAmmoQuantity(BigInteger ammoId) {
        return queryExecutor.getAttrValue(ammoId, DatabaseAttribute.AMMO_NUM, Integer.class);
    }

    @Override
    public boolean increaseAmmoQuantity(BigInteger ammoId, int increaseNumber) {
        Integer curQuantity = queryExecutor.getAttrValue(ammoId, 
                DatabaseAttribute.AMMO_NUM, 
                Integer.class);
        QueryBuilder builder = QueryBuilder.updateAttributeValue(ammoId)
                .setAttribute(DatabaseAttribute.AMMO_NUM, curQuantity + increaseNumber);
        int res = queryExecutor.updateAttribute(builder);
        return res == 1;
    }

    @Override
    public boolean decreaseAmmoQuantity(BigInteger ammoId, int decreaseNumber) {
        return increaseAmmoQuantity(ammoId, -decreaseNumber);
    }

    @Override
    public BigInteger createAmmo(BigInteger ammoTemplateId, int quantity) {
        QueryBuilder builder = QueryBuilder.insert(DatabaseObject.AMMO_OBJTYPE_ID)
        .setSourceObjId(ammoTemplateId)
        .setAttribute(DatabaseAttribute.AMMO_NUM, quantity);
        
        BigInteger newObjId = queryExecutor.createNewEntity(builder);
        return newObjId;
    }

    @Override
    public List<Ammo> getAllAmmoFromStock(BigInteger idStock) {
        return getAllAmmoFromAnywear(idStock);
    }

    @Override
    public List<Ammo> getAllAmmoFromHold(BigInteger idHold) {
        return getAllAmmoFromAnywear(idHold);
    }
    
    private List<Ammo> getAllAmmoFromAnywear(BigInteger containerId) {
        List<Ammo> ammoList = queryExecutor
                .getEntitiesFromContainer(containerId, 
                        DatabaseObject.AMMO_OBJTYPE_ID, 
                        new EntityListExtractor<>(new AmmoVisitor()));
        return ammoList;
    }
    
    private final class AmmoVisitor implements ExtractingVisitor<Ammo> {

        @Override
        public Ammo visit(BigInteger entityId, Map<String, String> papamMap) {
            return new Ammo(entityId,
                    papamMap.get(Ammo.NAME),
                    papamMap.get(Ammo.TYPE),
                    Integer.valueOf(papamMap.get(Ammo.NUM)), 
                    Integer.valueOf(papamMap.get(Ammo.COST)));
        }
        
    }

}