package com.nctc2017.services;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.StartShipEquipment;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;


public class ShipService {
    @Autowired
    ShipDao shipDao;
    @Autowired
    PlayerDao playerDao;



    public List<ShipTemplate> getAllShipTemplates() {
        return shipDao.findAllShipTemplates();
    }

    public List<Ship> getAllPlayerShips(BigInteger playerId) {
         List<BigInteger> shipsId=playerDao.findAllShip(playerId);
         List<Ship> ships=shipDao.findAllShips(shipsId);
         return ships;
    }

    public StartShipEquipment getStartShipEquipment(BigInteger shipTemplateId) {
        return shipDao.findStartShipEquip(shipTemplateId);
    }

    public int getSailorCost(){
        return shipDao.getSailorCost();
    }

    public Ship findShip(BigInteger shipId){
        return shipDao.findShip(shipId);
    }

    public boolean updateShipSailorsNumber(BigInteger shipId, int newSailorsNumber){
        return shipDao.updateShipSailorsNumber(shipId, newSailorsNumber);
    }

}