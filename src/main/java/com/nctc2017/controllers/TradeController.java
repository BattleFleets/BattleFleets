package com.nctc2017.controllers;

import java.util.*;

import com.nctc2017.bean.Thing;

/**
 * 
 */
public class TradeController {

    /**
     * Default constructor
     */
    public TradeController() {
    }

    /**
     * @param int id 
     * @param int idHash 
     * @param int goodsTemplateId 
     * @param int quantity 
     * @param int price
     */
    public void buy(int id, int idHash, int goodsTemplateId, int quantity, int price) {
        // TODO implement here
    }

    /**
     * @param int id 
     * @param int idHash 
     * @param int goodsTemplateId 
     * @param int buyingCost 
     * @param int quantity 
     * @param int price
     */
    public void sale(int id, int idHash, int goodsTemplateId, int buyingCost, int quantity, int price) {
        // TODO implement here
    }

    /**
     * @param int cityId 
     * @return
     */
    public List<Thing> getAllGoodsForBuying(int cityId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int playerId 
     * @return
     */
    public List<Thing> getAllGoodsForSelling(int playerId) {
        // TODO implement here
        return null;
    }

}