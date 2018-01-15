package com.nctc2017.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.bean.View;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.services.MoneyService;
import com.nctc2017.services.TradeService;
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
    TradeService tradeService;

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

    @Secured("ROLE_USER")
    public void sell(int id, int idHash, int goodsTemplateId, int buyingCost, int quantity, int price) {
        // TODO implement here
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/market/getBuyGoods", method = RequestMethod.GET)
    @ResponseBody
    public String getAllGoodsForBuying(@AuthenticationPrincipal PlayerUserDetails userDetails)
            throws JsonProcessingException {
        //moneyService.addMoney(userDetails.getPlayerId(),30000000);
        //tradeService.buy(userDetails.getPlayerId(), DatabaseObject.RUM_TEMPLATE_ID, tradeService.getMarketGoodsByPlayerId(userDetails.getPlayerId()).get(19).getBuyingPrice(), 11);
        return new ObjectMapper().writerWithView(View.Buy.class)
                    .writeValueAsString(tradeService.getMarketGoodsByPlayerId(userDetails.getPlayerId()));
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/market/getSellGoods", method = RequestMethod.GET)
    @ResponseBody
    public String getAllGoodsForSelling(@AuthenticationPrincipal PlayerUserDetails userDetails)
    throws JsonProcessingException{
        return new ObjectMapper().writerWithView(View.Sell.class)
                .writeValueAsString(tradeService.getPlayersGoodsForSale(userDetails.getPlayerId()));
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/market/myMoney", method = RequestMethod.GET)
    @ResponseBody
    public String getMoney(@AuthenticationPrincipal PlayerUserDetails userDetails){
        int money = moneyService.getPlayersMoney(userDetails.getPlayerId());
        return String.valueOf(money);
    }

}