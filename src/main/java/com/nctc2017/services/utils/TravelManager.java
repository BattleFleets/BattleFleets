package com.nctc2017.services.utils;

import java.math.BigInteger;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nctc2017.dao.PlayerDao;

@Component
public class TravelManager {
    private static Logger log = Logger.getLogger(TravelManager.class);
    
    @Autowired
    private PlayerDao playerDao;
    
    private final int lvlDiff = 5;
    private final int maxTime = 300000;
    private final int minTime = 60000;
    private final long managerWakeUp = 1000;
    
    private Map<BigInteger, TravelBook> journals = Collections.synchronizedMap(new HashMap<BigInteger, TravelBook>());
    private GregorianCalendar clock = new GregorianCalendar();
    private Random rand = new Random(clock.getTimeInMillis());
    
    public TravelManager(){
        Runnable managerTask = new ManagerTask();
        Thread manager = new Thread(managerTask);
        
        log.log(Level.DEBUG, "TravelManager starting");
        manager.start();
        log.log(Level.DEBUG, "TravelManager running");
    }
    
    public boolean prepareEnemyFor(BigInteger playerId) {
        boolean isEnemyOnHorisont = false;
        synchronized (journals) {
            TravelBook playerJornal = journals.get(playerId);
            int lvl = playerJornal.getPlayerLevel();
            
            for (Map.Entry<BigInteger, TravelBook> enemy: journals.entrySet()) {
                TravelBook enemyJornal = enemy.getValue();
                if (enemyJornal.getEnemyId() != null) continue;
                if (enemyJornal == playerJornal) continue;
                int enemyLvl = enemyJornal.getPlayerLevel();
                
                if (Math.abs(lvl - enemyLvl) <= lvlDiff) {
                    playerJornal.setEnemyId(enemy.getKey());
                    enemyJornal.setEnemyId(playerId);
                    
                    playerJornal.pause();
                    enemyJornal.pause();
                    
                    isEnemyOnHorisont = true;
                    break;
                }
            }
        }
        return isEnemyOnHorisont;
    }
    
    public void startJourney(BigInteger playerId, int lvl, BigInteger city) {
        TravelBook cityTime = journals.get(playerId);
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
        TravelBook cityTime = journals.get(playerId);
        if (cityTime == null) return 0;
        
        long timeToArrival = cityTime.getTime();
        long timeNow = clock.getTimeInMillis();
        return (int) (timeToArrival - timeNow) / 1000;
    }
    
    public BigInteger getEnemyId(BigInteger playerId) {
        return journals.get(playerId).getEnemyId();
    }

    public void friendly(BigInteger playerId) {
        TravelBook playerBook = journals.get(playerId);
        BigInteger enemyId = journals.get(playerId).getEnemyId();
        TravelBook enemyBook = journals.get(enemyId);
        
        if (enemyBook.isFriendly()) {
            playerBook.setEnemyId(null);
            playerBook.setFriendly(false);
            playerBook.resume();
            
            enemyBook.setEnemyId(null);
            enemyBook.setFriendly(false);
            enemyBook.resume();
        } else {
            playerBook.setFriendly(true);
        }
    }
    
    public int continueTravel(BigInteger playerId) {
        TravelBook playerBook = journals.get(playerId);
        playerBook.resume();
        long now = clock.getTimeInMillis();
        return (int) (now - playerBook.getTime());
    }
    
    private class TravelBook {
        
        private BigInteger cityId;
        private long arrivalTime;
        private int lvl;
        private BigInteger enemyId;
        private boolean pause;
        private long pauseTime;
        private boolean friendly = false;
        
        public TravelBook(BigInteger cityId, Long time, int lvl) {
            this.cityId = cityId;
            this.arrivalTime = time;
            this.lvl = lvl;
            this.enemyId = null;
            this.pause = false;
            this.pauseTime = 0L;
        }
        
        public void pause() {
            if (this.pause) {
                log.debug("Pause already on");
                return;
            }
            this.pause = true;
            pauseTime = clock.getTimeInMillis();
        }
        
        public void resume() {
            if (!this.pause) {
                log.debug("Pause already off");
                return;
            }
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
                synchronized (journals) {
                    long now = clock.getTimeInMillis();
                    
                    for (Map.Entry<BigInteger, TravelBook> player: journals.entrySet()) {
                        TravelBook cityTime = player.getValue();
                        long timeToLeft = cityTime.getTime();
                        if (now >= timeToLeft) {
                            playerDao.movePlayerToCity(player.getKey(), cityTime.getCityId());
                            journals.remove(player.getKey());
                        }
                    }
                }
                
                try {
                    log.log(Level.DEBUG, "TravelManager sleep");
                    this.wait(managerWakeUp);
                } catch (InterruptedException e) {
                    log.log(Level.ERROR, "TravelManager was Interrupted", e);
                    log.log(Level.DEBUG, "TravelManager will start again");
                    Thread manager = new Thread(this);
                    log.log(Level.DEBUG, "TravelManager starting");
                    manager.start();
                    log.log(Level.DEBUG, "TravelManager running");
                }
                log.log(Level.DEBUG, "TravelManager awoke");
            }
        }
    }
    
}
