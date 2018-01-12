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
    @Autowired
    private LevelUpService lvlUpService;

    private ModelAndView model=new ModelAndView();

    @Secured("ROLE_USER")
    @RequestMapping(value = "/tavern", method = RequestMethod.GET)
    public ModelAndView tavernWelcome(
            @RequestParam(value = "tavern", required = false) String city,
            @AuthenticationPrincipal PlayerUserDetails userDetails) {
        BigInteger playerId = userDetails.getPlayerId();
        List<Ship> ships = shipService.getAllPlayerShips(playerId);
        int sailorCost = shipService.getSailorCost();
        String login = lvlUpService.getLogin(playerId);
        int money = moneyService.getPlayersMoney(playerId);
        int level = lvlUpService.getCurrentLevel(playerId);
        int points = lvlUpService.getCurrentPoints(playerId);
        int maxShips = lvlUpService.getMaxShips(playerId);
        int income = lvlUpService.getPassiveIncome(playerId);
        int pointsToNxtLvl =lvlUpService.getPointsToNxtLevel(playerId);
        int nextImprove = lvlUpService.getNextLevel(playerId);
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("login", login);
        model.addObject("money", money);
        model.addObject("points", points);
        model.addObject("level", level);
        model.addObject("city", city);
        model.addObject("ships", ships);
        model.addObject("maxShips", maxShips);
        model.addObject("income", income);
        model.addObject("toNxt", pointsToNxtLvl);
        model.addObject("sailorCost",sailorCost);
        model.addObject("nextImprove",nextImprove);
        model.setViewName("TavernView");
        return model;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/buySailors", method = RequestMethod.GET)
    @ResponseBody
    public String[] buySailors(@RequestParam(value="shipId",required = false) BigInteger shipId,
                               @RequestParam(value="num",required = false) String newSailors,
                               @RequestParam(value="toSpend",required = false) String cost,
                               @AuthenticationPrincipal PlayerUserDetails userDetails){
        Ship ship = shipService.findShip(shipId);
        int oldNumSailors = shipService.getSailorsNumber(shipId);
        int costForSailors;
        int sailors;
        if((oldNumSailors+Integer.valueOf(newSailors))>ship.getMaxSailorsQuantity()){
            costForSailors=Integer.valueOf(cost)-
                    ((oldNumSailors+Integer.valueOf(newSailors))-ship.getMaxSailorsQuantity())*shipService.getSailorCost();
            sailors=ship.getMaxSailorsQuantity();
        }
        else
        {
            costForSailors=Integer.valueOf(cost);
            sailors=oldNumSailors+Integer.valueOf(newSailors);
        }
        int money = moneyService.deductMoney(userDetails.getPlayerId(), costForSailors);
        shipService.updateShipSailorsNumber(shipId, sailors);
        int curSailors = shipService.getSailorsNumber(shipId);
        String[] results = new String[2];
        results[0] = String.valueOf(money);
        results[1] = String .valueOf(curSailors);
        return results;
    }

}