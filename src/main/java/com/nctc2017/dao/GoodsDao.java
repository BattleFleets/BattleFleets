package com.nctc2017.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.nctc2017.bean.Goods;

public interface GoodsDao {

	BigInteger createNewGoods(BigInteger goodsTemplateId, int quantity, int price);

	void increaseGoodsQuantity(BigInteger goodsId, int quantity);

	boolean decreaseGoodsQuantity(BigInteger goodsId, int quantity);

	int getGoodsRaraty(BigInteger goodsTemplateId);

	List<Goods> getAllGoodsFromStock(BigInteger stockId);

	List<Goods> getAllGoodsFromHold(BigInteger holdId);

}