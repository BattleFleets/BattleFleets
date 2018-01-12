package com.nctc2017.controllers;

import java.math.BigInteger;

import com.nctc2017.services.LevelUpService;
import com.nctc2017.services.MoneyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.bean.City;
import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.services.TravelService;

@Controller
public class CityActionsController {
    private static final Logger LOG = Logger.getLogger(CityActionsController.class);

    @Autowired
    private TravelService travelService;

    @Autowired
    private LevelUpService lvlUpService;

    @Autowired
    private MoneyService moneyService;

    @Secured("ROLE_USER")
    @RequestMapping(value = "/travel", method = RequestMethod.GET)
    public ModelAndView travelWelcome(@AuthenticationPrincipal PlayerUserDetails userDetails) {
       // travelService
        ModelAndView model = new ModelAndView();
        BigInteger playerId = userDetails.getPlayerId();
        boolean fleetSpeedOk = travelService.isFleetSpeedOk(playerId);
        boolean sailorsEnough = travelService.isSailorsEnough(playerId);
        boolean emptyStock = travelService.isEmptyStock(playerId);
        model.setViewName("fragment/message");
        if (fleetSpeedOk && sailorsEnough && emptyStock) {
            model.setStatus(HttpStatus.OK);
        } else if (!emptyStock) {
            model.setStatus(HttpStatus.FOUND);
            model.addObject("errorMes", "You forgot something in stock. Do you want to come back?");
            model.addObject("errTitle", "Things in stock");
        } else {
            model.setStatus(HttpStatus.LOCKED);
            model.addObject("errorMes", 
                    !fleetSpeedOk 
                        ? "Thousand devils!!! The masts are damaged, the ship can not leave the port." 
                        : !sailorsEnough 
                            ? "You need to hire more sailors" 
                            : "New condition not processed");
        }
        return model;
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/city**", method = RequestMethod.GET)
    public ModelAndView getCity(@AuthenticationPrincipal PlayerUserDetails userDetails) {
        ModelAndView model = new ModelAndView();
        BigInteger playerId =userDetails.getPlayerId();
        while (true) {
            int time = travelService.getRelocateTime(playerId);
            if (time <= 0) break;
            else if (time > 5) return new ModelAndView("redirect:/trip");
            try {
                Thread.sleep(time * 1000);
            } catch (InterruptedException e) {
                LOG.error("User thread was interrupted while entering in new city", e);
            }
        }
        City currCity = travelService.getCurrentCity(playerId);
        String login = lvlUpService.getLogin(playerId);
        int money = moneyService.getPlayersMoney(playerId);
        int level = lvlUpService.getCurrentLevel(playerId);
        int points = lvlUpService.getCurrentPoints(playerId);
        int nextLevel = lvlUpService.getNextLevel(playerId);
        int maxShips = lvlUpService.getMaxShips(playerId);
        int income = lvlUpService.getPassiveIncome(playerId);
        int pointsToNxtLvl =lvlUpService.getPointsToNxtLevel(playerId);
        int nextImprove = lvlUpService.getNextLevel(playerId);
        model.setViewName("CityView");
        model.addObject("login", login);
        model.addObject("money", money);
        model.addObject("points", points);
        model.addObject("level", level);
        model.addObject("nextLevel", nextLevel);
        model.addObject("maxShips", maxShips);
        model.addObject("income", income);
        model.addObject("toNxt", pointsToNxtLvl);
        model.addObject("city", currCity.getCityName());
        model.addObject("nextImprove",nextImprove);
        return model;
    }

   }