package com.nctc2017.controllers;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.bean.City;
import com.nctc2017.exception.BattleStartException;
import com.nctc2017.exception.PlayerNotFoundException;
import com.nctc2017.services.TravelService;

@Controller
public class TravelController {
    private static final Logger LOG = Logger.getLogger(TravelController.class);
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
    @RequestMapping(value = "/is_enemy_on_horizon", method = RequestMethod.GET, produces="text/plain")
    @ResponseBody
    public String isEnemyOnHorizon() throws PlayerNotFoundException {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        return String.valueOf(travelService.isEnemyOnHorizon(debugId));
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/attack_decision", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void decision(String decision) throws PlayerNotFoundException, BattleStartException {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        
        travelService.confirmAttack(debugId, Boolean.valueOf(decision));
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/is_battle_start", method = RequestMethod.GET, produces="text/plain")
    @ResponseBody
    public String isBattleStart() {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        boolean start = travelService.isBattleStart(debugId);
        return String.valueOf(start);
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/relocate", method = RequestMethod.POST)
    public ModelAndView wantsToRelocate(@RequestParam(value = "city_id", required = false) String id) {
        BigInteger cityId = new BigInteger(id);
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        BigInteger debugId2 = BigInteger.valueOf(44L);
        ModelAndView model = new ModelAndView();
        City currCity = travelService.getCurrentCity(debugId);
        if (currCity.getCityId().equals(cityId)) {
            model.setStatus(HttpStatus.FOUND);
            model.addObject("errorMes", "You already in " + currCity.getCityName() + ", Captain!");
            model.addObject("errTitle", "You drunk!");
        } else {
            
            try {
                travelService.relocate(debugId, cityId);
                travelService.relocate(debugId2, cityId);//TODO delete
                
              //TODO delete
                try {
                    travelService.isEnemyOnHorizon(debugId2);
                } catch (PlayerNotFoundException e) {
                    LOG.error("debug player 2 not in travel", e);
                }
                
            } catch (IllegalAccessError e) {
                LOG.warn("Player try to relocate to another city while is already traveling."
                        + " Player continue his last trip.");
            }
            model.setStatus(HttpStatus.OK);
        }
        model.setViewName("fragment/message");
        
        return model;
    }
    @Secured("ROLE_USER")
    @RequestMapping(value = "/is_decision_accept", method = RequestMethod.GET, produces="text/plain")
    @ResponseBody
    public String isDecisionAccept() {
        BigInteger debugId = BigInteger.valueOf(43L);
        boolean accept = travelService.isDecisionAccept(debugId);
        return String.valueOf(accept);
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/trip", method = RequestMethod.GET)
    public ModelAndView travelWelcome() {
        ModelAndView model = new ModelAndView();
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        int relocateTime = travelService.getRelocateTime(debugId);
        City city = travelService.getRelocationCity(debugId);
        model.addObject("time", relocateTime);
        model.addObject("city", city.getCityName());
        model.setStatus(HttpStatus.OK);
        model.setViewName("TravelView");
        return model;
    }
}