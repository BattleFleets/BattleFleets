package com.nctc2017.controllers;

import java.math.BigInteger;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.bean.City;
import com.nctc2017.services.TravelService;

@Controller
public class CityActionsController {
    private static final Logger LOG = Logger.getLogger(CityActionsController.class);

    @Autowired
    private TravelService travelService;
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/travel", method = RequestMethod.GET)
    public ModelAndView travelWelcome() {
       // travelService
        ModelAndView model = new ModelAndView();
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        boolean fleetSpeedOk = travelService.isFleetSpeedOk(debugId);
        boolean sailorsEnough = travelService.isSailorsEnough(debugId);
        boolean emptyStock = travelService.isEmptyStock(debugId);
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
    public ModelAndView getCity() {
        ModelAndView model = new ModelAndView();
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        while (true) {
            int time = travelService.getRelocateTime(debugId);
            if (time == Integer.MIN_VALUE) break;
            try {
                Thread.currentThread().wait(1000);
            } catch (InterruptedException e) {
                LOG.error("User thread was interrupted while entering in new city", e);
            }
        }
        City currCity = travelService.getCurrentCity(debugId);
        model.addObject("msg", "This is protected page - Only for Users!");
        model.setViewName("CityView");
        model.addObject("city", currCity.getCityName());
        return model;
    }

}