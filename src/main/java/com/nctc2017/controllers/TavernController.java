package com.nctc2017.controllers;


import java.math.BigInteger;
import java.util.*;

import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.bean.Ship;
import com.nctc2017.services.MoneyService;
import com.nctc2017.services.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.List;


@Controller
public class TavernController {
    @Autowired
    private ShipService shipService;
    @Autowired
    private MoneyService moneyService;

    private ModelAndView model=new ModelAndView();

    @Secured("ROLE_USER")
    @RequestMapping(value = "/tavern", method = RequestMethod.GET)
    public ModelAndView tavernWelcome(
            @RequestParam(value = "tavern", required = false) String city,
            @AuthenticationPrincipal PlayerUserDetails userDetails) {
        List<Ship> ships = shipService.getAllPlayerShips(userDetails.getPlayerId());
        int sailorCost = shipService.getSailorCost();
        int money = moneyService.getPlayersMoney(userDetails.getPlayerId());
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("money", money);
        model.addObject("city", city);
        model.addObject("ships", ships);
        model.addObject("sailorCost",sailorCost);
        model.setViewName("TavernView");
        return model;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/buySailors", method = RequestMethod.POST)
    public ModelAndView buySailors(@RequestParam(value="shipId",required = false) BigInteger shipId,
                                   @RequestParam(value="num",required = false) int newSailors,
                                   @RequestParam(value="toSpend",required = false) Integer cost,
                                   @AuthenticationPrincipal PlayerUserDetails userDetails){
        Ship ship = shipService.findShip(shipId);
        shipService.updateShipSailorsNumber(shipId, ship.getCurSailorsQuantity()+newSailors);
        int money = moneyService.deductMoney(userDetails.getPlayerId(), cost);
        List<Ship> ships = shipService.getAllPlayerShips(userDetails.getPlayerId());
        model.addObject("money",money);
        model.addObject("ships",ships);
        return model;
    }

}