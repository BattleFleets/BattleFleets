package com.nctc2017.services;

import com.nctc2017.bean.Mast;
import com.nctc2017.dao.MastDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

public class ShipRepairService {

    @Autowired
    PlayerDao playerDao;

    @Autowired
    ShipDao shipDao;

    @Autowired
    MastDao mastDao;

    public int countRepairCost(BigInteger shipId) {
        int speedDifference = 0;
        int repairCost = 0;
        for (Mast mast : mastDao.getShipMastsFromShip(shipId)) {
            speedDifference = speedDifference + (mast.getMaxSpeed() - mast.getCurSpeed());
        }
        repairCost = (shipDao.getShipCost(shipId) / 1000) *
                (shipDao.getHealthLimit(shipId) - shipDao.getCurrentShipHealth(shipId)) +
                speedDifference;
        return repairCost;/*((shipDao.getShipCost(shipId)/1000)
                *(shipDao.getHealthLimit(shipId)-shipDao.getCurrentShipHealth(shipId))
                +speedDifference);*/
    }

    @Transactional
    public boolean repairShip(BigInteger playerId, BigInteger shipId) {
        //updating Player money
        int updateMoney = playerDao.getPlayerMoney(playerId) - countRepairCost(shipId);
        if (updateMoney >= 0) {
            playerDao.updateMoney(playerId, updateMoney);
            //updating Ship health
            shipDao.updateShipHealth(shipId, shipDao.getHealthLimit(shipId));
            //updating Mast speed
            for (Mast mast : mastDao.getShipMastsFromShip(shipId)) {
                mastDao.updateCurMastSpeed(mast.getThingId(), mastDao.getMaxSpeed(mast.getThingId()));
            }
            return true;
        } else return false;
    }

}