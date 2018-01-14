package com.nctc2017.controllers;

import com.nctc2017.bean.PlayerUserDetails;
import com.nctc2017.dao.ScoreDao;
import com.nctc2017.services.LevelUpService;
import com.nctc2017.services.MoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;

import static java.lang.Integer.parseInt;

@Controller
public class UpdateController {

    @Autowired
    private LevelUpService lvlUpService;

    @Secured("ROLE_USER")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(@AuthenticationPrincipal PlayerUserDetails userDetails){
        ModelAndView model = new ModelAndView();
        model.setViewName("UpdateView");
        BigInteger playerId = userDetails.getPlayerId();
        int level = lvlUpService.getCurrentLevel(playerId);
        model.addObject("level", level);
        return model;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/incomeUp", method = RequestMethod.GET)
    @ResponseBody
    public String[] incomeUp(@AuthenticationPrincipal PlayerUserDetails userDetails) {
        lvlUpService.incomeUp(userDetails.getPlayerId());
        lvlUpService.updateNxtLvl(userDetails.getPlayerId());
        int curIncome = lvlUpService.getPassiveIncome(userDetails.getPlayerId());
        int nxtLvlImprove = lvlUpService.getNextLevel(userDetails.getPlayerId());
        String[] results = new String[3];
        results[0] = "Your income grew";
        results[1] = String.valueOf(curIncome);
        results[2] = String.valueOf(nxtLvlImprove);
        return results;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/shipUp", method = RequestMethod.GET)
    @ResponseBody
    public String[] shipUp(@AuthenticationPrincipal PlayerUserDetails userDetails) {
        lvlUpService.shipUp(userDetails.getPlayerId());
        lvlUpService.updateNxtLvl(userDetails.getPlayerId());
        int curMaxShips = lvlUpService.getMaxShips(userDetails.getPlayerId());
        int nxtLvlImprove = lvlUpService.getNextLevel(userDetails.getPlayerId());
        String[] results = new String[3];
        results[0] = "Your max number of ships grew";
        results[1] = String.valueOf(curMaxShips);
        results[2] = String.valueOf(nxtLvlImprove);
        return results;
    }
}

