package com.nctc2017.services.utils;

import java.math.BigInteger;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nctc2017.bean.City;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.exception.PlayerNotFoundException;

@Component
@Scope("prototype")
public class TravelManager {
    private static final Logger LOG = Logger.getLogger(TravelManager.class);
    
    @Autowired
    private PlayerDao playerDao;
    
    private final int lvlDiff = 5;
    private final int maxTime = 300000;
    private final int minTime = 60000;
    private final long managerWakeUp = 2000;
    
    private Map<BigInteger, TravelBook> journals = new ConcurrentHashMap<BigInteger, TravelBook>();
    private Random rand = new Random(new GregorianCalendar().getTimeInMillis());
    private Thread manager;
    
    public TravelManager(){
        Runnable managerTask = new ManagerTask();
        manager = new Thread(managerTask);
        
        LOG.debug("TravelManager starting");
        manager.start();
        LOG.debug("TravelManager running");
    }
    
    @PreDestroy
    private void interruptManager() {
        manager.interrupt();
    }
    
    public boolean prepareEnemyFor(BigInteger playerId) throws PlayerNotFoundException {
        LOG.debug("Find enemy for Player_" + playerId);
        boolean isEnemyOnHorisont = false;
        TravelBook playerJornal = journals.get(playerId);
        if (playerJornal == null) {
            throw new PlayerNotFoundException("Player left a trip or has not yet begun");
        }

        if (playerJornal.isParticipated()) {
            LOG.debug("Player_" + playerId + " is participated, no enemy needed");
            return false;
        }
        int lvl = playerJornal.getPlayerLevel();
        LOG.debug("Player_" + playerId + " lvl = " + lvl);

        if (playerJornal.isFriendly()) {
            LOG.debug("Player_" + playerId + " is friendly (prepareEnemy) ");
            return false;
        }

        if (playerJornal.getEnemyId() != null) {
            LOG.debug("Player_" + playerId + " already have enemy");
            if (playerJornal.isFriendly()) {
                LOG.debug("But player_" + playerId + " friendly");
                return false;
            }
            
            return true;
        }

        for (Map.Entry<BigInteger, TravelBook> enemy : journals.entrySet()) {
            TravelBook enemyJornal = enemy.getValue();
            if (enemyJornal.getEnemyId() != null)
                continue;
            if (enemyJornal == playerJornal)
                continue;
            if (enemyJornal.isParticipated())
                continue;
            int enemyLvl = enemyJornal.getPlayerLevel();

            if (Math.abs(lvl - enemyLvl) <= lvlDiff) {
                playerJornal.setEnemyId(enemy.getKey());
                enemyJornal.setEnemyId(playerId);
                LOG.debug("Enemy for Player_" + playerId + " found - Player_" + playerJornal.getEnemyId());
                playerJornal.pause();
                enemyJornal.pause();

                playerJornal.setDecisionMade(false);
                isEnemyOnHorisont = true;
                break;
            }
        }
        return isEnemyOnHorisont;
    }
    
    public void startJourney(BigInteger playerId, int lvl, BigInteger city) {
        TravelBook cityTime = journals.get(playerId);
        GregorianCalendar clock = new GregorianCalendar();
        long timeNow = clock.getTimeInMillis();
        long timeToArrival;
        if (cityTime != null) {
            timeToArrival = cityTime.getTime() - timeNow;
            throw new IllegalAccessError("Player already in travel to city with id = " 
                    + cityTime.getCityId() + ". Time to arrival: " 
                    + timeToArrival/60000 + " min");
        }
        
        timeToArrival = timeNow + minTime + rand.nextInt(maxTime - minTime);
        cityTime = new TravelBook(city, timeToArrival, lvl);
        journals.put(playerId, cityTime);
    }

    public int getRelocateTime(BigInteger playerId) {
        TravelBook travelBook = journals.get(playerId);
        if (travelBook == null) return Integer.MIN_VALUE;
        
        long timeToArrival = travelBook.getTime();
        GregorianCalendar clock = new GregorianCalendar();
        long timeNow = clock.getTimeInMillis();
        return (int) (timeToArrival - timeNow) / 1000;
    }
    
    public BigInteger getEnemyId(BigInteger playerId) {
        return journals.get(playerId).getEnemyId();
    }

    public void friendly(BigInteger playerId) throws PlayerNotFoundException {
        LOG.debug("Player_" + playerId + " .friendly() ");
        TravelBook playerBook = getPlayersJournal(playerId);
        
        BigInteger enemyId = playerBook.getEnemyId();
        playerBook.resume();
        if (enemyId == null) {
            RuntimeException ex = 
                    new IllegalStateException(".friendly() was called when no enemy was written down in travel book.");
            LOG.warn("Player_" + playerId + " no enemy found ", ex);
            throw ex;
        }
        
        TravelBook enemyBook = journals.get(enemyId);
        if (enemyBook == null) {
            PlayerNotFoundException ex = new PlayerNotFoundException("Player_" + enemyId + " already left travel.");
            LOG.warn("Player_" + playerId + " enemy not found", ex);
            throw ex;
        }

        if (enemyBook.isFriendly()) {
            LOG.debug("Player_" + playerId + " and Player_" + enemyId + " - Both Friendly");
            playerBook.setEnemyId(null);
            playerBook.setFriendly(false);
            playerBook.setDecisionMade(false);
            
            enemyBook.setEnemyId(null);
            enemyBook.setFriendly(false);
            enemyBook.setDecisionMade(false);
        } else {
            playerBook.setFriendly(true);
            LOG.debug("Player_" + playerId + " is friendly now.");
        }
    }
    
    public int continueTravel(BigInteger playerId) throws PlayerNotFoundException {
        TravelBook playerBook = getPlayersJournal(playerId);
        playerBook.resume();
        GregorianCalendar clock = new GregorianCalendar();
        long now = clock.getTimeInMillis();
        return (int) (now - playerBook.getTime());
    }

    public BigInteger getRelocationCity(BigInteger playerId) throws PlayerNotFoundException {
        LOG.debug("Player_" + playerId + " getting his relocation city ");
        return getPlayersJournal(playerId).getCityId();
    }
    
    public void decisionWasMade(BigInteger playerId) throws PlayerNotFoundException {
        TravelBook playerBook = getPlayersJournal(playerId);
        playerBook.setDecisionMade(true);
    }
    
    public boolean isDecisionWasMade(BigInteger playerId) throws PlayerNotFoundException {
        TravelBook playerBook = getPlayersJournal(playerId);
        return playerBook.isDecisionWasMade();
    }

    public void setParticipated(BigInteger playerId) throws PlayerNotFoundException {
        TravelBook playerBook = getPlayersJournal(playerId);
        playerBook.setParticipated(true);
    }
    
    public boolean isPlayerInTravel(BigInteger playerId) {
        return journals.get(playerId) != null;
    }

    public boolean isParticipated(BigInteger playerId) throws PlayerNotFoundException {
        return getPlayersJournal(playerId).isParticipated();
    }
    
    private TravelBook getPlayersJournal(BigInteger playerId) throws PlayerNotFoundException {
        if (journals.get(playerId) == null) {
            PlayerNotFoundException ex = new PlayerNotFoundException("May be player already arrived");
            LOG.warn("Player_" + playerId + " not found in trip", ex);
            throw ex;
        }
        return journals.get(playerId);
    }
    
    private class TravelBook {
        
        private BigInteger cityId;
        private long arrivalTime;
        private int lvl;
        private BigInteger enemyId;
        private boolean pause;
        private long pauseTime;
        private boolean friendly = false;
        private boolean decisionMade = false;
        private boolean participated = false;
        
        public TravelBook(BigInteger cityId, Long time, int lvl) {
            this.cityId = cityId;
            this.arrivalTime = time;
            this.lvl = lvl;
            this.enemyId = null;
            this.pause = false;
            this.pauseTime = 0L;
        }
        
        public boolean isParticipated() {
            return participated;
        }

        public void setParticipated(boolean participated) {
            this.enemyId = null;
            this.decisionMade = false;
            this.participated = participated;
        }

        public void setDecisionMade(boolean made) {
            decisionMade = made;
        }
        
        public boolean isDecisionWasMade() {
            return decisionMade;
        }

        public void pause() {
            if (this.pause) {
                LOG.debug("Pause already on");
                return;
            }
            this.pause = true;
            GregorianCalendar clock = new GregorianCalendar();
            pauseTime = clock.getTimeInMillis();
        }
        
        public void resume() {
            if (!this.pause) {
                LOG.debug("Pause already off");
                return;
            }
            GregorianCalendar clock = new GregorianCalendar();
            long now = clock.getTimeInMillis();
            arrivalTime = now + (arrivalTime - pauseTime);
            pauseTime = 0L;
            this.pause = false;
        }
        public BigInteger getCityId() {
            return cityId;
        }
        
        public Long getTime() {
            if (pause) {
                GregorianCalendar clock = new GregorianCalendar();
                long now = clock.getTimeInMillis();
                return now + (arrivalTime - pauseTime);
            } else {
                return arrivalTime;
            }
        }
        
        public int getPlayerLevel() {
            return lvl;
        }

        public BigInteger getEnemyId() {
            return enemyId;
        }

        public void setEnemyId(BigInteger enemyId) {
            this.enemyId = enemyId;
        }

        public boolean isFriendly() {
            return friendly;
        }

        public void setFriendly(boolean friendly) {
            this.friendly = friendly;
        }
        
    }
    
    private class ManagerTask implements Runnable{
        
        @Override
        public void run() {
            while (true) {
                GregorianCalendar clock = new GregorianCalendar();
                long now = clock.getTimeInMillis();
                
                Iterator<Entry<BigInteger, TravelBook>> mapIterator = journals.entrySet().iterator();
                Map.Entry<BigInteger, TravelBook> player;
                
                while (mapIterator.hasNext()) {
                    player = mapIterator.next();
                    TravelBook travelBook = player.getValue();
                    long timeToLeft = travelBook.getTime();
                    if (now >= timeToLeft) {
                        playerDao.movePlayerToCity(player.getKey(), travelBook.getCityId());
                        BigInteger enemyId = travelBook.getEnemyId();
                        if (enemyId != null) {
                            new Thread(new EnemyJournalFixTask(enemyId, player.getKey())).start();
                        }
                        mapIterator.remove();
                    }
                }
                try {
                    LOG.trace("TravelManager sleep");
                    Thread.sleep(managerWakeUp);
                } catch (InterruptedException e) {
                    LOG.error("TravelManager was Interrupted", e);
                }
                LOG.trace("TravelManager awoke");
            }
        }
    }
    
    private class EnemyJournalFixTask implements Runnable{
        private BigInteger enemyId;
        private BigInteger playerId;
        
        public EnemyJournalFixTask(BigInteger enemyId, BigInteger playerId) {
            this.enemyId = enemyId;
            this.playerId = playerId;
        }

        @Override
        public void run() {
            TravelBook travelBook = journals.get(enemyId);
            if (travelBook == null) return;
            synchronized (travelBook) {
                if (playerId.equals(travelBook.getEnemyId())) {
                    travelBook.setEnemyId(null);
                    travelBook.setFriendly(false);
                    travelBook.setDecisionMade(false);
                }
            }
        }
        
    }

}
