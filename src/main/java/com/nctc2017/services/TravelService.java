package com.nctc2017.services;

import java.math.BigInteger;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.services.utils.TravelManager;

@Service
public class TravelService {
    
    @Autowired
    TravelManager travelManager;
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private CityDao cityDao;

    public void relocate(BigInteger playerId, BigInteger cityId) {
        Player player = playerDao.findPlayerById(playerId);
        travelManager.startJourney(playerId, player.getLevel().intValueExact(), cityId);
    }

    public City getCurrentCity(BigInteger playerId) {
        City city = playerDao.getPlayerCity(playerId);
        return city;
    }

    public List<City> getCities() {
        List<City> allCity = cityDao.findAll();
        return allCity;
    }

    public int getRelocateTime(BigInteger playerId) {
        return travelManager.getRelocateTime(playerId);
    }

    public boolean isEnemyOnHorizon(BigInteger playerId) {
        return travelManager.prepareEnemyFor(playerId);
    }

    public int pauseRelocateTime(int playerId) {
        // TODO implement here
        return 0;
    }

    public int resumeRelocateTime(int playerId) {
        // TODO implement here
        return 0;
    }

    public void confirmAttack(int playerId, boolean decision) {
        // TODO implement here
    }

    public int isBattleStart(int playerId) {
        // TODO implement here
        return 0;
    }

    public int autoDecisionTimer(int playerId) {
        // TODO implement here
        return 0;
    }

}