package com.nctc2017.controllers;

import java.math.BigInteger;

import com.nctc2017.services.LevelUpService;
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
        model.addObject("msg", "This is protected page - Only for Users!");
        model.setViewName("CityView");
        model.addObject("level", lvlUpService.getCurrentLevel(playerId));
        model.addObject("nextLevel", lvlUpService.getNextLevel(playerId));
        model.addObject("city", currCity.getCityName());
        return model;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update( @RequestParam(value="diff",required = false) int diff){
        ModelAndView model = new ModelAndView();
        model.setViewName("UpdateView");
        model.addObject("diff", diff);
        return model;
    }
    @Secured("ROLE_USER")
    @RequestMapping(value = "/incomeUp", method = RequestMethod.GET)
    public ModelAndView incomeUp(@AuthenticationPrincipal PlayerUserDetails userDetails,
                                 @RequestParam(value="diffIncome",required = false) int diff){
       if(diff>=0) {
           lvlUpService.incomeUp(userDetails.getPlayerId());
           lvlUpService.updateNxtLvl(userDetails.getPlayerId());
           diff-=5;
           return update(diff);
       }
       else {
           return getCity(userDetails);
       }
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/shipUp", method = RequestMethod.GET)
    public ModelAndView shipUp(@AuthenticationPrincipal PlayerUserDetails userDetails,
                               @RequestParam(value="diffShip",required = false) int diff){
        if(diff>=0) {
            lvlUpService.shipUp(userDetails.getPlayerId());
            lvlUpService.updateNxtLvl(userDetails.getPlayerId());
            diff-=5;
            return update(diff);
        }
        else {
            return getCity(userDetails);
        }
    }
}