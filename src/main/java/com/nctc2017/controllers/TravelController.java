package com.nctc2017.controllers;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.bean.City;
import com.nctc2017.services.TravelService;

@Controller
public class TravelController {
    private static final String CITY_NAME_VAR = "city_name_";
    private static final String CITY_ID = "city_id_";
        
    @Autowired
    private TravelService travelService;
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/world", method = RequestMethod.GET)
    public ModelAndView worldWelcome(
            @RequestParam(value = "info", required = false) String info) {
        ModelAndView model = new ModelAndView();
        List<City> cities = travelService.getCities();
        for (int i = 0; i < cities.size(); i++) {
            City nextCity = cities.get(i);
            model.addObject(CITY_NAME_VAR + (i + 1), nextCity.getCityName());
            model.addObject(CITY_ID + (i + 1), nextCity.getCityId());
        }
        model.addObject("info", info);
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
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/relocate", method = RequestMethod.POST)
    public ModelAndView wantsToRelocate(@RequestParam(value = "city_id", required = false) String id) {
        BigInteger cityId = new BigInteger(id);
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        ModelAndView model = new ModelAndView();
        City currCity = travelService.getCurrentCity(debugId);
        if (currCity.getCityId().equals(cityId)) {
            model.setStatus(HttpStatus.FOUND);
            model.addObject("errorMes", "You already in " + currCity.getCityName() + ", Captain!");
            model.addObject("errTitle", "You drunk!");
            model.setViewName("fragment/message");
        } else {
            model.setStatus(HttpStatus.OK);
            model.setViewName("TravelView");
        }
        
        return model;
    }

}