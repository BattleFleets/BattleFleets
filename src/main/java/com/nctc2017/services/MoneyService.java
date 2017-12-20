package com.nctc2017.services;

import com.nctc2017.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.util.*;
public class MoneyService {
@Autowired
PlayerDao playerDao;

    public int addMoney(BigInteger playerId, int moneyAdd) {

        int newMoney=getPlayersMoney(playerId)+moneyAdd;
        playerDao.updateMoney(playerId,newMoney);
        return newMoney;
    }

    public int deductMoney(BigInteger playerId, int moneyDeduct) {
        return addMoney(playerId,-moneyDeduct);
    }

    public boolean isEnoughMoney(BigInteger playerId, int money) {
        return (getPlayersMoney(playerId)>=money);
    }

    public int getPlayersMoney(BigInteger playerId) {
        return playerDao.getPlayerMoney(playerId);
    }

}