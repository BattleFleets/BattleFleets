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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class TavernController {
    @Autowired
    private ShipService shipService;
    @Autowired
    private MoneyService moneyService;

    private ModelAndView model=new ModelAndView();


    public int sailorCost;

    @Secured("ROLE_USER")
    @RequestMapping(value = "/tavern", method = RequestMethod.GET)
    public ModelAndView tavernWelcome(
            @RequestParam(value = "tavern", required = false) String city) {
        //ModelAndView model = new ModelAndView();
        BigInteger id=new BigInteger("41");
        List<Ship> ships = shipService.getAllPlayerShips(id);
        int sailorCost = shipService.getSailorCost();
        int money = moneyService.getPlayersMoney(id);
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("money", money);
        model.addObject("city", city);
        model.addObject("ships", ships);
        model.addObject("sailorCost",sailorCost);
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

    /*public int getSailorsNumber(int id, int idHash, int idShip) {
        // TODO implement here
        return 0;
    }*/

    @Secured("ROLE_USER")
    @RequestMapping(value = "/buySailors", method = RequestMethod.POST)
    public ModelAndView buySailors(@RequestParam(value="shipId",required = false) BigInteger shipId,
                                   @RequestParam(value="num",required = false) int newSailors) {
        BigInteger id=new BigInteger("41");
        int i=0;
        Ship ship = shipService.findShip(shipId);
        int sailorCost = shipService.getSailorCost();
        int allCost = sailorCost*newSailors;
        int money = moneyService.getPlayersMoney(id);
        if(moneyService.isEnoughMoney(id, allCost)){
            while(money<allCost){
                allCost = sailorCost*(newSailors-i);
                i++;
            }
        }
        shipService.updateShipSailorsNumber(shipId, ship.getCurSailorsQuantity()+(newSailors-i));
        money = moneyService.deductMoney(id, allCost);
        List<Ship> ships = shipService.getAllPlayerShips(id);
        model.addObject("money",money);
        model.addObject("ships",ships);
        return model;
    }

    /*public int getMoney(int id, int idHash) {
        // TODO implement here
        return 0;
    }*/

}