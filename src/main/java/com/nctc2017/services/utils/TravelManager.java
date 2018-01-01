package com.nctc2017.services.utils;

import java.math.BigInteger;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.nctc2017.bean.City;
import com.nctc2017.dao.PlayerDao;

@Component
@Scope("prototype")
public class TravelManager {
    private static Logger log = Logger.getLogger(TravelManager.class);
    
    @Autowired
    private PlayerDao playerDao;
    
    private final int lvlDiff = 5;
    private final int maxTime = 300000;
    private final int minTime = 60000;
    private final long managerWakeUp = 1000;
    
    private Map<BigInteger, TravelBook> journals = Collections.synchronizedMap(new HashMap<BigInteger, TravelBook>());
    private Random rand = new Random(new GregorianCalendar().getTimeInMillis());
    private Thread manager;
    
    public TravelManager(){
        Runnable managerTask = new ManagerTask();
        manager = new Thread(managerTask);
        
        log.debug("TravelManager starting");
        manager.start();
        log.debug("TravelManager running");
    }
    
    @PreDestroy
    private void interruptManager() {
        manager.interrupt();
    }
    
    public boolean prepareEnemyFor(BigInteger playerId) {
        boolean isEnemyOnHorisont = false;
        synchronized (journals) {
            TravelBook playerJornal = journals.get(playerId);
            int lvl = playerJornal.getPlayerLevel();
            if (playerJornal.getEnemyId() != null) return true;
            
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
        GregorianCalendar clock = new GregorianCalendar();
        long now = clock.getTimeInMillis();
        return (int) (now - playerBook.getTime());
    }

    public BigInteger getRelocationCity(BigInteger playerId) {
        return journals.get(playerId).getCityId();
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
            GregorianCalendar clock = new GregorianCalendar();
            pauseTime = clock.getTimeInMillis();
        }
        
        public void resume() {
            if (!this.pause) {
                log.debug("Pause already off");
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
                synchronized (journals) {
                    GregorianCalendar clock = new GregorianCalendar();
                    long now = clock.getTimeInMillis();
                    
                    for (Map.Entry<BigInteger, TravelBook> player: journals.entrySet()) {
                        TravelBook travelBook = player.getValue();
                        long timeToLeft = travelBook.getTime();
                        if (now >= timeToLeft) {
                            playerDao.movePlayerToCity(player.getKey(), travelBook.getCityId());
                            journals.remove(player.getKey());
                        }
                    }
                }
                try {
                    log.debug("TravelManager sleep");
                    Thread.sleep(managerWakeUp);
                } catch (InterruptedException e) {
                    log.error("TravelManager was Interrupted", e);
                }
                log.debug("TravelManager awoke");
            }
        }
    }
    
}
