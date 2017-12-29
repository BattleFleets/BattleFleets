package com.nctc2017.controllers;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.services.TravelService;

@Controller
public class CityActionsController {


    @Autowired
    private TravelService travelService;
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/travel", method = RequestMethod.GET)
    public ModelAndView travelWalcome() {
       // travelService
        ModelAndView model = new ModelAndView();
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        boolean fleetSpeedOk = travelService.isFleetSpeedOk(debugId);
        boolean sailorsEnough = travelService.isSailorsEnough(debugId);
        model.setViewName("fragment/message");
        if (fleetSpeedOk && sailorsEnough) {
            model.setStatus(HttpStatus.OK);
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
    public String checkSailors(int id, int idHash) {
        // TODO implement here
        return "";
    }

    @Secured("ROLE_USER")
    public String checkSpeed(int id, int idHash) {
        // TODO implement here
        return "";
    }

    @Secured("ROLE_USER")
    public boolean stokIsEmpty(int id, int idHash) {
        // TODO implement here
        return false;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/city**", method = RequestMethod.GET)
    public ModelAndView getCity() {
        ModelAndView model = new ModelAndView();
        model.addObject("msg", "This is protected page - Only for Users!");
        model.setViewName("CityView");
        model.addObject("city", "Port Royal");
        return model;
    }

}