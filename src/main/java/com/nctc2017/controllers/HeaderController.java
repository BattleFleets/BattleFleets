package com.nctc2017.controllers;

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
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.List;

@Controller
public class HeaderController {
    @Autowired
    private LevelUpService lvlUpService;

    @Autowired
    private MoneyService moneyService;

    @Autowired
    private ShipService shipService;


    @Secured("ROLE_USER")
    @RequestMapping(value="/addHeader",method = RequestMethod.GET)
    public ModelAndView header(@AuthenticationPrincipal PlayerUserDetails userDetails, ModelAndView model){
        BigInteger playerId = userDetails.getPlayerId();
        String login = lvlUpService.getLogin(playerId);
        int money = moneyService.getPlayersMoney(playerId);
        int level = lvlUpService.getCurrentLevel(playerId);
        int points = lvlUpService.getCurrentPoints(playerId);
        int nextLevel = lvlUpService.getNextLevel(playerId);
        int maxShips = lvlUpService.getMaxShips(playerId);
        int income = lvlUpService.getPassiveIncome(playerId);
        int pointsToNxtLvl =lvlUpService.getPointsToNxtLevel(playerId);
        int nextImprove = lvlUpService.getNextLevel(playerId);
        List<Ship> currShips = shipService.getAllPlayerShips(playerId);
        model.addObject("login", login);
        model.addObject("money", money);
        model.addObject("points", points);
        model.addObject("level", level);
        model.addObject("nextLevel", nextLevel);
        model.addObject("currShips", currShips.size());
        model.addObject("maxShips", maxShips);
        model.addObject("income", income);
        model.addObject("toNxtLevel", pointsToNxtLvl);
        model.addObject("nextImprove",nextImprove);
        model.setViewName("fragment/header");
        return model;
    }
}
