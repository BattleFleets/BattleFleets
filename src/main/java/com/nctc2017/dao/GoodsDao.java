package com.nctc2017.dao;

import java.util.*;

import com.nctc2017.bean.Goods;

/**
 * 
 */
public class GoodsDao {

    /**
     * Default constructor
     */
    public GoodsDao() {
    }



    /**
     * @param int goodsTemplateId 
     * @param int quantity 
     * @param int price 
     * @return
     */
    public int createNewGoods(int goodsTemplateId, int quantity, int price) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int goodsId 
     * @param int quantity 
     */
    public void increaseGoodsQuantity(int goodsId, int quantity) {
        // TODO implement here
    }

    /**
     * @param int goodsId 
     * @param int quantity 
     * @return
     */
    public boolean decreaseGoods(int goodsId, int quantity) {
        // TODO implement here
        return false;
    }

    /**
     * @param int goodsTemlateId 
     * @return
     */
    public int getGoodsRaraty(int goodsTemlateId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int stockId 
     * @return
     */
    public List<Goods> getAllGoodsFromStock(int stockId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int holdId 
     * @return
     */
    public List<Goods> getAllGoodsFromHold(int holdId) {
        // TODO implement here
        return null;
    }

}