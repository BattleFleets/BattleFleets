package com.nctc2017.bean;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Battle {
    private static final String WRONG_PLAYER_WITH_ID = "Wrong player with id = ";
    
    private final String errorDescription;
    
    private Logger log = Logger.getLogger(Battle.class);

    private Participant player1;
    private Participant player2;
    
    protected int distance;

    public Battle(BigInteger idPlayer1, BigInteger idPlayer2) {
        this.player1 = new Participant(idPlayer1);
        this.player2 = new Participant(idPlayer2);
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
        if (playerId.equals(player1.getPlayerId())) {
            return player1.getShipId();
        } else if (playerId.equals(player2.getPlayerId())) {
            return player2.getShipId();
        } else {
            RuntimeException ex = 
                    new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
            log.error("Error when getting player's ship from Battle, "
                    + errorDescription, ex);
            throw ex;
        }
    }

    public void setShipId(BigInteger playerId, BigInteger shipId) {
        if (playerId.equals(player1.getPlayerId())) {
            this.player1.setShipId(shipId);
        } else if (playerId.equals(player2.getPlayerId())) {
            this.player2.setShipId(shipId);
        } else {
            RuntimeException ex = 
                    new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
            log.error("Error when setting player's ship for Battle, "
                    + errorDescription, ex);
            throw ex;
        }
    }

    public BigInteger getIdPlayer1() {
        return player1.getPlayerId();
    }

    public BigInteger getIdPlayer2() {
        return player2.getPlayerId();
    }
    
    public boolean isEnemyReady(BigInteger playerId) {
        if (playerId.equals(player1.getPlayerId())) {
            return player2.isReady();
        } else if (playerId.equals(player2.getPlayerId())) {
            return player1.isReady();
        } else {
            RuntimeException ex = 
                    new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
            log.error("Error when checking player for ready to battle, "
                    + errorDescription, ex);
            throw ex;
        }
    }
    
    public void setReady(BigInteger playerId, boolean readyPlayer) {
        if (playerId.equals(player1.getPlayerId())) {
            player1.setReady(readyPlayer);
        } else if (playerId.equals(player2.getPlayerId())) {
            player2.setReady(readyPlayer);
        } else {
            RuntimeException ex = 
                    new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
            log.error("Error when setting ready status in Battle, "
                    + errorDescription, ex);
            throw ex;
        }
    }

    public void setConvergence(BigInteger playerId, boolean convergence) {
        if (playerId.equals(player1.getPlayerId())) {
            player1.setConvergence(convergence);
        } else if (playerId.equals(player2.getPlayerId())) {
            player2.setConvergence(convergence);
        } else {
            RuntimeException ex = 
                    new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
            log.error("Error when setting convergence in Battle, "
                    + errorDescription, ex);
            throw ex;
        }
    }

    public boolean isConvergence(BigInteger playerId) {
        if (playerId.equals(player1.getPlayerId())) {
            return player1.isConvergence();
        } else if (playerId.equals(player2.getPlayerId())) {
            return player2.isConvergence();
        } else {
            RuntimeException ex = 
                    new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
            log.error("Error when checking convergence in Battle, "
                    + errorDescription, ex);
            throw ex;
        }
    }

    public BigInteger getEnemyShipId(BigInteger playerId) {
        if (playerId.equals(player1.getPlayerId())) {
            return getShipId(player2.getPlayerId());
        } else {
            return getShipId(player1.getPlayerId());
        }
    }

    public BigInteger getEnemyId(BigInteger playerId) {
        if (playerId.equals(player1.getPlayerId())) {
            return player2.getPlayerId();
        } else if (playerId.equals(player2.getPlayerId())) {
            return player1.getPlayerId();
        } else {
            RuntimeException ex = 
                    new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
            log.error("Error when getting enemy from Battle, "
                    + errorDescription, ex);
            throw ex;
        }
    }
    
    public List<BigInteger> getShipsLeftBattle(BigInteger playerId) {
        if (playerId.equals(player1.getPlayerId())) {
            return player1.getShipsLeftBattle();
        } else if (playerId.equals(player2.getPlayerId())) {
            return player2.getShipsLeftBattle();
        } else {
            RuntimeException ex = 
                    new IllegalArgumentException(WRONG_PLAYER_WITH_ID + playerId);
            log.error("Error when getting ships which left Battle, "
                    + errorDescription, ex);
            throw ex;
        }
    }
    
    private class Participant {
        
        private BigInteger playerId;
        private BigInteger shipId;
        private ArrayList<BigInteger> shipsLeftBattle = new ArrayList<>();
        private boolean ready;
        private boolean convergence;
        
        public Participant(BigInteger playerId) {
            this.playerId = playerId;
            this.ready = false;
            this.convergence = false;
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