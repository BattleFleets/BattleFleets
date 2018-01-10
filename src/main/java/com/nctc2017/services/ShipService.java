package com.nctc2017.services;

import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.bean.StartShipEquipment;
import com.nctc2017.bean.StartTypeOfShipEquip;
import com.nctc2017.dao.*;
import com.nctc2017.services.utils.CompBeans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ShipService {
    @Autowired
    private ShipDao shipDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private CannonDao cannonDao;
    @Autowired
    private MastDao mastDao;
    @Autowired
    private HoldDao holdDao;

    public List<ShipTemplate> getAllShipTemplates() {
        List<ShipTemplate> result = shipDao.findAllShipTemplates();
        result.sort(new CompBeans().new ShipTemplateCompare());
        return result;
    }


    public List<Ship> getAllPlayerShips(BigInteger playerId) {
         List<BigInteger> shipsId=playerDao.findAllShip(playerId);
         List<Ship> ships=shipDao.findAllShips(shipsId);
         return ships;
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


    public int getSailorCost(){
        return shipDao.getSailorCost();
    }

    public Ship findShip(BigInteger shipId){
        return shipDao.findShip(shipId);
    }

    public boolean updateShipSailorsNumber(BigInteger shipId, int newSailorsNumber){
        return shipDao.updateShipSailorsNumber(shipId, newSailorsNumber);
    }

    public List<StartShipEquipment> getStartShipEquipment() {
        List<StartShipEquipment> result = shipDao.findStartShipsEqup();
        result.sort(new CompBeans().new StartShipEquipCompare());
        return result;
    }


    public BigInteger createNewShip(BigInteger templateId, BigInteger playerId){
        BigInteger shipId = shipDao.createNewShip(templateId, playerId);
        StartShipEquipment startShipEquipment = shipDao.findStartShipEquip(templateId);
        for(int i = 0; i < startShipEquipment.getStartNumCannon(); i++){
            cannonDao.createCannon(startShipEquipment.getStartCannonType(), shipId);
        }
        for(int i = 0; i < startShipEquipment.getStartNumMast(); i++){
            mastDao.createNewMast(startShipEquipment.getStartMastType(), shipId);
        }
        holdDao.createHold(shipId);
        return shipId;
    }



}