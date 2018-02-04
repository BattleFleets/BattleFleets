package com.nctc2017.controllers;


import java.math.BigInteger;

import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.bean.Ship;
import com.nctc2017.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
public class TavernController {
    @Autowired
    private ShipService shipService;
    @Autowired
    private MoneyService moneyService;
    @Autowired
    private TravelService travelService;
    @Autowired
    private BattleService battleService;

    @Secured("ROLE_USER")
    @RequestMapping(value = "/tavern", method = RequestMethod.GET)
    public ModelAndView tavernWelcome(
            @RequestParam(value = "tavern", required = false) String city,
            @AuthenticationPrincipal PlayerUserDetails userDetails) {
        BigInteger playerId = userDetails.getPlayerId();
        if(travelService.isPlayerInTravel(playerId)){
           return new ModelAndView("redirect:/trip");
        }
        ModelAndView model=new ModelAndView();
        List<Ship> ships = shipService.getAllPlayerShips(playerId);
        int sailorCost = shipService.getSailorCost();
        int money = moneyService.getPlayersMoney(playerId);
        int completedShip = shipService.numShipsCompleted(playerId);
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("money", money);
        model.addObject("city", city);
        model.addObject("ships", ships);
        model.addObject("completedShip", completedShip);
        model.addObject("sailorCost",sailorCost);
        model.setViewName("TavernView");
        return model;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/maxValue", method = RequestMethod.GET)
    @ResponseBody
    public String[] maxValue(
            @RequestParam(value = "shipId", required = false) BigInteger shipId,
            @AuthenticationPrincipal PlayerUserDetails userDetails){
        String results[] = new String[2];
        Ship ship = shipService.findShip(shipId);
        int curr = ship.getCurSailorsQuantity();
        int limit = ship.getMaxSailorsQuantity();
        int money = moneyService.getPlayersMoney(userDetails.getPlayerId());
        int cost = shipService.getSailorCost();
        int canToBuy = (int)Math.floor(money / cost);
        if (canToBuy < (limit - curr)) {
            results[0] = String.valueOf(canToBuy);
            results[1] = String.valueOf(canToBuy*cost);
            return results;
        }
        else {
            results[0] = String.valueOf(limit-curr);
            results[1] = String.valueOf((limit-curr)*cost);
            return  results;
        }
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/cost", method = RequestMethod.GET)
    @ResponseBody
    public String cost(@RequestParam(value="val",required = false) String val,
                       @RequestParam(value="max",required = false) String max){
        Pattern p = Pattern.compile("^[0-9]+$");
        Matcher m = p.matcher(val);
            if (!val.isEmpty() &&(!m.find() || (Integer.valueOf(val) <= 0 || Integer.valueOf(val) > Integer.valueOf(max)))) {
                return max;
            } else {
                return val;
            }

    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/allStuffed", method = RequestMethod.GET)
    @ResponseBody
    public String isAllShipStuffed(@AuthenticationPrincipal PlayerUserDetails userDetails,
                                   @RequestParam(value="msg",required = false) String msg){
        BigInteger playerId = userDetails.getPlayerId();
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(msg);
        if(shipService.isAllShipsCompleted(playerId)){
             return "All your ships are stuffed with sailors";
        }
        else if(m.find()){
            return msg;
        }
        else{
            return "You can hire sailors on your ships";
        }

    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/hireSailors", method = RequestMethod.GET)
    @ResponseBody
    public String[] buySailors(@RequestParam(value="shipId",required = false) BigInteger shipId,
                               @RequestParam(value="num",required = false) String newSailors,
                               @AuthenticationPrincipal PlayerUserDetails userDetails){
        int oldNumSailors = shipService.getSailorsNumber(shipId);
        int sailorCost = shipService.getSailorCost();
        int cost = sailorCost*Integer.valueOf(newSailors);
        int sailors = oldNumSailors + Integer.valueOf(newSailors);
        int money = moneyService.deductMoney(userDetails.getPlayerId(), cost);
        shipService.updateShipSailorsNumber(shipId, sailors);
        int curSailors = shipService.getSailorsNumber(shipId);
        boolean shipComplete = shipService.isShipComplete(shipId);
        boolean enoughMoney = moneyService.isEnoughMoney(userDetails.getPlayerId(), sailorCost);
        String[] results = new String[4];
        results[0] = String.valueOf(money);
        results[1] = String .valueOf(curSailors);
        results[2] = String.valueOf(shipComplete);
        results[3] = String.valueOf(enoughMoney);
        return results;
    }

}