package com.nctc2017.services;

import java.math.BigInteger;
import java.rmi.UnexpectedException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.*;

import javax.validation.UnexpectedTypeException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Battle;
import com.nctc2017.bean.Player;
import com.nctc2017.bean.Ship;
import com.nctc2017.dao.ExecutorDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.dao.impl.ShipDaoImpl;
import com.nctc2017.exception.BattleEndException;
import com.nctc2017.exception.DeadEndException;
import com.nctc2017.services.utils.BattleEndVisitor;
import com.nctc2017.services.utils.BattleManager;

@Service
@Transactional
public class BattleService {
    private static final Logger LOG = Logger.getLogger(ShipDaoImpl.class);
    private static final int RAPAIR_BONUS = 1;
    
    @Autowired
    protected BattleManager battles;
    @Autowired
    protected BattleEndingService battleEnd;
    
    @Autowired
    private ShipDao shipDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ExecutorDao executorDao;
    
    private Random random = new Random(System.currentTimeMillis());

    public void calculateDamage(int[][] ammoCannon, BigInteger playerId, BattleEndVisitor visitor) throws SQLException {
        Battle battle = battles.getBattle(playerId);
        if (battle == null) {
            RuntimeException ex = new IllegalArgumentException("Wrong playerId = " + playerId);
            LOG.error("Exception while calculation damage.", ex);
            throw ex;
        }
        synchronized (battle) {
            BigInteger enemyId = battle.getEnemyId(playerId);
            BigInteger enemyShipId = battle.getEnemyShipId(playerId);
            BigInteger plyerShipId = battle.getShipId(playerId);
            
            executorDao.calculateDamage(ammoCannon, plyerShipId, enemyShipId);
            battle.makeStep(playerId);
            if (battle.wasEnemyMadeStep(playerId)) {
                int enemyHp = shipDao.getCurrentShipHealth(enemyShipId);
                int playerHp = shipDao.getCurrentShipHealth(plyerShipId);
                
                if (enemyHp <= 0 && playerHp <=0) {
                    if (playerHp > enemyHp) {
                        defineWinner(plyerShipId, enemyShipId, visitor, playerId, enemyId);
                    } else if (playerHp < enemyHp) {
                        defineWinner(enemyShipId, plyerShipId, visitor, enemyId, playerId);
                    } else {
                        if (random.nextBoolean()) {
                            defineWinner(plyerShipId, enemyShipId, visitor, playerId, enemyId);
                        } else {
                            defineWinner(enemyShipId, plyerShipId, visitor, enemyId, playerId);
                        }
                    }
                    return;
                }
                if (enemyHp <= 0) {
                    visitor.endCaseVisit(playerDao, shipDao, plyerShipId, enemyShipId, playerId, enemyId);
                } else if (playerHp <= 0) {
                    visitor.endCaseVisit(playerDao, shipDao, enemyShipId, plyerShipId, enemyId, playerId);
                } else {
                    battle.resetSteps(playerId);
                    return;
                }
                battles.resetBattle(playerId);
            }
            return;
        }
    }
    
    public boolean isBattleFinish(BigInteger playerId) {
        return battles.getBattle(playerId).getShipId(playerId) == null;
    }
    
    public boolean isLeaveBattleFieldAvailable(BigInteger playerId) throws BattleEndException {
        Battle battle = battles.getBattle(playerId);
        if (battle == null) {
            throw new BattleEndException("Battle already end. You automatically left.");
        }
        List<BigInteger> playerShipsLeft = battle.getShipsLeftBattle(playerId);
        BigInteger enemyId = battle.getEnemyId(playerId);
        List<BigInteger> enemyShipsLeft = battle.getShipsLeftBattle(enemyId);
        List<BigInteger> playerFactShips = playerDao.findAllShip(playerId);
        List<BigInteger> enemyFactShips = playerDao.findAllShip(enemyId);
        return playerShipsLeft.size() >= playerFactShips.size() 
                || enemyShipsLeft.size() >= enemyFactShips.size();
    }
    
    public boolean leaveBattleField (BigInteger playerId) throws BattleEndException {
        if ( ! isLeaveBattleFieldAvailable(playerId)) return false;
        synchronized (battles) {
            BigInteger enemyId = battles.getEnemyId(playerId);
            if (! battles.endBattle(enemyId) || ! battles.endBattle(playerId))
                throw new BattleEndException("Battle already end. You automatically left.");
        }
        return true;
    }
    
    private void defineWinner(BigInteger winnerShipId, BigInteger loserShipId, 
            BattleEndVisitor visitor, BigInteger winnerId, BigInteger loserId) {
        shipDao.updateShipHealth(winnerShipId, random.nextInt(RAPAIR_BONUS));
        visitor.endCaseVisit(playerDao, shipDao, winnerShipId, loserShipId, winnerId, loserId);
    }

    public void decreaseOfDistance(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        synchronized (battle) {
            if (battle.isConvergence(playerId)) {
                int dist = battle.getDistance();
                BigInteger shipId = battle.getShipId(playerId);
                int speed = shipDao.getSpeed(shipId);
                dist = dist - Math.round(speed / 10);
                if (dist < 0) dist = 0;
                battle.setDistance(dist); // (speed *10) / 100; => 10%
            }
        }
    }

    public int getDistance(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        return battle.getDistance();
    }

    public Player boarding(BigInteger playerId, BattleEndVisitor visitor) throws DeadEndException, BattleEndException {
        Battle battle = battles.getBattle(playerId);
        BigInteger winner;
        synchronized(battle) {
            BigInteger enemyShipId = battle.getEnemyShipId(playerId);
            BigInteger plyerShipId = battle.getShipId(playerId);
            
            if(plyerShipId == null) 
                throw new BattleEndException("Battle already finish.\n"
                        + "Your crwe: " + shipDao.getCurrentShipSailors(plyerShipId)
                        + "Enemy crwe: " + shipDao.getCurrentShipSailors(enemyShipId));
            if(battle.getDistance() > 0) throw new IllegalStateException("Distance too big for boarding");
            
            int enemyCrew = shipDao.getCurrentShipSailors(enemyShipId);
            int playerCrew = shipDao.getCurrentShipSailors(plyerShipId);
            int enemyRndCoef = random.nextInt(10) - 5;
            int playerRndCoef = random.nextInt(10) - 5;
            
            int boarding = (playerCrew + playerRndCoef) - (enemyCrew + enemyRndCoef);
            
            BigInteger loser;
            BigInteger shipWiner;
            BigInteger shipLoser;
            BigInteger enemyId = battles.getEnemyId(playerId); 
            
            if (boarding > 0) {
                winner = playerId;
                loser = enemyId;
                shipWiner = plyerShipId;
                shipLoser = enemyShipId;
            }
            else if (boarding < 0) {
                winner = enemyId;
                loser = playerId;
                shipWiner = enemyShipId;
                shipLoser = plyerShipId;
            } else {
                shipDao.updateShipSailorsNumber(plyerShipId, 0);
                shipDao.updateShipSailorsNumber(enemyShipId, 0);
                throw new DeadEndException("No one won. Full mortality!");
            }
            shipDao.updateShipSailorsNumber(shipWiner, boarding);
            shipDao.updateShipSailorsNumber(shipLoser, 0);
            
            visitor.endCaseVisit(playerDao, shipDao, shipWiner, shipLoser, winner, loser);
            battles.resetBattle(playerId);
        }
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