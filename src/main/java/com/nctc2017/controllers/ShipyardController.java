package com.nctc2017.controllers;

import com.nctc2017.bean.*;
import com.nctc2017.services.ShipRepairService;
import com.nctc2017.services.ShipService;
import com.nctc2017.services.ShipTradeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.List;

@Controller
public class ShipyardController {
    private static final Logger LOG = Logger.getLogger(ShipyardController.class);

    @Autowired
    ShipService shipService;

    @Autowired
    ShipTradeService shipTradeService;

    @Autowired
    ShipRepairService shipRepairService;


    @Secured("ROLE_USER")
    @RequestMapping(value = "/shipyard", method = RequestMethod.GET)
    public ModelAndView shipyardWelcome(
            @RequestParam(value = "shipyard", required = false) String city) {
        ModelAndView model = new ModelAndView();
        model.addObject("msg", "This is protected page - Only for Users!");
        model.addObject("city", city);
        model.setViewName("ShipyardView");
        return model;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/buy", method = RequestMethod.GET)
    @ResponseBody
    public String buyShip(@RequestParam(value = "shipTemplateId") BigInteger shipTemplateId) {
        //Update TODO
        BigInteger debugPlayerId = new BigInteger("45");
        return shipTradeService.buyShip(debugPlayerId,shipTemplateId);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/buyShip", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAllShipTemplates() {

        List<ShipTemplate> shipTemplates = shipService.getAllShipTemplates();
        List<StartShipEquipment> shipEquipments = shipService.getStartShipEquipment();
        List<StartTypeOfShipEquip> startTypeOfShipEquips = shipService.getTypeOfShipEquipment();
        ModelAndView model = new ModelAndView();
        model.addObject("startTypeOfShipEquips",startTypeOfShipEquips);
        model.addObject("shipTemplates", shipTemplates);
        model.addObject("shipEquipments",shipEquipments);
        model.setViewName("fragment/shiptable");
        return model;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/sellShip", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getAllPlayerShips() {
        //TODO debugID
        BigInteger playerId = new BigInteger("45");
        List<Ship> playerShips = shipService.getAllPlayerShips(playerId);
        List<Integer> shipCosts = shipTradeService.getShipsCost(playerShips);
        String action = "Sell";

        ModelAndView model = new ModelAndView();
        model.addObject("action",action);
        model.addObject("shipCosts",shipCosts);
        model.addObject("playerShips",playerShips);
        model.setViewName("fragment/playerships");
        return model;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/sell", method = RequestMethod.GET)
    @ResponseBody
    public boolean sellShip(@RequestParam(value = "shipId") BigInteger shipId) {
        BigInteger playerId = new BigInteger("45");
        return shipTradeService.sellShip(playerId, shipId);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/repairShip", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView repairShip() {
        BigInteger playerId = new BigInteger("45");
        List<Ship> playerShips = shipService.getAllPlayerShips(playerId);
        List<Integer> shipCosts = shipTradeService.getShipsCost(playerShips);
        List<ShipService.ShipSpeed> shipsSpeed = shipService.getSpeedShips(playerShips);
        String action = "Repair";

        ModelAndView model = new ModelAndView();
        model.addObject("action",action);
        model.addObject("shipsSpeed", shipsSpeed);
        model.addObject("shipCosts",shipCosts);
        model.addObject("playerShips",playerShips);
        model.setViewName("fragment/playerships");
        return model;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/repair", method = RequestMethod.GET)
    @ResponseBody
    public  boolean repair(@RequestParam(value = "shipId") BigInteger shipId) {
        BigInteger playerId = new BigInteger("45");
        return shipRepairService.repairShip(playerId, shipId);
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

}