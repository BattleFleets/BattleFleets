package com.nctc2017.services;

import java.math.BigInteger;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Battle;
import com.nctc2017.bean.Cannon;
import com.nctc2017.bean.Ship;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.exception.BattleEndException;
import com.nctc2017.services.utils.AutoDecisionTask;
import com.nctc2017.services.utils.BattleManager;
import com.nctc2017.services.utils.Visitor;

@Service
@Transactional
public class BattlePreparationService {
    private static final int DELAY = 60000;
    
    @Autowired
    protected BattleManager battles;    
    
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ShipDao shipDao;
    @Autowired
    private CannonDao cannonDao;
    
    private Random randomShip = new Random(System.currentTimeMillis());
    private Map<BigInteger, Thread> playerChoiceShipTimer = new HashMap<>();
    
    public boolean escape(BigInteger playerId) {
        BigInteger enemyId = battles.getEnemyId(playerId);
        if (fleetSpeed(playerId) > fleetSpeed(enemyId)) {
            battles.endBattle(playerId);
            return true;
        }
        return false;
    }
    
    private int fleetSpeed(BigInteger playerId) {
        return playerDao.getFleetSpeed(playerId);
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
        Thread timer = playerChoiceShipTimer.get(playerId);
        if (timer != null && timer.isAlive()) {
            timer.interrupt();
        }
        Battle battle = battles.getBattle(playerId);
        battle.setShipId(playerId, shipId);
        List<Cannon> cannons = cannonDao.getAllCannonFromShip(shipId);
        int maxDist = 0;
        int dist;
        for (Cannon cannon : cannons) {
            dist = cannon.getDistance();
            if (maxDist < dist) {
                maxDist = dist;
            }
        }
        battle.setDistance(maxDist);
    }

    public void setReady(BigInteger playerId) {
        Battle battle = battles.getBattle(playerId);
        battle.setReady(playerId, true);
    }
    
    public boolean waitForEnemyReady(BigInteger playerId) throws BattleEndException {
        Battle battle = battles.getBattle(playerId);
        if (battle == null) {
            throw new BattleEndException("Your enemy run away.");
        }
        return battle.isEnemyReady(playerId);
    }

    public int autoChoiceShipTimer(BigInteger playerId) {
        Runnable decisionTask = new AutoDecisionTask(new ShipVisitor(playerId), DELAY);
        Thread decisionThread = new Thread(decisionTask);
        decisionThread.start();
        return 0;
    }
    
    public List<BigInteger> getShipsLeftBattle(BigInteger playerId) {
        return battles.getBattle(playerId).getShipsLeftBattle(playerId);
    }
    
    private class ShipVisitor implements Visitor{
        
        private BigInteger playerId;
        
        public ShipVisitor(BigInteger playerId) {
            this.playerId = playerId;
        }

        @Override
        public void visit() {
            List<BigInteger> ships = playerDao.findAllShip(playerId);
            ships.removeAll(getShipsLeftBattle(playerId));
            
            BigInteger shipId = ships.get(randomShip.nextInt(ships.size()));
            chooseShip(playerId, shipId);
        }
        
    }
}