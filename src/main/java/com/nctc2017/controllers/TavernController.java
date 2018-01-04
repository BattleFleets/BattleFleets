package com.nctc2017.controllers;

import java.math.BigInteger;
import java.util.*;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.services.MoneyService;
import com.nctc2017.services.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
public class TavernController {
    @Autowired
    private ShipService shipService;
    @Autowired
    private MoneyService moneyService;




    public int sailorCost;

    @Secured("ROLE_USER")
    @RequestMapping(value = "/tavern", method = RequestMethod.GET)
    public ModelAndView tavernWelcome(
            @RequestParam(value = "tavern", required = false) String city) {
        ModelAndView model = new ModelAndView();
        BigInteger id=new BigInteger("41");
        List<Ship> ships = shipService.getAllPlayerShips(id);
        int money = moneyService.getPlayersMoney(id);
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("money", money);
        model.addObject("city", city);
        model.addObject("ships", ships);
        model.setViewName("TavernView");
        return model;
    }


    /*@Secured("ROLE_USER")
    @RequestMapping(value = "/sailors", method = RequestMethod.GET)
    public ModelAndView getListOfShips(BigInteger id,@RequestParam(value = "HireSailors", required = false) String city) {
        id=new BigInteger("41");
        List<Ship> ships = shipService.getAllPlayerShips(id);
        ModelAndView model = new ModelAndView();
        model.addObject("city", city);
        model.addObject("ships", ships);
        return model;
    }*/

    public int getSailorsNumber(int id, int idHash, int idShip) {
        // TODO implement here
        return 0;
    }

    public int buySailors(int id, int idHash, int idShip) {
        // TODO implement here
        return 0;
    }

    public int getMoney(int id, int idHash) {
        // TODO implement here
        return 0;
    }

}