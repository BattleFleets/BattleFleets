package com.nctc2017.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.bean.View;
import com.nctc2017.exception.GoodsLackException;
import com.nctc2017.exception.MoneyLackException;
import com.nctc2017.services.MoneyService;
import com.nctc2017.services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.method.annotation.*;

import java.math.BigInteger;

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
    @RequestMapping(value = "/market/buy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> buy(@RequestParam(value = "goodsTemplateId") BigInteger goodsTemplateId,
                                      @RequestParam(value = "price") int price,
                                      @RequestParam(value = "quantity") int quantity,
                                      @AuthenticationPrincipal PlayerUserDetails userDetails) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(tradeService
                    .buy(userDetails.getPlayerId(),goodsTemplateId,price,quantity));
        }
        catch(GoodsLackException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(MoneyLackException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/market/sell", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> sell(@RequestParam(value = "goodsId") BigInteger goodsId,
                     @RequestParam(value = "goodsTemplateId") BigInteger goodsTemplateId,
                     @RequestParam(value = "price") int price,
                     @RequestParam(value = "quantity") int quantity,
                     @AuthenticationPrincipal PlayerUserDetails userDetails) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(tradeService.sell(userDetails
                    .getPlayerId(),goodsId, goodsTemplateId,price,quantity));
        }
        catch(GoodsLackException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/market/getBuyGoods", method = RequestMethod.GET)
    @ResponseBody
    public String getAllGoodsForBuying(@AuthenticationPrincipal PlayerUserDetails userDetails)
            throws JsonProcessingException {
        //moneyService.addMoney(userDetails.getPlayerId(),999000000);
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleBadParameters(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity must be a natural number!");
    }

}
