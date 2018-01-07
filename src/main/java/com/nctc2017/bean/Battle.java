package com.nctc2017.bean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class Battle {
    private static final String WRONG_PLAYER_WITH_ID = "Wrong player with id = ";
    
    private final String errorDescription;
    
    private Logger log = Logger.getLogger(Battle.class);
    private Map<BigInteger, Participant> playerMap;
    
    protected int distance = 0;
    
    public Battle(BigInteger idPlayer1, BigInteger idPlayer2) {
        playerMap = new HashMap<>(2);
        playerMap.put(idPlayer1, new Participant(idPlayer1, idPlayer2));
        playerMap.put(idPlayer2, new Participant(idPlayer2, idPlayer1));
        this.errorDescription = "because only two player permitted with id1 = " 
                + idPlayer1 
                + " and id2 = " 
                + idPlayer2;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
            this.distance = distance;
    }
    
    public BigInteger getShipId(BigInteger playerId) {
        Participant prtn = playerMap.get(playerId);
        if (prtn != null)
            return prtn.getShipId();
        RuntimeException ex = 
                new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
        log.error("Error when getting player's ship from Battle, "
                + errorDescription, ex);
        throw ex;
    }

    public void setShipId(BigInteger playerId, BigInteger shipId) {
        Participant prtn = playerMap.get(playerId);
        if (prtn != null) {
            prtn.setShipId(shipId);
            return;
        }
        RuntimeException ex = 
                new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
        log.error("Error when setting player's ship for Battle, " 
                + errorDescription, ex);
        throw ex;
    }
    
    public boolean isEnemyReady(BigInteger playerId) {
        Participant prtn = playerMap.get(playerId);
        if (prtn != null) {
            Participant enemy = playerMap.get(prtn.getEnemyId());
            return enemy.isReady();
        }
        RuntimeException ex = 
                new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
        log.error("Error when checking player for ready to battle, " 
                + errorDescription, ex);
        throw ex;
    }
    
    public void setReady(BigInteger playerId, boolean readyPlayer) {
        Participant prtn = playerMap.get(playerId);
        if (prtn != null) {
            prtn.setReady(readyPlayer);
            return;
        }
        RuntimeException ex = 
                new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
        log.error("Error when setting ready status in Battle, " 
                + errorDescription, ex);
        throw ex;
    }

    public void setConvergence(BigInteger playerId, boolean convergence) {
        Participant prtn = playerMap.get(playerId);
        if (prtn != null) {
            prtn.setConvergence(convergence);
            return;
        }
        RuntimeException ex = 
                new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
        log.error("Error when setting convergence in Battle, " 
                + errorDescription, ex);
        throw ex;
    }

    public boolean isConvergence(BigInteger playerId) {
        Participant prtn = playerMap.get(playerId);
        if (prtn != null)
            return prtn.isConvergence();
        RuntimeException ex = 
                new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
        log.error("Error when checking convergence in Battle, " 
                + errorDescription, ex);
        throw ex;
    }

    public BigInteger getEnemyShipId(BigInteger playerId) {
        Participant prtn = playerMap.get(playerId);
        if (prtn != null) {
            Participant enemy = playerMap.get(prtn.getEnemyId());
            return enemy.getShipId();
        }
        RuntimeException ex = 
                new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
        log.error("Error when geting  enemy ship id, becouse player id wrong" 
                + errorDescription, ex);
        throw ex;
    }
    
    public void makeStep(BigInteger playerId) {
        playerMap.get(playerId).setMadeStep(true);
    }
    
    public void resetSteps(BigInteger playerId) {
        Participant prtn = playerMap.get(playerId);
        prtn.setMadeStep(false);
        playerMap.get(prtn.getEnemyId()).setMadeStep(false);
    }
    
    public boolean wasEnemyMadeStep(BigInteger playerId) {
        BigInteger enenyId = playerMap.get(playerId).getEnemyId();
        Participant eneny = playerMap.get(enenyId);
        return eneny.wasMadeStep();
    }
    
    public boolean wasPlayerMadeStep(BigInteger playerId) {
        Participant palyer = playerMap.get(playerId);
        return palyer.wasMadeStep();
    }
    
    public BigInteger getEnemyId(BigInteger playerId) {
        Participant prtn = playerMap.get(playerId);
        if (prtn != null)
            return prtn.getEnemyId();
        RuntimeException ex = 
                new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
        log.error("Error when getting enemy from Battle, " 
                + errorDescription, ex);
        throw ex;
    }
    
    public List<BigInteger> getShipsLeftBattle(BigInteger playerId) {
        Participant prtn = playerMap.get(playerId);
        if (prtn != null)
            return prtn.getShipsLeftBattle();
        RuntimeException ex = 
                new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
        log.error("Error when getting ships which left Battle, " 
                + errorDescription, ex);
        throw ex;
    }
    
    public void resetAll() {
        for (Participant element : playerMap.values()) {
            element.setConvergence(false);
            element.setMadeStep(false);
            element.setReady(false);
            element.setShipId(null);
        }
    }
    
    private class Participant {

        private BigInteger playerId;
        private BigInteger enemyId;
        private BigInteger shipId;
        private ArrayList<BigInteger> shipsLeftBattle = new ArrayList<>();
        private boolean ready;
        private boolean convergence;
        private boolean madeStep;

        public Participant(BigInteger playerId, BigInteger enemyId) {
            this.playerId = playerId;
            this.enemyId = enemyId;
            madeStep = false;
            this.ready = false;
            this.convergence = false;
        }

        public boolean wasMadeStep() {
            return madeStep;
        }

        public void setMadeStep(boolean madeStep) {
            this.madeStep = madeStep;
        }

        public BigInteger getShipId() {
            return shipId;
        }

        public void setShipId(BigInteger shipId) {
            this.shipId = shipId;
            addShipLeftBattle(shipId);
        }

        public ArrayList<BigInteger> getShipsLeftBattle() {
            return shipsLeftBattle;
        }

        private void addShipLeftBattle(BigInteger shipsLeftBattle) {
            this.shipsLeftBattle.add(shipsLeftBattle);
        }

        public BigInteger getPlayerId() {
            return playerId;
        }

        public BigInteger getEnemyId() {
            return enemyId;
        }

        public boolean isReady() {
            return ready;
        }

        public void setReady(boolean ready) {
            this.ready = ready;
        }

        public boolean isConvergence() {
            return convergence;
        }

        public void setConvergence(boolean convergence) {
            this.convergence = convergence;
        }

    }

}