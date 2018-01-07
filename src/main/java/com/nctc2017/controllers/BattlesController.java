package com.nctc2017.controllers;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nctc2017.bean.Ship;
import com.nctc2017.exception.BattleEndException;
import com.nctc2017.services.BattlePreparationService;
import com.nctc2017.services.BattlePreparationService.ShipWrapper;
import com.nctc2017.services.BattleService;

@Controller
public class BattlesController {
    private static final Logger LOG = Logger.getLogger(BattlesController.class);
    @Autowired
    private BattlePreparationService prepService;
    @Autowired
    private BattleService battleService;
    
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
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public String pickShip(
            @RequestParam(value = "ship_id", required = false) String shipId) {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        LOG.debug("Player_" + 43 + " Ship picked request Ship: " + shipId);
        prepService.chooseShip(debugId, new BigInteger(shipId));
        prepService.setReady(debugId);
        return "Wait for enemy pick...";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/wait_for_enemy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String waitForEnemy() throws BattleEndException, InterruptedException {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        LOG.debug("Player_" + 43 + " wait for enemy ready request");
        boolean ready = prepService.waitForEnemyReady(debugId);
        while(!ready){
            Thread.sleep(2000);
            ready = prepService.waitForEnemyReady(debugId);
        }
        
        return String.valueOf(ready);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/battle", method = RequestMethod.GET)
    public ModelAndView getBattle() {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        LOG.debug("Player_" + 43 + " battle request");
        ModelAndView model = new ModelAndView("BattleView");
        model.setStatus(HttpStatus.OK);
        return model;
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/battle", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void fire(int[][] cannonMatrix)  {
        
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/fire_results", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> fireResults() throws JsonProcessingException {
        BigInteger debugId = BigInteger.valueOf(43L);//TODO replace after AughRegController will completed
        if (battleService.isStepResultAvalible(debugId)) {
            Ship playerShip = battleService.getShipInBattle(debugId);
            Ship enemyShip = battleService.getEnemyShipInBattle(debugId);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Ship> shipMap = new HashMap<>();
            shipMap.put("enemy_ship", enemyShip);
            shipMap.put("player_ship", playerShip);
            String jsonShips = mapper.writeValueAsString(shipMap);
            return ResponseEntity.ok(jsonShips);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
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