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
    
    private final int lvlDiff = 5;
    private final int maxTime = 300000;
    private final int minTime = 60000;
    private final long managerWakeUp = 10000;
    
    private Map<BigInteger, TravelBook> relocation = Collections.synchronizedMap(new HashMap<BigInteger, TravelBook>());
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
        synchronized (relocation) {
            TravelBook playerJornal = relocation.get(playerId);
            int lvl = playerJornal.getPlayerLevel();
            
            for (Map.Entry<BigInteger, TravelBook> enemy: relocation.entrySet()) {
                TravelBook enemyJornal = enemy.getValue();
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
        TravelBook cityTime = relocation.get(playerId);
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
        relocation.put(playerId, cityTime);
    }

    public int getRelocateTime(BigInteger playerId) {
        TravelBook cityTime = relocation.get(playerId);
        if (cityTime == null) return 0;
        
        long timeToArrival = cityTime.getTime();
        long timeNow = clock.getTimeInMillis();
        return (int) (timeToArrival - timeNow) / 1000;
    }
    
    private class TravelBook {
        
        private BigInteger cityId;
        private long arrivalTime;
        private int lvl;
        private BigInteger enemyId;
        private boolean pause;
        private long pauseTime;
        
        public TravelBook(BigInteger cityId, Long time, int lvl) {
            this.cityId = cityId;
            this.arrivalTime = time;
            this.lvl = lvl;
            this.enemyId = null;
            this.pause = false;
            this.pauseTime = 0L;
        }
        
        public void pause() {
            this.pause = true;
            pauseTime = clock.getTimeInMillis();
        }
        
        public void resume() {
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
    }
    
    private class ManagerTask implements Runnable{
        
        @Autowired
        PlayerDao playerDao;
        
        @Override
        public void run() {
            while (true) {
                synchronized (relocation) {
                    long now = clock.getTimeInMillis();
                    
                    for (Map.Entry<BigInteger, TravelBook> player: relocation.entrySet()) {
                        TravelBook cityTime = player.getValue();
                        long timeToLeft = cityTime.getTime();
                        if (now >= timeToLeft) {
                            playerDao.movePlayerToCity(player.getKey(), cityTime.getCityId());
                            relocation.remove(player.getKey());
                        }
                    }
                }
                
                try {
                    this.wait(managerWakeUp);
                } catch (InterruptedException e) {
                    log.log(Level.ERROR, "TravelManager was Interrupted", e);
                    log.log(Level.DEBUG, "TravelManager will start again");
                    Thread manager = new Thread(this);
                    log.log(Level.DEBUG, "TravelManager starting");
                    manager.start();
                    log.log(Level.DEBUG, "TravelManager running");
                }
            }
        }
    }
    
}
