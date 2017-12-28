package com.nctc2017.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TravelController {

    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/travel", method = RequestMethod.GET)
    public ModelAndView travelWalcome(
            @RequestParam(value = "travel", required = false) String city) {
        ModelAndView model = new ModelAndView();
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("city", city);
        model.setViewName("WorldView");
        return model;
    }
    
    @Secured("ROLE_USER")
    public boolean isEnemyOnHorizon(int id, int idHash) {
        // TODO implement here
        return false;
    }

    public void decision(int id, int idHash, String dec) {
        // TODO implement here
    }

    public void isBattleStart(int id, int idHash) {
        // TODO implement here
    }

    public void wantsToRelocate(int id, int idHash, String city) {
        // TODO implement here
    }

    public String getCurrentCity(int id, int idHash) {
        // TODO implement here
        return "";
    }

    public String getCities() {
        // TODO implement here
        return "";
    }

}