package com.nctc2017.services;

import com.nctc2017.dao.PlayerDao;
import com.nctc2017.exception.MoneyLackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.log4j.Logger;

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

     public int deductMoney(BigInteger playerId, int moneyDeduct) {
         if(getPlayersMoney(playerId)>=moneyDeduct) {
             return addMoney(playerId, -moneyDeduct);
         }
         else{
             MoneyLackException ex = new MoneyLackException("Not enough money");
             log.error("MoneyService Exception while deduct money.", ex);
             throw ex;
         }
     }

     public boolean isEnoughMoney(BigInteger playerId, int money) {
        return (getPlayersMoney(playerId)>=money);
     }

     public int getPlayersMoney(BigInteger playerId) {
        return playerDao.getPlayerMoney(playerId);
    }

}