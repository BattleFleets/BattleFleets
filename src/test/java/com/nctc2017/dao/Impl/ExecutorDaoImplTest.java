package com.nctc2017.dao.Impl;

import com.nctc2017.bean.Goods;
import com.nctc2017.bean.Player;
import com.nctc2017.bean.Ship;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationConfig.class })
@Transactional
public class ExecutorDaoImplTest {
    @Autowired
    ShipDao shipDao;
    @Autowired
    PlayerDao playerDao;
    @Autowired
    ExecutorDao executorDao;
    @Autowired
    HoldDao holdDao;
    @Autowired
    GoodsDao goodsDao;

    @Test
    @Rollback(true)
    public void moveCargoToWinnerBoardingAllFree() throws Exception {
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player = playerDao.findPlayerByLogin("Steve");
        BigInteger myShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, player.getPlayerId());
        Ship ship = shipDao.findShip(myShipId);
        BigInteger myHoldId = holdDao.createHold(myShipId);
        BigInteger myWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,10,40);
        holdDao.addCargo(myWoodId, myHoldId);
        playerDao.addNewPlayer("Iogan","1111","Shmidt@gmail.com");
        Player enemy = playerDao.findPlayerByLogin("Iogan");
        BigInteger enemyShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, enemy.getPlayerId());
        BigInteger enemyHoldId = holdDao.createHold(enemyShipId);
        BigInteger enemyWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,10,30);
        BigInteger enemyTeaId = goodsDao.createNewGoods(DatabaseObject.TEA_TEMPLATE_ID,10,50);
        holdDao.addCargo(enemyWoodId, enemyHoldId);
        holdDao.addCargo(enemyTeaId, enemyHoldId);
        String res = executorDao.moveCargoToWinner(myShipId, enemyShipId);
        List<Goods> myGoods = goodsDao.getAllGoodsFromHold(myHoldId);
        List<Goods> enemyGoods = goodsDao.getAllGoodsFromHold(enemyHoldId);
        assertEquals(myGoods.size(),3);
        assertEquals(enemyGoods.size(), 0);
        assertEquals(res, "You received part of goods from enemy ship as a result of boarding");
    }

    @Test
    @Rollback(true)
    public void moveCargoToWinnerBoardingPartFree() throws Exception {
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player = playerDao.findPlayerByLogin("Steve");
        BigInteger myShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, player.getPlayerId());
        Ship ship = shipDao.findShip(myShipId);
        BigInteger myHoldId = holdDao.createHold(myShipId);
        BigInteger myWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,10,40);
        holdDao.addCargo(myWoodId, myHoldId);
        playerDao.addNewPlayer("Iogan","1111","Shmidt@gmail.com");
        Player enemy = playerDao.findPlayerByLogin("Iogan");
        BigInteger enemyShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, enemy.getPlayerId());
        BigInteger enemyHoldId = holdDao.createHold(enemyShipId);
        BigInteger enemyWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,10,30);
        BigInteger enemyTeaId = goodsDao.createNewGoods(DatabaseObject.TEA_TEMPLATE_ID,90,50);
        holdDao.addCargo(enemyWoodId, enemyHoldId);
        holdDao.addCargo(enemyTeaId, enemyHoldId);
        String res = executorDao.moveCargoToWinner(myShipId, enemyShipId);
        List<Goods> myGoods = goodsDao.getAllGoodsFromHold(myHoldId);
        List<Goods> enemyGoods = goodsDao.getAllGoodsFromHold(enemyHoldId);
        assertEquals(myGoods.size(),3);
        assertEquals(enemyGoods.size(), 1);
        assertEquals(holdDao.getOccupiedVolume(myShipId),100);
        assertEquals(holdDao.getOccupiedVolume(enemyShipId),10);
        assertEquals(res, "You received part of goods from enemy ship as a result of boarding");
    }

    @Test
    @Rollback(true)
    public void moveCargoToWinnerDestroy() throws Exception {
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player = playerDao.findPlayerByLogin("Steve");
        BigInteger myShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, player.getPlayerId());
        BigInteger myHoldId = holdDao.createHold(myShipId);
        BigInteger myWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,100,40);
        holdDao.addCargo(myWoodId, myHoldId);
        playerDao.addNewPlayer("Iogan","1111","Shmidt@gmail.com");
        Player enemy = playerDao.findPlayerByLogin("Iogan");
        BigInteger enemyShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, enemy.getPlayerId());
        BigInteger enemyHoldId = holdDao.createHold(enemyShipId);
        BigInteger enemyWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,80,30);
        BigInteger enemyTeaId = goodsDao.createNewGoods(DatabaseObject.TEA_TEMPLATE_ID,700,50);
        holdDao.addCargo(enemyWoodId, enemyHoldId);
        holdDao.addCargo(enemyTeaId, enemyHoldId);
        shipDao.updateShipHealth(enemyShipId,0);
        String res = executorDao.moveCargoToWinner(myShipId, enemyShipId);
        List<Goods> myGoods = goodsDao.getAllGoodsFromHold(myHoldId);
        List<Goods> enemyGoods = goodsDao.getAllGoodsFromHold(enemyHoldId);
        assertEquals(myGoods.size(),3);
        assertEquals(enemyGoods.size(), 0);
        assertEquals(res, "You received part of goods from enemy ship as a result of destruction");
    }
    @Test()
    @Rollback(true)
    public void moveCargoToWinnerWrongTwoId() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player = playerDao.findPlayerByLogin("Steve");
        BigInteger myShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, player.getPlayerId());
        BigInteger myHoldId = holdDao.createHold(myShipId);
        BigInteger myWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,100,40);
        holdDao.addCargo(myWoodId, myHoldId);
        playerDao.addNewPlayer("Iogan","1111","Shmidt@gmail.com");
        Player enemy = playerDao.findPlayerByLogin("Iogan");
        BigInteger enemyShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, enemy.getPlayerId());
        BigInteger enemyHoldId = holdDao.createHold(enemyShipId);
        BigInteger enemyWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,80,30);
        BigInteger enemyTeaId = goodsDao.createNewGoods(DatabaseObject.TEA_TEMPLATE_ID,700,50);
        holdDao.addCargo(enemyWoodId, enemyHoldId);
        holdDao.addCargo(enemyTeaId, enemyHoldId);
        String res = executorDao.moveCargoToWinner(player.getPlayerId(), enemy.getPlayerId());
        assertEquals(res, "Wrong input data");
    }
    @Test()
    @Rollback(true)
    public void moveCargoToWinneWrongWinId() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player = playerDao.findPlayerByLogin("Steve");
        BigInteger myShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, player.getPlayerId());
        BigInteger myHoldId = holdDao.createHold(myShipId);
        BigInteger myWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,100,40);
        holdDao.addCargo(myWoodId, myHoldId);
        playerDao.addNewPlayer("Iogan","1111","Shmidt@gmail.com");
        Player enemy = playerDao.findPlayerByLogin("Iogan");
        BigInteger enemyShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, enemy.getPlayerId());
        BigInteger enemyHoldId = holdDao.createHold(enemyShipId);
        BigInteger enemyWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,80,30);
        BigInteger enemyTeaId = goodsDao.createNewGoods(DatabaseObject.TEA_TEMPLATE_ID,700,50);
        holdDao.addCargo(enemyWoodId, enemyHoldId);
        holdDao.addCargo(enemyTeaId, enemyHoldId);
        String res = executorDao.moveCargoToWinner(myHoldId, enemyShipId);
        assertEquals(res, "Wrong input data");

    }

    @Test()
    @Rollback(true)
    public void moveCargoToWinneIdWrongLoseId() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player = playerDao.findPlayerByLogin("Steve");
        BigInteger myShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, player.getPlayerId());
        BigInteger myHoldId = holdDao.createHold(myShipId);
        BigInteger myWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,100,40);
        holdDao.addCargo(myWoodId, myHoldId);
        playerDao.addNewPlayer("Iogan","1111","Shmidt@gmail.com");
        Player enemy = playerDao.findPlayerByLogin("Iogan");
        BigInteger enemyShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, enemy.getPlayerId());
        BigInteger enemyHoldId = holdDao.createHold(enemyShipId);
        BigInteger enemyWoodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,80,30);
        BigInteger enemyTeaId = goodsDao.createNewGoods(DatabaseObject.TEA_TEMPLATE_ID,700,50);
        holdDao.addCargo(enemyWoodId, enemyHoldId);
        holdDao.addCargo(enemyTeaId, enemyHoldId);
        String res = executorDao.moveCargoToWinner(myShipId, enemyHoldId);
        assertEquals(res, "Wrong input data");
    }
}