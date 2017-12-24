package com.nctc2017.services.utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.nctc2017.bean.Battle;

@Component
public class BattleManager {
    
    private Map<BigInteger, Battle> battles = new HashMap<>();
    
    public void newBattleBetween(BigInteger pl1, BigInteger pl2) {
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

    public void endBattle(BigInteger playerId) {
        battles.remove(playerId);
    }
    
}
