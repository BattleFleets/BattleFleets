package com.nctc2017.dao;

import java.util.List;

import com.nctc2017.bean.Goods;

public interface GoodsDao {

	int createNewGoods(int goodsTemplateId, int quantity, int price);

	void increaseGoodsQuantity(int goodsId, int quantity);

	boolean decreaseGoods(int goodsId, int quantity);

	int getGoodsRaraty(int goodsTemlateId);

	List<Goods> getAllGoodsFromStock(int stockId);

	List<Goods> getAllGoodsFromHold(int holdId);

}