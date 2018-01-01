package com.nctc2017.dao.Impl;

import com.nctc2017.bean.*;
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
    @Autowired
    AmmoDao ammoDao;
    @Autowired
    CannonDao cannonDao;
    @Autowired
    MastDao mastDao;

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
        BigInteger cannonballId = ammoDao.createAmmo(DatabaseObject.CANNONBALL_TEMPLATE_OBJECT_ID, 10);
        BigInteger chainId = ammoDao.createAmmo(DatabaseObject.CHAIN_TEMPLATE_OBJECT_ID, 10);
        BigInteger buckshotId = ammoDao.createAmmo(DatabaseObject.BUCKSHOT_TEMPLATE_OBJECT_ID, 10);
        holdDao.addCargo(enemyWoodId, enemyHoldId);
        holdDao.addCargo(enemyTeaId, enemyHoldId);
        holdDao.addCargo(cannonballId, enemyHoldId);
        holdDao.addCargo(buckshotId, enemyHoldId);
        holdDao.addCargo(chainId, enemyHoldId);
        String res = executorDao.moveCargoToWinner(myShipId, enemyShipId);
        List<Goods> myGoods = goodsDao.getAllGoodsFromHold(myHoldId);
        List<Ammo> myAmmos = ammoDao.getAllAmmoFromHold(myHoldId);
        List<Goods> enemyGoods = goodsDao.getAllGoodsFromHold(enemyHoldId);
        List<Ammo> enemyAmmos = ammoDao.getAllAmmoFromHold(enemyHoldId);
        assertEquals(myGoods.size(),3);
        assertEquals(myAmmos.size(), 3);
        assertEquals(enemyGoods.size(), 0);
        assertEquals(enemyAmmos.size(), 0);
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
        BigInteger enemyTeaId = goodsDao.createNewGoods(DatabaseObject.TEA_TEMPLATE_ID,30,50);
        BigInteger cannonballId = ammoDao.createAmmo(DatabaseObject.CANNONBALL_TEMPLATE_OBJECT_ID, 80);
        BigInteger chainId = ammoDao.createAmmo(DatabaseObject.CHAIN_TEMPLATE_OBJECT_ID, 80);
        BigInteger buckshotId = ammoDao.createAmmo(DatabaseObject.BUCKSHOT_TEMPLATE_OBJECT_ID, 100);
        holdDao.addCargo(enemyWoodId, enemyHoldId);
        holdDao.addCargo(enemyTeaId, enemyHoldId);
        holdDao.addCargo(cannonballId, enemyHoldId);
        holdDao.addCargo(buckshotId, enemyHoldId);
        holdDao.addCargo(chainId, enemyHoldId);
        String res = executorDao.moveCargoToWinner(myShipId, enemyShipId);
        List<Goods> myGoods = goodsDao.getAllGoodsFromHold(myHoldId);
        List<Ammo> myAmmos = ammoDao.getAllAmmoFromHold(myHoldId);
        List<Goods> enemyGoods = goodsDao.getAllGoodsFromHold(enemyHoldId);
        List<Ammo> enemyAmmos = ammoDao.getAllAmmoFromHold(enemyHoldId);
        assertEquals(myAmmos.size(), 1);
        assertEquals(myGoods.size(),3);
        assertEquals(enemyGoods.size(), 0);
        assertEquals(enemyAmmos.size(), 3);
        assertEquals(holdDao.getOccupiedVolume(myShipId),100);
        assertEquals(holdDao.getOccupiedVolume(enemyShipId),210);
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

    @Test
    @Rollback(true)
    public void moveCargoTo() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player = playerDao.findPlayerByLogin("Steve");
        BigInteger myShipId1 = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, player.getPlayerId());
        BigInteger myShipId2 = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, player.getPlayerId());
        BigInteger myHoldId1 = holdDao.createHold(myShipId1);
        BigInteger myHoldId2 = holdDao.createHold(myShipId2);
        BigInteger myWoodId1 = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,10,40);
        BigInteger myWoodId2 = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,10,40);
        BigInteger cannonballId = ammoDao.createAmmo(DatabaseObject.CANNONBALL_TEMPLATE_OBJECT_ID, 10);
        BigInteger chainId = ammoDao.createAmmo(DatabaseObject.CHAIN_TEMPLATE_OBJECT_ID, 10);
        BigInteger buckshotId = ammoDao.createAmmo(DatabaseObject.BUCKSHOT_TEMPLATE_OBJECT_ID, 10);
        BigInteger bombardId =  cannonDao.createCannon(DatabaseObject.BOMBARD_TEMPLATE_ID, myHoldId1);
        BigInteger mastId = mastDao.createNewMast(DatabaseObject.MAST1_TEMPLATE_OBJECT_ID, myHoldId1);
        holdDao.addCargo(myWoodId1,myHoldId1);
        holdDao.addCargo(cannonballId,myHoldId1);
        holdDao.addCargo(bombardId, myHoldId1);
        holdDao.addCargo(mastId, myHoldId1);
        holdDao.addCargo(chainId,myHoldId1);
        holdDao.addCargo(buckshotId,myHoldId1);
        holdDao.addCargo(myWoodId2,myHoldId2);
        executorDao.moveCargoTo(myWoodId1,myHoldId2, 10);
        executorDao.moveCargoTo(bombardId, myHoldId2,1);
        List<Goods> goods1 = goodsDao.getAllGoodsFromHold(myHoldId1);
        List<Goods> goods2 = goodsDao.getAllGoodsFromHold(myHoldId2);
        int cargo1Quant = holdDao.getOccupiedVolume(myShipId1);
        int cargo2Quant = holdDao.getOccupiedVolume(myShipId2);
        assertEquals(goods1.size(), 0);
        assertEquals(goods2.size(), 2);
        assertEquals(cargo1Quant, 31);
        assertEquals(cargo2Quant, 21);
    }
    @Test
    @Rollback(true)
    public void moveCargoToFailed() throws Exception {
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player steve = playerDao.findPlayerByLogin("Steve");
        BigInteger steveShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, steve.getPlayerId());
        BigInteger steveHoldId = holdDao.createHold(steveShipId);
        playerDao.addNewPlayer("Iogan","1111","Shimdt@gmail.com");
        Player iogan = playerDao.findPlayerByLogin("Iogan");
        BigInteger ioganShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, iogan.getPlayerId());
        BigInteger ioganHoldId = holdDao.createHold(ioganShipId);
        BigInteger woodId = goodsDao.createNewGoods(DatabaseObject.WOOD_TEMPLATE_ID,10,40);
        holdDao.addCargo(woodId, steveHoldId);
        String res = executorDao.moveCargoTo(woodId, ioganHoldId, 5);
        assertEquals(res, "Cargos can be transfered only between one player");
    }
}