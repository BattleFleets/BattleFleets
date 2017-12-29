package com.nctc2017.controllers;

import java.util.*;

import com.nctc2017.bean.Ship;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TavernController {


    public int sailorCost;

    @Secured("ROLE_USER")
    @RequestMapping(value = "/tavern", method = RequestMethod.GET)
    public ModelAndView tavernWalcome(
            @RequestParam(value = "tavern", required = false) String city) {
        ModelAndView model = new ModelAndView();
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("city", city);
        model.setViewName("TavernView");
        return model;
    }

    @Secured("ROLE_USER")
    public List<Ship> getListOfShips(int id, int idHash) {
        // TODO implement here
        return null;
    }

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