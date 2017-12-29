package com.nctc2017.controllers;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.services.TravelService;

@Controller
public class TravelController {
    
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/world", method = RequestMethod.GET)
    public ModelAndView worldWalcome(
            @RequestParam(value = "info", required = false) String city) {
        ModelAndView model = new ModelAndView();
        model.addObject("info", city);
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