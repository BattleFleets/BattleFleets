package com.nctc2017.dao;

import com.nctc2017.bean.GoodsForSale;

import java.math.BigInteger;
import java.util.List;

public interface GoodsForSaleDao {

    List<GoodsForSale> findAllByTypeId(BigInteger templateTypeId, GoodsForSale.GoodsType type);

    List<GoodsForSale> findAll();
}
