package com.nctc2017.controllers;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.exception.BattleEndException;
import com.nctc2017.services.BattlePreparationService;
import com.nctc2017.services.BattlePreparationService.ShipWrapper;

@Controller
public class BattlesController {
    private static final Logger LOG = Logger.getLogger(BattlesController.class);
    @Autowired
    private BattlePreparationService prepService;
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/battle_preparing", method = RequestMethod.GET)
    public ModelAndView battleWelcome() {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        BigInteger debugId2 = BigInteger.valueOf(44L);
        List<ShipWrapper> fleet = prepService.getShipsExtraInfo(debugId);
        List<ShipWrapper> enemyFleet = prepService.getShipsExtraInfo(debugId2);
        //List<Ship> fleet = new ArrayList<Ship>(2);
        ///fleet.add(new Ship(new ShipTemplate(null, "Caravella", 120, 100, 12000, 3, 28, 150), null, "Ferdenand", 119, 99, 149));
        //fleet.add(new Ship(new ShipTemplate(null, "Fregatta", 121, 101, 12001, 4, 29, 151), null, "Ferdenand2", 118, 98, 148));
        //LOG.debug(fleet.size());
        int time = prepService.autoChoiceShipTimer(debugId);
        prepService.autoChoiceShipTimer(debugId2);//TODO delete
        ModelAndView model = new ModelAndView("BattlePreparingView");
        model.addObject("fleet", fleet);
        model.addObject("enemy_fleet", enemyFleet);
        model.addObject("timer", time);
        return model;
    }

    @Secured("ROLE_USER")
    public boolean escape(int id, int idHash) {
        // TODO implement here
        return false;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/pick_ship", method = RequestMethod.POST)
    public ModelAndView pickShip(
            @RequestParam(value = "ship_id", required = false) String shipId) {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        ModelAndView model = new ModelAndView("fragment/message");
        model.addObject("errorMes","Wait...");
        prepService.chooseShip(debugId,new BigInteger(shipId));
        prepService.setReady(debugId);
        LOG.debug("Ship picked " + shipId);
        return(model);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/wait_for_enemy", method = RequestMethod.GET)
    public String waitForEnemy() throws BattleEndException, InterruptedException {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        boolean ready = prepService.waitForEnemyReady(debugId);
        while(!ready){
            Thread.sleep(2000);
            ready = prepService.waitForEnemyReady(debugId);
        }
        
        return String.valueOf(ready);
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