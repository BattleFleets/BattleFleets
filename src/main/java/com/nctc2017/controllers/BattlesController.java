package com.nctc2017.controllers;


import java.math.BigInteger;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.bean.Ship;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.exception.BattleEndException;
import com.nctc2017.exception.DeadEndException;
import com.nctc2017.services.BattleEndingService;
import com.nctc2017.services.BattlePreparationService;
import com.nctc2017.services.BattlePreparationService.ShipWrapper;
import com.nctc2017.services.BattleService;
import com.nctc2017.services.LevelUpService;
import com.nctc2017.services.utils.BattleEndVisitor;

@Controller
public class BattlesController {
    private static final int checkingCounter = 5; 
    private static final int checkingInterval = 3000;
    private static final Logger LOG = Logger.getLogger(BattlesController.class);
    @Autowired
    private BattlePreparationService prepService;
    @Autowired
    private BattleService battleService;
    @Autowired
    private BattleEndingService battleEndServ;
    @Autowired
    private LevelUpService levelUp;
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/battle_preparing", method = RequestMethod.GET)
    public ModelAndView battleWelcome(@AuthenticationPrincipal PlayerUserDetails userDetails) {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " battle preparing request");

        List<Ship> enemyFleet;
        int time;
        
        try {
            if (battleService.isBattleStart(playerId)) {
                return new ModelAndView("redirect:/battle");
            }
            enemyFleet = prepService.getEnemyShips(playerId);
            time = prepService.autoChoiceShipTimer(playerId);
        } catch (BattleEndException e) {
            return new ModelAndView("redirect:/trip");
        }
        List<ShipWrapper> fleet = prepService.getShipsExtraInfo(playerId);
        
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
        for (int i = 0; i < checkingCounter; i++) {
            Thread.sleep(checkingInterval);
            ready = prepService.waitForEnemyReady(playerId);
            if (ready) break;
        }
        
        return String.valueOf(ready);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/battle", method = RequestMethod.GET)
    public ModelAndView getBattle(
            @AuthenticationPrincipal PlayerUserDetails userDetails) {
        BigInteger playerId = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " get battle request");
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
        LOG.debug("Player_" + playerId + " fire request with convergace dist: " + decrease);
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
        for (int i = 0; i < checkingCounter; i++) {
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
            
            if (avaliable || forcibly) {
                
                BattleService.ShipWrapper playerShip = battleService.getShipInBattle(playerId);
                Ship enemyShip = battleService.getEnemyShipInBattle(playerId);
                
                Map<String, Object> shipMap = new HashMap<>();
                shipMap.put("enemy_ship", enemyShip);
                shipMap.put("player_ship", playerShip);
                int distance = battleService.getDistance(playerId);
                shipMap.put("distance", distance);
                    
                shipMap.put("madeStep", battleService.wasPalayerMadeStep(playerId));
                
                shipMap.put("try_later", false);
                ObjectMapper mapper = new ObjectMapper();
                String jsonShips = mapper.writeValueAsString(shipMap);
                LOG.debug("Player_" + playerId + " ship info will return");
                
                return ResponseEntity.ok(jsonShips);
            } else {
                Thread.sleep(checkingInterval);
            }
        }
        
        Map<String, Object> shipMap = new HashMap<>();
        shipMap.put("try_later", true);
        ObjectMapper mapper = new ObjectMapper();
        String jsonShips = mapper.writeValueAsString(shipMap);
        return ResponseEntity.ok(jsonShips);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/boarding", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public void boarding(@AuthenticationPrincipal PlayerUserDetails userDetails) throws BattleEndException {
        BigInteger playerId  = userDetails.getPlayerId();
        LOG.debug("Player_" + playerId + " boarding request ");
        BigInteger winnerId = battleService.boarding(playerId, new DefaultBoardingBattleEnd());

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
            if (battleEndServ.isPlayerWinner(playerId)) {
                resp.put("title", String.valueOf("You Won!!!"));
                resp.put("wonText", String.valueOf(battleEndServ.getWinnerMessage(playerId)));
            } else {
                resp.put("title", String.valueOf("You Lose :("));
                resp.put("wonText", String.valueOf(battleEndServ.getWinnerMessage(playerId)));
            }
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
    
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleCustomException(RuntimeException ex) {
        LOG.error("Intternal unexpected exception. ", ex);
        ModelAndView model = new ModelAndView("/error");
        model.addObject("reason", ex.getMessage());

        return model;
    }
    
    private class DefaultDestroyBattleEnd implements BattleEndVisitor {

        @Override
        public void endCaseVisit(PlayerDao playerDao, ShipDao shipDao, BigInteger winnerShipId, BigInteger loserShipId,
                BigInteger winnerId, BigInteger loserId) {
            battleEndServ.passDestroyGoodsToWinner(winnerShipId, loserShipId);
            battleEndServ.destroyShip(loserShipId);
        }
    }
    
    private class DefaultBoardingBattleEnd implements BattleEndVisitor {

        @Override
        public void endCaseVisit(PlayerDao playerDao, ShipDao shipDao, BigInteger winnerShipId, BigInteger loserShipId,
                BigInteger winnerId, BigInteger loserId) {
            battleEndServ.passCargoToWinnerAfterBoarding(winnerShipId, loserShipId);
        }
    }

}