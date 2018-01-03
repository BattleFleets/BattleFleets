package com.nctc2017.controllers;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.services.BattlePreparationService;
import com.nctc2017.services.BattlePreparationService.ShipWrapper;

@Controller
public class BattlesController {
    private static final Logger LOG = Logger.getLogger(TravelController.class);
    @Autowired
    private BattlePreparationService prepService;
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/battle_preparing", method = RequestMethod.GET)
    public ModelAndView battleWelcome() {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        List<ShipWrapper> fleet = prepService.getShipsExtraInfo(debugId);
        //List<Ship> fleet = new ArrayList<Ship>(2);
        ///fleet.add(new Ship(new ShipTemplate(null, "Caravella", 120, 100, 12000, 3, 28, 150), null, "Ferdenand", 119, 99, 149));
        //fleet.add(new Ship(new ShipTemplate(null, "Fregatta", 121, 101, 12001, 4, 29, 151), null, "Ferdenand2", 118, 98, 148));
        //LOG.debug(fleet.size());
        ModelAndView model = new ModelAndView("BattlePreparingView");
        model.addObject("fleet", fleet);
        return model;
    }

    @Secured("ROLE_USER")
    public boolean escape(int id, int idHash) {
        // TODO implement here
        return false;
    }

    @Secured("ROLE_USER")
    public void submit(int id, int idHash, int idShip) {
        // TODO implement here
    }

    @Secured("ROLE_USER")
    public boolean waitForEnemy(int id, int idHash) {
        // TODO implement here
        return false;
    }

    @Secured("ROLE_USER")
    public void getBattle(int id, int idHash) {
        // TODO implement here 
    }

    public void fire(int id, int idHash, int[][] cannonMatrix, boolean convergence) {
        // TODO implement here
    }

    public void fireResults(int id, int idHash) {
        // TODO implement here
    }

    public void bording(int id, int idHash) {
        // TODO implement here
    }

    public void getResource(int id, int idHash) {
        // TODO implement here  
    }

    public void getMoney(int id, int idHash) {
        // TODO implement here 
    }

    public void endBattle(int id, int idHash) {
        // TODO implement here
    }

    public void payoff(int id, int idHash) {
        // TODO implement here
    }

    public void surrender(int id, int idHash) {
        // TODO implement here
    }

}