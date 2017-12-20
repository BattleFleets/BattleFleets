package com.nctc2017.services.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class BattleManager {
    
    private Map<BigInteger, Battle> battles = new HashMap<>();
    
    public void newBattleBetween(BigInteger pl1, BigInteger pl2) {
        Participant player1 = new Participant(pl1);
        Participant player2 = new Participant(pl2);
        Battle newBattle = new Battle(player1, player2);
        battles.put(pl1, newBattle);
        battles.put(pl2, newBattle);
    }
    
    public BigInteger getEnemyId (BigInteger playerId) {
        Battle battle = battles.get(playerId);
        if (battle == null) return null;
        BigInteger enemyId = battle.getPlayer1().getPlayerId();
        return playerId.compareTo(enemyId) != 0 ? 
                enemyId : battle.getPlayer2().getPlayerId() ;
    }
    
    private class Battle {
        
        private Participant player1;
        private Participant player2;
        
        public Battle(Participant player1, Participant player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        public Participant getPlayer1() {
            return player1;
        }

        public Participant getPlayer2() {
            return player2;
        }
        
    }
    
    private class Participant {
        
        private BigInteger playerId;
        private BigInteger shipId;
        private ArrayList<BigInteger> shipsLeftBattle = new ArrayList<>();
        
        public Participant(BigInteger playerId) {
            this.playerId = playerId;
        }

        public BigInteger getShipId() {
            return shipId;
        }

        public void setShipId(BigInteger shipId) {
            this.shipId = shipId;
        }

        public ArrayList<BigInteger> getShipsLeftBattle() {
            return shipsLeftBattle;
        }

        public void addShipLeftBattle(BigInteger shipsLeftBattle) {
            this.shipsLeftBattle.add(shipsLeftBattle);
        }

        public BigInteger getPlayerId() {
            return playerId;
        }
        
    }
    
}
