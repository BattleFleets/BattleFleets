package com.nctc2017.controllers;


import java.math.BigInteger;

import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.bean.Ship;
import com.nctc2017.services.LevelUpService;
import com.nctc2017.services.MoneyService;
import com.nctc2017.services.ShipService;
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


@Controller
public class TavernController {
    @Autowired
    private ShipService shipService;
    @Autowired
    private MoneyService moneyService;

    @Secured("ROLE_USER")
    @RequestMapping(value = "/tavern", method = RequestMethod.GET)
    public ModelAndView tavernWelcome(
            @RequestParam(value = "tavern", required = false) String city,
            @AuthenticationPrincipal PlayerUserDetails userDetails) {
        ModelAndView model=new ModelAndView();
        BigInteger playerId = userDetails.getPlayerId();
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
    @RequestMapping(value = "/buySailors", method = RequestMethod.GET)
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
        boolean complete = shipService.isAllShipsCompleted(userDetails.getPlayerId());
        boolean enoughMoney = moneyService.isEnoughMoney(userDetails.getPlayerId(), sailorCost);
        String[] results = new String[4];
        results[0] = String.valueOf(money);
        results[1] = String .valueOf(curSailors);
        results[2] = String.valueOf(complete);
        results[3] = String.valueOf(enoughMoney);
        return results;
    }

}