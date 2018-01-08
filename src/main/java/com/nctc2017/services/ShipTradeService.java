package com.nctc2017.services;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.dao.ShipDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service("shipTradeService")
public class ShipTradeService {

    private static Logger log = Logger.getLogger(ShipTradeService.class);
    @Autowired
    MoneyService moneyService;
    @Autowired
    ShipRepairService shipRepairService;
    @Autowired
    ShipDao shipDao;


    public boolean buyShip(BigInteger playerId, BigInteger shipTemplateId) {
        try {
            ShipTemplate shipTemplate = shipDao.findShipTemplate(shipTemplateId);
            moneyService.deductMoney(playerId, shipTemplate.getCost());
            shipDao.createNewShip(shipTemplateId, playerId);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public List<Integer> getShipCosts(List<Ship> ships) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < ships.size(); i++)
            result.add(costOfShip(ships.get(i)));
        return result;
    }

    public int costOfShip(Ship shipForSelling) {
        int costOfShip = shipForSelling.getCost() - shipRepairService.countRepairCost(shipForSelling.getShipId());
        return costOfShip;
    }

    public boolean sellShip(BigInteger playerId, BigInteger shipId) {
        try {
            Ship ship = shipDao.findShip(shipId);
            int costOfShip = ship.getCost()-shipRepairService.countRepairCost(shipId);
            moneyService.addMoney(playerId, costOfShip);
            return true;
        } catch (RuntimeException e) {
            RuntimeException ex = new IllegalArgumentException("Can not sell ship with that id: " + shipId);
            log.error("ShipTradeService Exception while selling a ship", ex);
            return false;
        }

    }

}