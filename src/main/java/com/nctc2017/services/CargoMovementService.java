package com.nctc2017.services;

import com.nctc2017.bean.Ammo;
import com.nctc2017.bean.Cannon;
import com.nctc2017.bean.Goods;
import com.nctc2017.bean.PlayerGoodsForSale;
import com.nctc2017.bean.GoodsForSale.GoodsType;
import com.nctc2017.bean.Mast;
import com.nctc2017.dao.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service("cargoMovementService")
public class CargoMovementService {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private MastDao mastDao;
    @Autowired
    private AmmoDao ammoDao;
    @Autowired
    private CannonDao cannonDao;

    @Autowired
    private StockDao stockDao;
    @Autowired
    private HoldDao holdDao;

    @Autowired
    private ExecutorDao executorDao;

    public String moveCargoBetweenPlayers(BigInteger shipFromId, BigInteger shipToId) throws SQLException {
        return  executorDao.moveCargoToWinnerBoardingOSurrender(shipToId, shipFromId);

    }

    public void moveCargoTo(BigInteger cargoId, BigInteger destinationId, int quantity) {
        executorDao.moveCargoTo(cargoId, destinationId, quantity);
    }
    
    public List<PlayerGoodsForSale> getCargoFromShip(BigInteger playerId, BigInteger shipId) {

        List<Mast> masts = mastDao.getShipMastsFromShip(shipId);
        List<Cannon> cannons = cannonDao.getAllCannonFromShip(shipId);
        
        return cargoConvert(null, cannons, null, masts);
    }
    
    public List<PlayerGoodsForSale> getCargoFromHold(BigInteger playerId, BigInteger shipId) {
        BigInteger holdId = holdDao.findHold(shipId);

        List<Goods> goods = goodsDao.getAllGoodsFromHold(holdId);
        List<Cannon> cannons = cannonDao.getAllCannonFromHold(holdId);
        List<Ammo> ammos = ammoDao.getAllAmmoFromHold(holdId);
        List<Mast> masts = mastDao.getShipMastsFromHold(holdId);
        
        return cargoConvert(goods, cannons, ammos, masts);
    }
    
    public List<PlayerGoodsForSale> getCargoFromStock(BigInteger playerId) {
        BigInteger stockId = stockDao.findStockId(playerId);
        
        List<Goods> goods = goodsDao.getAllGoodsFromStock(stockId);
        List<Cannon> cannons = cannonDao.getAllCannonFromStock(stockId);
        List<Ammo> ammos = ammoDao.getAllAmmoFromStock(stockId);
        List<Mast> masts = mastDao.getShipMastsFromStock(stockId);

        return cargoConvert(goods, cannons, ammos, masts);
    }
    
    private List<PlayerGoodsForSale> cargoConvert(List<Goods> goods, 
            List<Cannon> cannons, 
            List<Ammo> ammos, 
            List<Mast> masts) {
        List<PlayerGoodsForSale> cargoInStock = new ArrayList<>();
        
        if (cannons != null) {
            for (Cannon cannon : cannons) {
                cargoInStock.add(new PlayerGoodsForSale(
                        cannon.getThingId(), 
                        cannon.getTamplateId(), 
                        cannon.getQuantity(), 
                        GoodsType.CANNON)
                    .setName(cannon.getName())
                    .setSalePrice(cannon.getCost()/2)
                    .appendDescription("damage " + cannon.getDamage())
                    .appendDescription("distance " + cannon.getDistance()));
            }
        }
        
        if (ammos != null) {
            for (Ammo ammo : ammos) {
                cargoInStock.add(new PlayerGoodsForSale(
                        ammo.getThingId(), 
                        ammo.getTamplateId(), 
                        ammo.getQuantity(), 
                        GoodsType.CANNON)
                    .setName(ammo.getName())
                    .setSalePrice(ammo.getCost()/2)
                    .appendDescription("damage type" + ammo.getDamageType())
                    .appendDescription("distance "));
            }
        }
        
        if (masts != null) {
            for (Mast mast : masts) {
                cargoInStock.add(new PlayerGoodsForSale(
                        mast.getThingId(), 
                        mast.getTamplateId(), 
                        mast.getQuantity(), 
                        GoodsType.CANNON)
                    .setName("Mast. Jast a Mast!")
                    .setSalePrice(mast.getCost()/2)
                    .appendDescription("speed " + mast.getCurSpeed() + "/" + mast.getMaxSpeed())
                    .appendDescription("Quantity " + mast.getQuantity()));
            }
        }
        
        if (goods != null) {
            for (Goods goodsInst : goods) {
                cargoInStock.add(new PlayerGoodsForSale(
                        goodsInst.getThingId(), 
                        goodsInst.getTamplateId(), 
                        goodsInst.getQuantity(), 
                        GoodsType.CANNON)
                    .setName(goodsInst.getName())
                    .setSalePrice(0)
                    .appendDescription("You can buy it and sale in another place"));
            }
        }
        
        return cargoInStock;
    }

}