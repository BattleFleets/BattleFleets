package com.nctc2017.services;

import java.math.BigInteger;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Goods;
import com.nctc2017.dao.ExecutorDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.services.utils.BattleManager;

@Service
@Transactional
public class BattleEndingService {
    private static final int PAY_OFF_COEF = 500;
    @Autowired
    private ShipDao shipDao;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ExecutorDao executorDao;
    
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

}