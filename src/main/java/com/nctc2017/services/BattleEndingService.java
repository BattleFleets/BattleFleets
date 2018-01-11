package com.nctc2017.services;

import java.math.BigInteger;
import java.util.*;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Battle;
import com.nctc2017.dao.ExecutorDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.exception.BattleEndException;
import com.nctc2017.exception.PlayerNotFoundException;
import com.nctc2017.services.utils.BattleManager;

@Service
@Transactional
public class BattleEndingService {
    private static final Logger LOG = Logger.getLogger(BattleEndingService.class);
    private static final int PAY_OFF_COEF = 500;
    @Autowired
    private ShipDao shipDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ExecutorDao executorDao;
    @Autowired
    private BattlePreparationService prepService;
    @Autowired
    private TravelService travelService;
    
    @Autowired
    protected BattleManager battles;

    public String passCargoToWinnerAfterBoarding(BigInteger shipWinnerId, BigInteger shipLoserId) {
        return executorDao.moveCargoToWinner(shipWinnerId, shipLoserId);
    }

    public String passDestroyGoodsToWinner(BigInteger shipWinnerId, BigInteger shipLoserId) {
        return executorDao.moveCargoToWinner(shipWinnerId, shipLoserId);
    }

    public boolean destroyShip(BigInteger shipId) {
        return shipDao.deleteShip(shipId);
    }

    public String passSurrenderGoodsToWinner(BigInteger playerId) {
        // TODO implement here
        return null;
    }
    
    private int getPayOff(int enemyLvl) {
        return enemyLvl * PAY_OFF_COEF;
    }

    public int getPayOffPrice(BigInteger playerId) {
        BigInteger enemyId = battles.getEnemyId(playerId);
        int enemyLvl = playerDao.getPlayerLevel(enemyId);
        int payOff = getPayOff(enemyLvl);
        return payOff;
    }
    
    public int passPayOffToEnemy(BigInteger playerId) {
        BigInteger enemyId = battles.getEnemyId(playerId);
        int playerMoney = playerDao.getPlayerMoney(playerId);
        int enemyLvl = playerDao.getPlayerLevel(enemyId);
        int payOff = getPayOff(enemyLvl);
        if (payOff <= playerMoney) {
            playerDao.updateMoney(enemyId, enemyLvl * PAY_OFF_COEF);
        }
        return payOff;
    }
    
    public boolean isBattleFinish(BigInteger playerId) throws BattleEndException {
        return battles.getBattle(playerId).getShipId(playerId) == null;
    }
    
    public boolean isLeaveBattleFieldAvailable(BigInteger playerId) throws BattleEndException {
        Battle battle = battles.getBattle(playerId);
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
            prepService.stopAutoChooseTimer(enemyId);
            try {
                int time = travelService.resumeRelocateTime(enemyId);
                LOG.debug("Player_" + playerId + " continue travel for enemy player_" + enemyId
                        + ". Time left: " + time);
            } catch (PlayerNotFoundException e) {
                LOG.warn("try resume relocate time for enemy fail when leaving battle field", e);
            }
        }
        prepService.stopAutoChooseTimer(playerId);
        try {
            int time = travelService.resumeRelocateTime(playerId);
            LOG.debug("Player_" + playerId + " continue travel"
                    + ". Time left: " + time);
        } catch (PlayerNotFoundException e) {
            return true;
        }
        return true;
    }
    
    public boolean isPlayerWinner(BigInteger playerId) throws BattleEndException {
        Battle battle = battles.getBattle(playerId);

        return battle.isWinner(playerId);
    }
    
    public String getWinnerMessage(BigInteger playerId) throws BattleEndException {
        Battle battle = battles.getBattle(playerId);

        return battle.getWinMessage(playerId);
    }
    
    public boolean isEnemyLeaveBattlefield(BigInteger playerId) {
        BigInteger enemy = battles.getEnemyId(playerId);
        return enemy == null;
    }
}