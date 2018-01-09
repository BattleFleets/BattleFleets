package com.nctc2017.controllers;


import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.bean.Ship;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.exception.BattleEndException;
import com.nctc2017.services.BattleEndingService;
import com.nctc2017.services.BattlePreparationService;
import com.nctc2017.services.BattlePreparationService.ShipWrapper;
import com.nctc2017.services.BattleService;
import com.nctc2017.services.utils.BattleEndVisitor;

@Controller
public class BattlesController {
    private static final Logger LOG = Logger.getLogger(BattlesController.class);
    @Autowired
    private BattlePreparationService prepService;
    @Autowired
    private BattleService battleService;
    @Autowired
    private BattleEndingService battleEndServ;
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/battle_preparing", method = RequestMethod.GET)
    public ModelAndView battleWelcome(@AuthenticationPrincipal PlayerUserDetails userDetails) {
        BigInteger playerId = userDetails.getPlayerId();
        BigInteger debugId2 = BigInteger.valueOf(44L);//TODO delete after debug complete
        List<ShipWrapper> fleet = prepService.getShipsExtraInfo(playerId);
        List<ShipWrapper> enemyFleet = prepService.getShipsExtraInfo(debugId2);
        int time = prepService.autoChoiceShipTimer(playerId);
        prepService.autoChoiceShipTimer(debugId2);//TODO delete
        ModelAndView model = new ModelAndView("BattlePreparingView");
        model.addObject("fleet", fleet);
        LOG.debug(fleet.get(0).getCannons());
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
            @AuthenticationPrincipal PlayerUserDetails userDetails,
            @RequestParam(value = "ship_id", required = true) String shipId) {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " Ship picked request Ship: " + shipId);
        prepService.chooseShip(playerId, new BigInteger(shipId));
        prepService.setReady(playerId);
        return "Wait for enemy pick...";
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/wait_for_enemy", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String waitForEnemy(
            @AuthenticationPrincipal PlayerUserDetails userDetails) 
                    throws BattleEndException, InterruptedException {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " wait for enemy ready request");
        boolean ready = prepService.waitForEnemyReady(playerId);
        while(!ready){
            Thread.sleep(2000);
            ready = prepService.waitForEnemyReady(playerId);
        }
        
        return String.valueOf(ready);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/battle", method = RequestMethod.GET)
    public ModelAndView getBattle(
            @AuthenticationPrincipal PlayerUserDetails userDetails) {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " battle request");
        ModelAndView model = new ModelAndView("BattleView");
        model.setStatus(HttpStatus.OK);
        return model;
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/fire", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void fire(
            @AuthenticationPrincipal PlayerUserDetails userDetails,
            @RequestParam(value = "ammoCannon[]") int[] ammoCannon,
            @RequestParam(value = "dim") int dim) throws SQLException  {
        int[][] ammoCannon2 = new int[dim][];
        int k = 0;
        for (int i = 0; i < ammoCannon2.length; i++) {
            int rowDim = ammoCannon.length / dim;
            ammoCannon2[i] = new int[rowDim];
            for (int j = 0; j < ammoCannon2[i].length; j++) {
                ammoCannon2[i][j] = ammoCannon[k++];
            }
        }
        for (int[] i : ammoCannon2) {
            for (int j : i) {
                LOG.debug(j);
            }
            LOG.debug(" ");
        }
        LOG.debug(dim);
       // BigInteger plaerId  = userDetails.getPlayerId();
       // battleService.calculateDamage(ammoCannon, plaerId, new DefaultDestroyBattleEnd());
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/fire_results", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> fireResults(
            @AuthenticationPrincipal PlayerUserDetails userDetails) 
                    throws JsonProcessingException, InterruptedException {
        BigInteger playerId = userDetails.getPlayerId();
        while(true) {
            if (battleService.isStepResultAvalible(playerId)) {
                Ship playerShip = battleService.getShipInBattle(playerId);
                Ship enemyShip = battleService.getEnemyShipInBattle(playerId);
                
                Map<String, Object> shipMap = new HashMap<>();
                shipMap.put("enemy_ship", enemyShip);
                shipMap.put("player_ship", playerShip);
                int distance = battleService.getDistance(playerId);
                shipMap.put("distance", distance);
    
                ObjectMapper mapper = new ObjectMapper();
                String jsonShips = mapper.writeValueAsString(shipMap);
                
                return ResponseEntity.ok(jsonShips);
            } else {
                Thread.sleep(2000);
            }
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
    
    private class DefaultDestroyBattleEnd implements BattleEndVisitor {

        @Override
        public void endCaseVisit(PlayerDao playerDao, ShipDao shipDao, BigInteger winnerShipId, BigInteger loserShipId,
                BigInteger winnerId, BigInteger loserId) {
            battleEndServ.passDestroyGoodsToWinner(winnerShipId, loserShipId);
        }
    }

}