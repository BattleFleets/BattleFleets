package com.nctc2017.services;

import com.nctc2017.dao.PlayerDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;

public class MoneyService {
    private static Logger log = Logger.getLogger(MoneyService.class);

     @Autowired
     PlayerDao playerDao;

     public int addMoney(BigInteger playerId, int moneyAdd) {
        int newMoney = getPlayersMoney(playerId)+moneyAdd;
        playerDao.updateMoney(playerId, newMoney);
        return newMoney;
     }

     public Integer deductMoney(BigInteger playerId, int moneyDeduct) {
         if(getPlayersMoney(playerId)>=moneyDeduct) {
             return addMoney(playerId, -moneyDeduct);
         }
         else{
             log.info("MoneyService Info not enough money while deduct money.");
             return null;
         }
     }

     public boolean isEnoughMoney(BigInteger playerId, int money) {
        return (getPlayersMoney(playerId)>=money);
     }

     public int getPlayersMoney(BigInteger playerId) {
        return playerDao.getPlayerMoney(playerId);
    }

}