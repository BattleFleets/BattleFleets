package com.nctc2017.services;

import com.nctc2017.dao.*;
import com.nctc2017.exception.MoneyLackException;
import com.nctc2017.services.utils.MarketManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service("tradeService")
public class TradeService {
    private static final Logger log = Logger.getLogger(TradeService.class);

    @Autowired
    MoneyService moneyService;

    @Autowired
    MarketManager marketManager;

    @Autowired
    PlayerDao playerDao;
    @Autowired
    StockDao stockDao;

    @Autowired
    GoodsDao goodsDao;
    @Autowired
    AmmoDao ammoDao;
    @Autowired
    CannonDao cannonDao;
    @Autowired
    MastDao mastDao;


    @Transactional
    public String buy(BigInteger playerId, BigInteger goodsTemplateId, int price, int quantity) {
        int totalCost = price * quantity;

        if (!moneyService.isEnoughMoney(playerId, totalCost)) {
            MoneyLackException e = new MoneyLackException("Not enough money to pay " + totalCost);
            log.error("TradeService Exception while buying goods", e);
            throw e;
        }
        BigInteger cityId = playerDao.getPlayerCity(playerId);
        if (!marketManager.isActualBuyingPrice(cityId, goodsTemplateId, price)) {
            RuntimeException e = new IllegalArgumentException("Not actual price for goods");
            log.error("TradeService Exception. While buying/selling");
            throw e;
        }

        switch (marketManager.findMarketByCityId(cityId).getGoodsType(goodsTemplateId)) {

            case GOODS:
                buyGoods(playerId, goodsTemplateId, price, quantity);
                break;

            case AMMO:
                buyAmmo(playerId, goodsTemplateId, quantity);
                break;

            case MAST:
                buyMast(goodsTemplateId, stockDao.findStockId(playerId), quantity);
                break;

            case CANNON:
               buyCannon(goodsTemplateId, stockDao.findStockId(playerId), quantity);
                break;
        }

        marketManager.decreaseGoodsQuantity(cityId, goodsTemplateId, quantity);
        moneyService.deductMoney(playerId, totalCost);

        return "Success!";
    }

    private void buyGoods(BigInteger playerId, BigInteger goodsTemplateId, int price, int quantity) {
        BigInteger newGoodId = goodsDao.createNewGoods(goodsTemplateId, quantity, price);
        stockDao.addCargo(newGoodId, playerId);
    }

    private void buyAmmo(BigInteger playerId, BigInteger goodsTemplateId, int quantity) {
        BigInteger newGoodId = ammoDao.createAmmo(goodsTemplateId, quantity);
        stockDao.addCargo(newGoodId, playerId);
    }

    private void buyCannon(BigInteger goodsTemplateId, BigInteger playersStock, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cannonDao.createCannon(goodsTemplateId, playersStock);
        }
    }

    private void buyMast(BigInteger goodsTemplateId, BigInteger playersStock, int quantity) {
        for (int i = 0; i < quantity; i++) {
            mastDao.createNewMast(goodsTemplateId, playersStock);
        }
    }


    public String sell(BigInteger playerId, BigInteger goodsId, int goodsTemplateId, int price, int quantity) {

        // поняла что реализацию продажи можно сделать с помощью CargoMovementService
        // решила переделать после реализации CargoMovementService

        return "";
    }

}