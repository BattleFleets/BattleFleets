package com.nctc2017.controllers;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.StartShipEquipment;
import com.nctc2017.bean.Thing;
import com.nctc2017.services.ShipService;
import com.nctc2017.services.ShipTradeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ShipyardController {
    private static final Logger LOG = Logger.getLogger(ShipyardController.class);

    @Autowired
    ShipService shipService;

    @Autowired
    ShipTradeService shipTradeService;


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

    /*@Secured("ROLE_USER")
    @RequestMapping(value = "/buyShip", method = RequestMethod.POST)
    public String buyShip(@RequestParam(value = "shipTemplateId", required = false)
                                    BigInteger shipTemplateId, Model model) {
        //Update TODO
        BigInteger debugPlayerId = new BigInteger("45");

        String resultOfBuing = null;
        if (shipTradeService.buyShip(debugPlayerId,shipTemplateId))
            resultOfBuing = "Congratulations! New ship is already armed.";
        else {
            resultOfBuing = "Sorry, you can not buy it";
            LOG.info("can not buy ship");
        }
        model.addAttribute("resultOfBuing",resultOfBuing);
        return "ShipyardView";
    }*/

    public void isEnoughMoneyForShip(int shipTemplateId) {
        // TODO implement here
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/buyShip", method = RequestMethod.GET)
    public String getAllShipTemplates(
            @RequestParam(value = "shipyard", required = false) String city,
            @RequestParam(value = "shipTemplateId", required = false) BigInteger shipTemplateId, Model model) {

        List<ShipTemplate> shipTemplates = shipService.getAllShipTemplates();
        List<StartShipEquipment> shipEquipments = new ArrayList<>();

        //Update cyclic queries.TODO
        for (ShipTemplate sT: shipTemplates) {
            StartShipEquipment shipEquipment = shipService.getStartShipEquipment(sT.getTemplateId());
            shipEquipments.add(shipEquipment);
        }

        model.addAttribute("city", city);
        model.addAttribute("shipTemplates", shipTemplates);
        model.addAttribute("shipEquipments",shipEquipments);

        return "ShipyardView";
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