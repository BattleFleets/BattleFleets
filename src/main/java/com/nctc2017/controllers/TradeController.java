package com.nctc2017.controllers;

import java.math.BigInteger;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nctc2017.bean.Market;
import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.bean.Thing;
import com.nctc2017.bean.View;
import com.nctc2017.services.MoneyService;
import com.nctc2017.services.TravelService;
import com.nctc2017.services.utils.MarketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TradeController {

    @Autowired
    MoneyService moneyService;

    @Autowired
    MarketManager marketManager;

    @Autowired
    private TravelService travelService;

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

    @RequestMapping(value = "/market/buy", method = RequestMethod.GET)
    @ResponseBody
    public String getAllGoodsForBuying(@AuthenticationPrincipal PlayerUserDetails userDetails)
            throws JsonProcessingException {
        return new ObjectMapper().writerWithView(View.Buy.class)
                    .writeValueAsString(
                            marketManager.findMarketByCityId(
                                travelService.getCurrentCity(
                                        userDetails.getPlayerId())
                                            .getCityId())
                                    .getAllGoodsValues());
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