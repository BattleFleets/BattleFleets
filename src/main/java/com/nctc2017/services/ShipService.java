package com.nctc2017.services;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.StartShipEquipment;
import com.nctc2017.dao.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.List;


public class ShipService {
    @Autowired
    ShipDao shipDao;
    @Autowired
    PlayerDao playerDao;
    @Autowired
    CannonDao cannonDao;
    @Autowired
    MastDao mastDao;
    @Autowired
    HoldDao holdDao;

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

    public Ship createNewShip(BigInteger templateId, BigInteger playerId){
        BigInteger shipId = shipDao.createNewShip(templateId, playerId);
        StartShipEquipment startShipEquipment = shipDao.findStartShipEquip(templateId);
        for(int i = 0; i < startShipEquipment.getStartNumCannon(); i++){
            cannonDao.createCannon(startShipEquipment.getStartCannonType(), shipId);
        }
        for(int i = 0; i < startShipEquipment.getStartNumMast(); i++){
            mastDao.createNewMast(startShipEquipment.getStartMastType(), shipId);
        }
        holdDao.createHold(shipId);
        Ship ship = shipDao.findShip(shipId);
        return ship;
    }

}