package com.nctc2017.services;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Ammo;
import com.nctc2017.bean.Battle;
import com.nctc2017.bean.Player;
import com.nctc2017.bean.Ship;
import com.nctc2017.dao.AmmoDao;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.ExecutorDao;
import com.nctc2017.dao.HoldDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.dao.impl.ShipDaoImpl;
import com.nctc2017.exception.BattleEndException;
import com.nctc2017.exception.DeadEndException;
import com.nctc2017.exception.PlayerNotFoundException;
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
    @Autowired
    private CannonDao cannonDao;
    @Autowired
    private AmmoDao ammoDao;
    @Autowired
    private HoldDao holdDao;
    
    private Random random = new Random(System.currentTimeMillis());

    public void calculateDamage(int[][] ammoCannon, BigInteger playerId, BattleEndVisitor visitor) throws SQLException {
        Battle battle = battles.getBattle(playerId);
        if (battle == null) {
            RuntimeException ex = new IllegalArgumentException("Wrong playerId = " + playerId);
            LOG.error("Exception while calculation damage. Battle not found.", ex);
            throw ex;
        }
        synchronized (battle) {
            battle.setAmmoCannon(playerId, ammoCannon);
            battle.makeStep(playerId);
            LOG.debug("Player_" + playerId + " Made step");
            if (battle.wasEnemyMadeStep(playerId)) {
                LOG.debug("Player_" + playerId + " Enemy made step too");
                
                BigInteger enemyId = battle.getEnemyId(playerId);
                BigInteger enemyShipId = battle.getEnemyShipId(playerId);
                BigInteger plyerShipId = battle.getShipId(playerId);
                
                decreaseOfDistance(enemyId);
                decreaseOfDistance(playerId);
                
                LOG.debug("Player_" + playerId + " Start damage calc");
                executorDao.calculateDamage(battle.getAmmoCannon(playerId), plyerShipId, enemyShipId);
                LOG.debug("Player_" + playerId + " End damage calc");
                
                LOG.debug("Player_" + enemyId + " Start damage calc");
                executorDao.calculateDamage(battle.getAmmoCannon(enemyId), enemyShipId, plyerShipId);
                LOG.debug("Player_" + enemyId + " End damage calc");
                
                int enemyHp = shipDao.getCurrentShipHealth(enemyShipId);
                LOG.debug("Player_" + enemyId + " HP: " + enemyHp);
                int playerHp = shipDao.getCurrentShipHealth(plyerShipId);
                LOG.debug("Player_" + playerId + " HP: " + playerHp);
                
                if (enemyHp <= 0 && playerHp <=0) {
                    if (playerHp > enemyHp) {
                        defineWinner(battle, plyerShipId, enemyShipId, visitor, playerId, enemyId);
                    } else if (playerHp < enemyHp) {
                        defineWinner(battle, enemyShipId, plyerShipId, visitor, enemyId, playerId);
                    } else {
                        if (random.nextBoolean()) {
                            defineWinner(battle, plyerShipId, enemyShipId, visitor, playerId, enemyId);
                        } else {
                            defineWinner(battle, enemyShipId, plyerShipId, visitor, enemyId, playerId);
                        }
                    }
                    return;
                }
                if (enemyHp <= 0) {
                    visitor.endCaseVisit(playerDao, shipDao, plyerShipId, enemyShipId, playerId, enemyId);
                    battle.setWinner(playerId);
                } else if (playerHp <= 0) {
                    visitor.endCaseVisit(playerDao, shipDao, enemyShipId, plyerShipId, enemyId, playerId);
                    battle.setWinner(enemyId);
                } else {
                    battle.resetSteps(playerId);
                    LOG.debug("Player_" + playerId + " reset steps ");
                    return;
                }
                LOG.debug("Player_" + playerId + " reset battle ");
                battles.resetBattle(playerId);
            }
            return;
        }
    }
    
    public boolean isPlayerWinner(BigInteger playerId) {
        Battle battle = battles.getBattle(playerId);
        if (battle == null) {
            RuntimeException ex = new IllegalArgumentException("Wrong playerId = " + playerId + "or battle end");
            LOG.error("Exception while winner checking. Battle not found.", ex);
            throw ex;
        }
        return battle.isWinner(playerId);
    }
    
    public boolean isStepResultAvalible(BigInteger playerId) {       
        Battle battle = battles.getBattle(playerId);
        if (battle == null) {
            RuntimeException ex = new IllegalArgumentException("Wrong playerId = " + playerId);
            LOG.error("Exception while checking step result avalible. Battle not found.", ex);
            throw ex;
        }
        boolean shotWas = battle.wasEnemyMadeStep(playerId) || battle.wasPlayerMadeStep(playerId);
        return !shotWas;
    }
    
    public boolean isBattleFinish(BigInteger playerId) {
        return battles.getBattle(playerId).getShipId(playerId) == null;
    }
    
    public boolean isLeaveBattleFieldAvailable(BigInteger playerId) throws BattleEndException {
        Battle battle = battles.getBattle(playerId);
        if (battle == null) {
            throw new BattleEndException("Battle already ended. You automatically left.");
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
    
    private void defineWinner(Battle battle, BigInteger winnerShipId, BigInteger loserShipId, 
            BattleEndVisitor visitor, BigInteger winnerId, BigInteger loserId) {
        shipDao.updateShipHealth(winnerShipId, random.nextInt(RAPAIR_BONUS));
        visitor.endCaseVisit(playerDao, shipDao, winnerShipId, loserShipId, winnerId, loserId);
        battle.setWinner(winnerId);
    }
    
    public void setConvergaceOfDist(BigInteger playerId, boolean decision) {
        Battle battle = battles.getBattle(playerId); 
        battle.setConvergence(playerId, decision);
    }

    public void decreaseOfDistance(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        LOG.debug("Player_" + playerId + " Start decrease of distance");
        if (battle == null) {
            RuntimeException ex = new IllegalArgumentException("Wrong playerId = " + playerId);
            LOG.error("Exception while decrease of distance . Battle not found.", ex);
            throw ex;
        }
        
        synchronized (battle) {
            if (battle.isConvergence(playerId)) {
                int dist = battle.getDistance();
                LOG.debug("Player_" + playerId + " Decrease of distance. Current: " + dist);
                BigInteger shipId = battle.getShipId(playerId);
                
                int speed = shipDao.getSpeed(shipId);
                dist = dist - Math.round(speed / 10);
                if (dist < 0) dist = 0;
                
                battle.setDistance(dist); // (speed *10) / 100; => 10%
                battle.setConvergence(playerId, false);
                LOG.debug("Player_" + playerId + " Finish decreasing of distance. Current: " + dist);
            } else {
                LOG.debug("Player_" + playerId + " No decrease of distance by player decision");
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
            int winerCrew;
            int loserCrew;
            
            if (boarding > 0) {
                winner = playerId;
                loser = enemyId;
                shipWiner = plyerShipId;
                shipLoser = enemyShipId;
                winerCrew = playerCrew;
                loserCrew = enemyCrew;
            }
            else if (boarding < 0) {
                winner = enemyId;
                loser = playerId;
                shipWiner = enemyShipId;
                shipLoser = plyerShipId;
                winerCrew = enemyCrew;
                loserCrew = playerCrew;
            } else {
                shipDao.updateShipSailorsNumber(plyerShipId, 0);
                shipDao.updateShipSailorsNumber(enemyShipId, 0);
                throw new DeadEndException("No one won. Full mortality!");
            }
            int loserCrewPrec = (loserCrew * 100) / winerCrew;
            int crewDiff = winerCrew - loserCrew; 
            shipDao.updateShipSailorsNumber(shipWiner, 
                    loserCrewPrec > 100 ? 1 : random.nextInt(crewDiff));
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

    public ShipWrapper getShipInBattle(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        BigInteger shipId = battle.getShipId(playerId);
        Ship ship = shipDao.findShip(shipId);
        Map<String, String> cannons = cannonDao.getCurrentQuantity(shipId);
        BigInteger holdId = holdDao.findHold(shipId);
        List<Ammo> ammos = ammoDao.getAllAmmoFromHold(holdId);
        Map<String, Integer> ammoQuantity = new HashMap<>();
        for (Ammo ammo : ammos) {
            ammoQuantity.put(ammo.getName(), ammo.getQuantity());
        }
        return new ShipWrapper(ship, cannons, ammoQuantity);
    }

    public Ship getEnemyShipInBattle(BigInteger playerId) {
        Battle battle =battles.getBattle(playerId); 
        BigInteger enemyShipId = battle.getEnemyShipId(playerId);
        return shipDao.findShip(enemyShipId);
    }
    
    public class ShipWrapper {
        private Ship ship;
        private Map<String, String> cannons;
        private Map<String, Integer>  ammo;
        public ShipWrapper(Ship ship, Map<String, String> cannons, Map<String, Integer> ammo) {
            this.ship = ship;
            this.cannons = cannons;
            this.ammo = ammo;
        }
        
        public Ship getShip() {
            return ship;
        }
        
        public Map<String, String> getCannons() {
            return cannons;
        }

        public Map<String, Integer> getAmmo() {
            return ammo;
        }
        
    }
}