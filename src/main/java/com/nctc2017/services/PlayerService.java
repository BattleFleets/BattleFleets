package com.nctc2017.services;

import com.nctc2017.bean.Player;
import com.nctc2017.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class PlayerService {

    @Autowired
    private PlayerDao playerDao;

    public Player findPlayer(BigInteger playerId){
        return playerDao.findPlayerById(playerId);
    }
}
