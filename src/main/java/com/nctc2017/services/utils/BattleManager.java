package com.nctc2017.services.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.nctc2017.bean.Battle;
import com.nctc2017.exception.BattleStartException;

@Component
public class BattleManager {
    private static final Logger LOG = Logger.getLogger(BattleManager.class);
    
    private Map<BigInteger, Battle> battles = new HashMap<>();
    
    public void newBattleBetween(BigInteger pl1, BigInteger pl2) throws BattleStartException {
        if (battles.get(pl1) != null) {
            BattleStartException ex = new BattleStartException("Player with id=" + pl1 + " already have a battle");
            LOG.warn("Players " + pl1 + " and " + pl2 + " cannon make a battlt ", ex);
            throw ex;
        }
        if (battles.get(pl2) != null) {
            BattleStartException ex = new BattleStartException("Player with id=" + pl2 + " already have a battle");
            LOG.warn("Players " + pl1 + " and " + pl2 + " cannon make a battlt ", ex);
            throw ex;
        }
        Battle newBattle = new Battle(pl1, pl2);
        battles.put(pl1, newBattle);
        battles.put(pl2, newBattle);
    }
    
    public BigInteger getEnemyId (BigInteger playerId) {
        Battle battle = battles.get(playerId);
        if (battle == null) return null;
        BigInteger enemyId = battle.getEnemyId(playerId);
        return enemyId;
    }
    
    public Battle getBattle(BigInteger playerId) {
        return battles.get(playerId);
    }

    public boolean endBattle(BigInteger playerId) {
        return battles.remove(playerId) == null ? false : true;
    }

    public void resetBattle(BigInteger playerId) {
        battles.get(playerId).resetAll();
    }

    
}
