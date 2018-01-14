package com.nctc2017.services;

import com.nctc2017.bean.Thing;
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
    private ShipDao shipDao;

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

    public List<Thing> getCargoFromHold(BigInteger playerId, BigInteger shipId) {
        BigInteger holdId = holdDao.findHold(shipId);
        List<Thing> cargoInHold = new ArrayList<>();
        cargoInHold.addAll(goodsDao.getAllGoodsFromHold(holdId));
        cargoInHold.addAll(mastDao.getShipMastsFromHold (holdId));
        cargoInHold.addAll(cannonDao.getAllCannonFromHold(holdId));
        cargoInHold.addAll(ammoDao.getAllAmmoFromHold(holdId));
        return cargoInHold;
    }

    public List<Thing> getCargoFromStock(BigInteger playerId) {
        BigInteger stockId = stockDao.findStockId(playerId);
        List<Thing> cargoInStock = new ArrayList<>();
        cargoInStock.addAll(goodsDao.getAllGoodsFromStock(stockId));
        cargoInStock.addAll(mastDao.getShipMastsFromStock(stockId));
        cargoInStock.addAll(cannonDao.getAllCannonFromStock(stockId));
        cargoInStock.addAll(ammoDao.getAllAmmoFromStock(stockId));
        return cargoInStock;
    }

    public List<Thing> getCargoFromShip(BigInteger playerId, BigInteger shipId) {
        List<Thing> shipCargo = new ArrayList<>();
        shipCargo.addAll(mastDao.getShipMastsFromShip(shipId));
        shipCargo.addAll(cannonDao.getAllCannonFromShip(shipId));
        return shipCargo;
    }

}