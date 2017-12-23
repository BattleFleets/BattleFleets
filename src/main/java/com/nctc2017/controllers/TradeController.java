package com.nctc2017.controllers;

import java.util.*;

import com.nctc2017.bean.Thing;
import org.springframework.security.access.annotation.Secured;

public class TradeController {


    @Secured("ROLE_USER")
    public void buy(int id, int idHash, int goodsTemplateId, int quantity, int price) {
        // TODO implement here
    }

    public void sale(int id, int idHash, int goodsTemplateId, int buyingCost, int quantity, int price) {
        // TODO implement here
    }

    public List<Thing> getAllGoodsForBuying(int cityId) {
        // TODO implement here
        return null;
    }

    public List<Thing> getAllGoodsForSelling(int playerId) {
        // TODO implement here
        return null;
    }

}