package com.nctc2017.services;

import java.math.BigInteger;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.nctc2017.bean.Ship;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.services.utils.BattleManager;

public class BattlePreparationService {
    @Autowired
    protected BattleManager battles;    
    
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ShipDao shipDao;
    
    public boolean escape(int playerId) {
        // TODO implement here
        return false;
    }

    public List<Ship> getShips(BigInteger playerId) {
        List<BigInteger> listShipsId = playerDao.findAllShip(playerId);
        return shipDao.findAllShips(listShipsId);
    }

    public List<Ship> getEnemyShips(BigInteger playerId) {
        BigInteger enemyId = battles.getEnemyId(playerId);
        return getShips(enemyId);
    }

    public void chooseShip(BigInteger playerId, BigInteger shipId) {
        battles.getBattle(playerId).setShipId(playerId, shipId);
    }

    public boolean waitForEnemyReady(int playerId) {
        // TODO implement here
        return false;
    }

    public int autoChoiceShipTimer() {
        // TODO implement here
        return 0;
    }

}