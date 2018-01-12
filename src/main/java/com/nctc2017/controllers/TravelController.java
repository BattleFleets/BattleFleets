package com.nctc2017.controllers;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.bean.City;
import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.exception.BattleStartException;
import com.nctc2017.exception.PlayerNotFoundException;
import com.nctc2017.services.TravelService;

@Controller
public class TravelController {
    private static final Logger LOG = Logger.getLogger(TravelController.class);
    private static final int checkingCounter = 5; 
    private static final int checkingInterval = 2000;
        
    @Autowired
    private TravelService travelService;
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/world", method = RequestMethod.GET)
    public ModelAndView worldWelcome(@AuthenticationPrincipal PlayerUserDetails userDetails,
            @RequestParam(value = "info", required = false) String info) {
        BigInteger playerId = userDetails.getPlayerId();
        
        if (travelService.isPlayerInTravel(playerId)) {
            return new ModelAndView("redirect:/trip");
        }
        
        ModelAndView model = new ModelAndView();
        List<City> cities = travelService.getCities();
        model.addObject("cities", cities);
        model.addObject("info", info);
        model.setViewName("WorldView");
        return model;
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/is_enemy_on_horizon", method = RequestMethod.GET, produces="text/plain")
    @ResponseBody
    public ResponseEntity<String> isEnemyOnHorizon(@AuthenticationPrincipal PlayerUserDetails userDetails) 
            throws PlayerNotFoundException, InterruptedException {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " request isEnemyOnHorizon()");
        if (travelService.isParticipated(playerId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        boolean appeared = false;
        for (int i = 0; i < checkingCounter; i++) {
            appeared = travelService.isEnemyOnHorizon(playerId);
            if (appeared) {
                return ResponseEntity.ok(String.valueOf(appeared));
            }
            Thread.sleep(checkingInterval);
        }
        return ResponseEntity.ok(String.valueOf(appeared));
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/attack_decision", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void decision(
            @AuthenticationPrincipal PlayerUserDetails userDetails,
            @RequestParam(value = "decision", required = false) String decision
            ) throws PlayerNotFoundException, BattleStartException {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " request for make decision() - " + decision);
        travelService.confirmAttack(playerId, Boolean.valueOf(decision));
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/is_battle_start", method = RequestMethod.GET, produces="text/plain")
    @ResponseBody
    public String isBattleStart(@AuthenticationPrincipal PlayerUserDetails userDetails) 
            throws InterruptedException {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " request isBattleStart()");
        boolean start = false;
        for (int i = 0; i < checkingCounter; i++) {
            start = travelService.isBattleStart(playerId);
            if (start) {
                return String.valueOf(start);
            }
            Thread.sleep(checkingInterval);
        }
        return String.valueOf(start);
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/relocate", method = RequestMethod.POST)
    public ModelAndView wantsToRelocate(
            @AuthenticationPrincipal PlayerUserDetails userDetails,
            @RequestParam(value = "city_id", required = false) String id) {
        BigInteger cityId = new BigInteger(id);
        BigInteger playerId = userDetails.getPlayerId();
        ModelAndView model = new ModelAndView();
        City currCity = travelService.getCurrentCity(playerId);
        if (currCity.getCityId().equals(cityId)) {
            model.setStatus(HttpStatus.LOCKED);
            model.addObject("errorMes", "You already in " + currCity.getCityName() + ", Captain!");
            model.addObject("errTitle", "You drunk!");
        } else {
            
            try {
                travelService.relocate(playerId, cityId);
                
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
    public String isDecisionAccept(@AuthenticationPrincipal PlayerUserDetails userDetails) 
            throws InterruptedException, PlayerNotFoundException {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " request isDecisionAccept()");
        boolean accept = false; 
        for (int i = 0; i < checkingCounter; i++) {
            accept = travelService.isDecisionAccept(playerId);
            if (accept) {
                return String.valueOf(accept);
            }
            Thread.sleep(checkingInterval);
        }
        return String.valueOf(accept);
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/trip", method = RequestMethod.GET)
    public ModelAndView travelWelcome(@AuthenticationPrincipal PlayerUserDetails userDetails) {
        ModelAndView model = new ModelAndView();
        BigInteger playerId = userDetails.getPlayerId();
        int relocateTime = travelService.getRelocateTime(playerId);
        City city;
        try {
            city = travelService.getRelocationCity(playerId);
            model.addObject("city", city.getCityName());
        } catch (PlayerNotFoundException e) {
            return new ModelAndView("redirect:/city");
        }
        model.addObject("time", relocateTime);
        model.setStatus(HttpStatus.OK);
        model.setViewName("TravelView");
        return model;
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleCustomException(RuntimeException ex) {

        ModelAndView model = new ModelAndView("/error");
        model.addObject("reason", ex.getMessage());

        return model;
    }
}