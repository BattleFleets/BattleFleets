package com.nctc2017.dao.impl;

import java.util.*;

import com.nctc2017.bean.Goods;
import com.nctc2017.dao.GoodsDao;

public class GoodsDaoImpl implements GoodsDao {

    @Override
	public int createNewGoods(int goodsTemplateId, int quantity, int price) {
        // TODO implement here
        return 0;
    }

    @Override
	public void increaseGoodsQuantity(int goodsId, int quantity) {
        // TODO implement here
    }

    @Override
	public boolean decreaseGoods(int goodsId, int quantity) {
        // TODO implement here
        return false;
    }

    @Override
	public int getGoodsRaraty(int goodsTemlateId) {
        // TODO implement here
        return 0;
    }

    @Override
	public List<Goods> getAllGoodsFromStock(int stockId) {
        // TODO implement here
        return null;
    }

    @Override
	public List<Goods> getAllGoodsFromHold(int holdId) {
        // TODO implement here
        return null;
    }

}