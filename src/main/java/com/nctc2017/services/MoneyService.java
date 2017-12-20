package com.nctc2017.services;

import com.nctc2017.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.*;
public class MoneyService {
@Autowired
PlayerDao playerDao;

    public int addMoney(BigInteger playerId, int moneyAdd) {

        return getPlayersMoney(playerId)+moneyAdd;
    }

    public int deductMoney(BigInteger playerId, int moneyDeduct) {
        return addMoney(playerId,-moneyDeduct);
    }

    public boolean isEnoughMoney(BigInteger idPlayer, int money) {
        // TODO implement here
        return false;
    }

    public int getPlayersMoney(BigInteger playerId) {
        return playerDao.getPlayerMoney(playerId);
    }

}