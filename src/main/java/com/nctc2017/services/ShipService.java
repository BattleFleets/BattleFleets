package com.nctc2017.services;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.StartShipEquipment;
import com.nctc2017.bean.StartTypeOfShipEquip;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.services.utils.CompBeans;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;


public class ShipService {

    @Autowired
    ShipDao shipDao;
    @Autowired
    PlayerDao playerDao;


    public List<ShipTemplate> getAllShipTemplates() {
        List<ShipTemplate> result = shipDao.findAllShipTemplates();
        result.sort(new CompBeans().new ShipTemplateCompare());
        return result;
    }

    public List<Ship> getAllPlayerShips(BigInteger playerId) {
        List<BigInteger> shipsId = playerDao.findAllShip(playerId);
        List<Ship> ships = shipDao.findAllShips(shipsId);
        return ships;
    }

    public List<StartShipEquipment> getStartShipEquipment() {
        List<StartShipEquipment> result = shipDao.findStartShipsEqup();
        result.sort(new CompBeans().new StartShipEquipCompare());
        return result;
    }

    public List<StartTypeOfShipEquip> getTypeOfShipEquipment() {
        List<StartTypeOfShipEquip> result = shipDao.findStartShipsEqupMastType();
        result.sort(new CompBeans().new StartTypeCompare());
        List<StartTypeOfShipEquip> cannon = shipDao.findStartShipsEqupCannonType();
        cannon.sort(new CompBeans().new StartTypeCompare());
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setTypeCannonName(cannon.get(i).getTypeCannonName());
        }
        return result;
    }


    public int getSailorCost() {
        return shipDao.getSailorCost();
    }

    public Ship findShip(BigInteger shipId) {
        return shipDao.findShip(shipId);
    }

    public boolean updateShipSailorsNumber(BigInteger shipId, int newSailorsNumber) {
        return shipDao.updateShipSailorsNumber(shipId, newSailorsNumber);
    }


}