package com.nctc2017.controllers;

import java.util.*;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.Thing;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShipyardController {
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/shipyard", method = RequestMethod.GET)
    public ModelAndView shipyardWalcome(
            @RequestParam(value = "shipyard", required = false) String city) {
        ModelAndView model = new ModelAndView();
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("city", city);
        model.setViewName("ShipyardView");
        return model;
    }

    @Secured("ROLE_USER")
    public void buyShip(int playerId, int shipTemplateId) {
        // TODO implement here
    }

    public void isEnoughMoneyForShip(int shipTemplateId) {
        // TODO implement here
    }

    public List<ShipTemplate> getAllShipTemplates() {
        // TODO implement here
        return null;
    }

    public List<Ship> getAllPlayerShips(int playerId) {
        // TODO implement here
        return null;
    }

    public void sellShip(int shipId, int playerId) {
        // TODO implement here
    }

    public List<Thing> getCargoFromHold(int playerId, int shipId) {
        // TODO implement here
        return null;
    }

    public List<Thing> getCargoFromStock(int playerId) {
        // TODO implement here
        return null;
    }

    public List<Thing> getCargoFromShip(int playerId, int shipId) {
        // TODO implement here
        return null;
    }

    public void moveCargoTo(int cargoId, int destinationId, int quantity) {
        // TODO implement here
    }

    public void repairShip(int shipId, int playerId) {
        // TODO implement here
    }

}