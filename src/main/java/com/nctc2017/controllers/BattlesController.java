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
import org.springframework.http.MediaType;
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
        List<ShipWrapper> fleet = prepService.getShipsExtraInfo(playerId);
        List<Ship> enemyFleet;
        try {
            enemyFleet = prepService.getEnemyShips(playerId);
        } catch (BattleEndException e) {
            enemyFleet = new ArrayList<>();
        }
        int time = prepService.autoChoiceShipTimer(playerId);
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
            @AuthenticationPrincipal PlayerUserDetails userDetails,
            @RequestParam(value = "ship_id", required = true) String shipId) throws BattleEndException {
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
            @RequestParam(value = "dim") int dim,
            @RequestParam(value = "decrease") boolean decrease) throws SQLException, BattleEndException  {
        int[][] ammoCannon2 = new int[dim][];
        int k = 0;
        for (int i = 0; i < ammoCannon2.length; i++) {
            int rowDim = ammoCannon.length / dim;
            ammoCannon2[i] = new int[rowDim];
            for (int j = 0; j < ammoCannon2[i].length; j++) {
                ammoCannon2[i][j] = ammoCannon[k++];
            }
        }

        BigInteger playerId  = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " fire request");
        LOG.debug("Player_" + playerId + " Convergace dist: " + decrease);
        battleService.setConvergaceOfDist(playerId, decrease);
        battleService.calculateDamage(ammoCannon2, playerId, new DefaultDestroyBattleEnd());
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/fire_results", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> fireResults(
            @AuthenticationPrincipal PlayerUserDetails userDetails,
            @RequestParam(value = "forcibly", required = false) Boolean forcibly) 
                    throws JsonProcessingException, InterruptedException, BattleEndException {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " request for getting fire result");
        if (forcibly == null) forcibly = false;
        while(true) {
            boolean avaliable = battleService.isStepResultAvalible(playerId);
            LOG.debug("Player_" + playerId + " step result avaliable: " + avaliable);

            if (battleEndServ.isBattleFinish(playerId)) {
                LOG.debug("Player_" + playerId + " battle end news will return ");
                if (battleEndServ.isPlayerWinner(playerId))
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                            .contentType(MediaType.TEXT_PLAIN)
                            .body("You Won!!!");
                else
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                            .contentType(MediaType.TEXT_PLAIN)
                            .body("You Lose :(");
            }
            
            if (battleService.isStepResultAvalible(playerId) || forcibly) {
                
                BattleService.ShipWrapper playerShip = battleService.getShipInBattle(playerId);
                Ship enemyShip = battleService.getEnemyShipInBattle(playerId);
                
                Map<String, Object> shipMap = new HashMap<>();
                shipMap.put("enemy_ship", enemyShip);
                shipMap.put("player_ship", playerShip);
                int distance = battleService.getDistance(playerId);
                shipMap.put("distance", distance);
    
                ObjectMapper mapper = new ObjectMapper();
                String jsonShips = mapper.writeValueAsString(shipMap);
                LOG.debug("Player_" + playerId + " ship info will return");
                
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
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/is_exit_available", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String isLeaveBattleFieldAvailable(@AuthenticationPrincipal PlayerUserDetails userDetails) throws BattleEndException {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " exit avaliable request");
        boolean exit = battleEndServ.isLeaveBattleFieldAvailable(playerId);
        LOG.debug("Player_" + playerId + " exit avaliable: " + exit);
        return String.valueOf(exit);
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/battlefield_exit", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String leaveBattleField(@AuthenticationPrincipal PlayerUserDetails userDetails) throws BattleEndException {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " exit battlefield request");
        boolean exit = battleEndServ.leaveBattleField(playerId);
        LOG.debug("Player_" + playerId + " exit battlefield : " + exit);
        return String.valueOf(exit);
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/is_enemy_leave_battlefield", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String isEnemyLeaveBattleField(@AuthenticationPrincipal PlayerUserDetails userDetails) {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " is_enemy_leave_battlefield request");
        boolean exit = battleEndServ.isEnemyLeaveBattlefield(playerId);
        LOG.debug("Player_" + playerId + " exit battlefield : " + exit);
        return String.valueOf(exit);
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/is_battle_end", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> isBattleEnd(@AuthenticationPrincipal PlayerUserDetails userDetails) throws JsonProcessingException, BattleEndException {
        BigInteger playerId = userDetails.getPlayerId();
        boolean finish = battleEndServ.isBattleFinish(playerId);
        Map<String, String> resp = new HashMap<>();
        resp.put("end", String.valueOf(finish));
        if (finish) {
            if (battleEndServ.isPlayerWinner(playerId))
                resp.put("wonText", String.valueOf("You Won!!!"));
            else
                resp.put("wonText", String.valueOf("You Lose :("));
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonShips = mapper.writeValueAsString(resp);
        
        return ResponseEntity.ok(jsonShips);
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