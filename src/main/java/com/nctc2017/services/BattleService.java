package com.nctc2017.services;

import java.math.BigInteger;
import java.sql.SQLDataException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.nctc2017.bean.Battle;
import com.nctc2017.bean.Player;
import com.nctc2017.bean.Ship;
import com.nctc2017.dao.ExecutorDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.exception.DeadEndException;
import com.nctc2017.services.utils.BattleManager;

public class BattleService {
    
    @Autowired
    protected BattleManager battles;
    
    @Autowired
    private ShipDao shipDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ExecutorDao executorDao;
    
    private Random random = new Random(System.currentTimeMillis());

    public boolean calculateDamage(List<List<Integer>> ammoCannon, BigInteger playerId) throws SQLDataException {
        Battle battle = battles.getBattle(playerId);
        BigInteger enemyShipId = battle.getEnemyShipId(playerId);
        BigInteger plyerShipId = battle.getShipId(playerId);
        return executorDao.calculateDamage(ammoCannon, plyerShipId, enemyShipId);
    }

    public void decreaseOfDistance(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        int dist = battle.getDistance();
        BigInteger shipId = battle.getShipId(playerId);
        int speed = shipDao.getSpeed(shipId);
        battle.setDistance(dist - speed / 100); // (speed *10) / 100; => 10%
    }

    public int getDistance(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        return battle.getDistance();
    }

    public Player boarding(BigInteger playerId) throws DeadEndException {
        Battle battle = battles.getBattle(playerId);
        BigInteger enemyShipId = battle.getEnemyShipId(playerId);
        BigInteger plyerShipId = battle.getShipId(playerId);
        
        int enemyCrew = shipDao.getCurrentShipSailors(enemyShipId);
        int playerCrew = shipDao.getCurrentShipSailors(plyerShipId);
        int enemyRndCoef = random.nextInt(10) - 5;
        int playerRndCoef = random.nextInt(10) - 5;
        
        int boarding = playerCrew + playerRndCoef - enemyCrew + enemyRndCoef;
        
        BigInteger winner;
        BigInteger shipWiner;
        BigInteger shipLoser;
        BigInteger enemyId = battles.getEnemyId(playerId); 
        
        if (boarding > 0) {
            winner = playerId;
            shipWiner = plyerShipId;
            shipLoser = enemyShipId;
        }
        else if (boarding < 0) {
            winner = enemyId;
            shipWiner = enemyShipId;
            shipLoser = plyerShipId;
        } else {
            throw new DeadEndException("No one won. Full mortality!");
        }
        
        executorDao.moveCargoToWinner(shipWiner, shipLoser);
        return playerDao.findPlayerById(winner);
    }

    public Player getEnemy(BigInteger playerId) {
        BigInteger enemyId = battles.getEnemyId(playerId);
        return playerDao.findPlayerById(enemyId);
    }

    public Ship getShipInBattle(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        BigInteger shipId = battle.getShipId(playerId);
        return shipDao.findShip(shipId);
    }

    public Ship getEnemyShipInBattle(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        BigInteger enemyShipId = battle.getEnemyShipId(playerId);
        return shipDao.findShip(enemyShipId);
    }

}