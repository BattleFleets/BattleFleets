package com.nctc2017.controllers;

import java.math.BigInteger;
import java.util.*;

import com.nctc2017.bean.Thing;
import com.nctc2017.services.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TradeController {

    @Autowired
    MoneyService moneyService;

    @Secured("ROLE_USER")
    @RequestMapping(value = "/market", method = RequestMethod.GET)
    public ModelAndView marketWelcome(
            @RequestParam(value = "market", required = false) String city) {
        ModelAndView model = new ModelAndView();
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("city", city);
        model.setViewName("MarketView");
        return model;
    }

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

    public String getMoney(BigInteger playerId){
        int money;
        money = moneyService.getPlayersMoney(playerId);
        String s="sdkgjh";
        return s;
    }

}