package com.nctc2017.services;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.Ammo;
import com.nctc2017.bean.City;
import com.nctc2017.bean.Mast;
import com.nctc2017.bean.Player;
import com.nctc2017.bean.Ship;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.AmmoDao;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.GoodsDao;
import com.nctc2017.dao.HoldDao;
import com.nctc2017.dao.MastDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.exception.BattleEndException;
import com.nctc2017.exception.DeadEndException;
import com.nctc2017.services.utils.BattleEndVisitor;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class})
@Transactional
@Rollback(true)
@FixMethodOrder
public class BattleIntegrationScenarioTest {
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private PlayerDao playerDao;
    @Autowired
    private ShipDao shipDao;
    @Autowired
    private HoldDao holdDao;
    @Autowired
    private CityDao cityDao;
    @Autowired
    private  AmmoDao ammoDao;
    @Autowired
    private  MastDao mastDao;
    @Autowired
    private  CannonDao cannonDao;
    @Autowired
    private GoodsDao goodsDao;
    
    @Autowired
    private  TravelService travelService;
    @Autowired
    private  BattlePreparationService prepService;
    @Autowired
    private  BattleService battleService;
    @Autowired
    private  BattleEndingService battleEnd;
    
    private  BigInteger nikId;
    private  BigInteger steveId;
    private  BigInteger nikShipId;
    private  BigInteger steveShipId;
    private  BigInteger nikHoldId;
    private  BigInteger steveHoldId;
    
    private  BigInteger cannonballId;
    private  BigInteger buckshotId;
    private  BigInteger chainId;
    
    private final int defCount = 2;
    private final int defCannonballCount = 1;
    private final int[] mortarsCball = new int[] {defCannonballCount, 0, 0};
    private final int[] kulevrinsCball = new int[] {defCannonballCount, 0, 0};
    private final int[] bombardsCball = new int[] {defCannonballCount, 0, 0};
    
    private final int[] mortarsCB = new int[] {0, defCount, defCount};
    private final int[] kulevrinsCB = new int[] {0, defCount, defCount};
    private final int[] bombardsCB = new int[] {0, defCount, defCount};

    private final int[][] hullDamage = new int [][]{mortarsCball, kulevrinsCball, bombardsCball};
    
    private final int[][] mastAndCrewDamage = new int [][]{mortarsCB, kulevrinsCB, bombardsCB};
    
    private  Player newCombatant(String login, String email) {
        playerDao.addNewPlayer(login, "123", email);
        Player player = playerDao.findPlayerByLogin(login);
        
        return player;
    }
    
    private  void getMoreCannons(int count, BigInteger shipId) {
        for (int i = 0; i < count / 2; i++) {
            BigInteger id = 
                    cannonDao.createCannon(DatabaseObject.BOMBARD_TEMPLATE_ID, shipId);
            shipDao.setCannonOnShip(id, shipId);
        }
        for (int i = 0; i < count / 2; i++) {
            BigInteger id = 
                    cannonDao.createCannon(DatabaseObject.KULEVRIN_TEMPLATE_ID, shipId);
            shipDao.setCannonOnShip(id, shipId);
        }
    }
    
    @Before
    public void setUpCombatant() {
        travelService = (TravelService)context.getBean("travelServicePrototype");
        String loginNik = "Nik";
        String emailNik = "q@q.q";
        String loginSteve = "Steve";
        String emailSteve = "qqq@qq.qq";
        
        Player nik = newCombatant(loginNik, emailNik);
        Player steve = newCombatant(loginSteve, emailSteve);
        nikId = nik.getPlayerId();
        steveId = steve.getPlayerId();
        
        nikShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, nikId);
        steveShipId = shipDao.createNewShip(DatabaseObject.T_CARAVELLA_OBJECT_ID, steveId);
        assertTrue(shipDao.getSpeed(nikShipId) > 0);
        assertTrue(shipDao.getSpeed(steveShipId) > 0);
        getMoreCannons(24, nikShipId);
        getMoreCannons(24, steveShipId);
        assertTrue(shipDao.getCurrentShipSailors(nikShipId) > 0);
        assertTrue(shipDao.getCurrentShipSailors(steveShipId) > 0);
        cannonballId = 
                ammoDao.createAmmo(DatabaseObject.CANNONBALL_TEMPLATE_OBJECT_ID, 200);
        buckshotId = 
                ammoDao.createAmmo(DatabaseObject.BUCKSHOT_TEMPLATE_OBJECT_ID, 150);
        chainId = 
                ammoDao.createAmmo(DatabaseObject.CHAIN_TEMPLATE_OBJECT_ID, 200);
        
        nikHoldId = holdDao.createHold(nikShipId);
        steveHoldId = holdDao.createHold(steveShipId);

        holdDao.addCargo(cannonballId, nikHoldId);
        holdDao.addCargo(buckshotId, steveHoldId);
        holdDao.addCargo(chainId, steveHoldId);
        BigInteger goodsId_1 = 
                goodsDao.createNewGoods(DatabaseObject.GRAIN_TEMPLATE_ID, 10, 10);
        holdDao.addCargo(goodsId_1, steveHoldId);
        BigInteger goodsId_2 = 
                goodsDao.createNewGoods(DatabaseObject.GRAIN_TEMPLATE_ID, 10, 10);
        holdDao.addCargo(goodsId_2, nikHoldId);
       
        BigInteger cityIdNik = playerDao.getPlayerCity(nik.getPlayerId());
        BigInteger cityIdSteve = playerDao.getPlayerCity(steve.getPlayerId());
        List<City> cities = cityDao.findAll();
        
        travelService.relocate(nikId, cityIdNik.equals(cities.get(0).getCityId()) 
                ? cities.get(1).getCityId() 
                : cities.get(0).getCityId());
        travelService.relocate(steveId, cityIdSteve.equals(cities.get(0).getCityId()) 
                ? cities.get(1).getCityId() 
                : cities.get(0).getCityId());
        
        boolean ret = travelService.isEnemyOnHorizon(nikId);
        assertTrue(ret);
        
        travelService.confirmAttack(nikId, true);
        
        boolean start = travelService.isBattleStart(nikId);
        assertTrue(start); 
        start = travelService.isBattleStart(steveId);
        assertTrue(start); 
            
        prepService.chooseShip(nikId, nikShipId);
        prepService.chooseShip(steveId, steveShipId);
        prepService.setReady(nikId);
        prepService.setReady(steveId);
        try {
            assertTrue(prepService.waitForEnemyReady(nikId));
        } catch (BattleEndException e) {
            fail();
        }
        try {
            assertTrue(prepService.waitForEnemyReady(steveId));
        } catch (BattleEndException e) {
            fail();
        }

    }
    
    @Test(expected = IllegalStateException.class)
    public void testBoardingWithBigDist() throws DeadEndException, BattleEndException {
        battleService.boarding(nikId, null);
    }
    
    private int totalCurrSpeed(BigInteger shipId) {
        int currSpeed = shipDao.getSpeed(shipId);
        return currSpeed;
    }
    
    @Test
    public void testBattleWithShipDestroy() throws DeadEndException, BattleEndException, SQLException {

        Ship steveShipBefore;
        Ship nikShipAfter;
        Ship steveShipAfter;
        while(true) {
            steveShipBefore = shipDao.findShip(steveShipId);
            
            battleService.decreaseOfDistance(nikId);
            
            battleService.calculateDamage(hullDamage, nikId, null);
            battleService.calculateDamage(mastAndCrewDamage, steveId, new DefaultDestroyBattleEnd());
            
            nikShipAfter = shipDao.findShip(nikShipId);
            try {
                steveShipAfter = shipDao.findShip(steveShipId);
            } catch (IllegalArgumentException e) {
                break;
            }
            assertTrue(nikShipAfter.getCurSailorsQuantity() >= 0);
            int currSpeed = totalCurrSpeed(steveShipId);
            assertTrue(currSpeed >= 1);
    
            assertTrue(steveShipAfter.getCurHealth() < steveShipBefore.getCurHealth());
            
            assertTrue(battleService.getDistance(nikId) >= 0);
        }
        assertTrue(battleService.isBattleFinish(nikId));
        assertTrue(battleService.isBattleFinish(steveId));
        assertTrue(battleService.isLeaveBattleFieldAvailable(steveId));
        assertTrue(battleService.isLeaveBattleFieldAvailable(nikId));
        assertTrue(battleService.leaveBattleField(nikId));
        try {
            battleService.leaveBattleField(steveId);
        } catch (BattleEndException e1) {
            return;
        }
        fail("BattleEndException expected");
        return;
        
    }
    
    @Test
    public void testBoarding() throws BattleEndException, SQLException {

        Ship nikShipBefore = shipDao.findShip(nikShipId);
        Ship steveShipBefore = shipDao.findShip(steveShipId);
        Ship nikShipAfter;
        Ship steveShipAfter;
        int dist;
        int[] mortars = new int[] {0, 5, 0};
        int[] kulevrins = new int[] {0, 5, 0};
        int[] bombards = new int[] {0, 5, 0};

        int[][] ammoCannons = new int [][]{mortars, kulevrins, bombards};
        int[][] ammoCannonsNik = new int [][]{{0, 0, 0},{0, 0, 0},{0, 0, 0}};
        while(true) {
            dist = battleService.getDistance(nikId);
            battleService.setConvergaceOfDist(nikId, true);
            battleService.setConvergaceOfDist(steveId, true);
            battleService.decreaseOfDistance(nikId);
            battleService.decreaseOfDistance(steveId);
            
            battleService.calculateDamage(ammoCannonsNik, nikId, null);
            battleService.calculateDamage(ammoCannons, steveId, null);
            int currSteveSailors = shipDao.getCurrentShipSailors(steveShipId);
            assertEquals("" + currSteveSailors + " != " + steveShipBefore.getCurSailorsQuantity(), 
                    currSteveSailors, 
                    steveShipBefore.getCurSailorsQuantity());
            
            assertTrue(battleService.getDistance(nikId) >= 0);
            assertTrue(battleService.getDistance(nikId) < dist);
            
            if (battleService.getDistance(nikId) == 0) {
                try {
                    battleService.boarding(nikId, new DefaultBoardingBattleEnd());
                    break;
                } catch (DeadEndException e) {
                    shipDao.updateShipSailorsNumber(nikShipId, 20);
                    shipDao.updateShipSailorsNumber(steveShipId, 20);
                }
            }
        }
        nikShipAfter = shipDao.findShip(nikShipId);
        steveShipAfter = shipDao.findShip(steveShipId);
        assertTrue(nikShipAfter.getCurSailorsQuantity() >= 0);
        assertTrue(nikShipAfter.getCurSailorsQuantity() < nikShipBefore.getCurSailorsQuantity());
        int steveCurSailors = steveShipAfter.getCurSailorsQuantity();
        assertTrue("steve crew after battle: " + steveCurSailors + " >= 0", steveCurSailors >= 0);
        assertTrue(steveShipAfter.getCurSailorsQuantity() < steveShipBefore.getCurSailorsQuantity());
        assertTrue(battleService.getDistance(nikId) >= 0);
       
        assertTrue(battleService.isBattleFinish(nikId));
        assertTrue(battleService.isBattleFinish(steveId));
        assertTrue(battleService.isLeaveBattleFieldAvailable(steveId));
        assertTrue(battleService.isLeaveBattleFieldAvailable(nikId));
        assertTrue(battleService.leaveBattleField(nikId));
        try {
            battleService.leaveBattleField(steveId);
        } catch (BattleEndException e1) {
            return;
        }
        fail("BattleEndException expected");
        return;
        
    }
    
    private class DefaultDestroyBattleEnd implements BattleEndVisitor {

        @Override
        public void endCaseVisit(PlayerDao playerDao, ShipDao shipDao, BigInteger winnerShipId, BigInteger loserShipId,
                BigInteger winnerId, BigInteger loserId) {
            int loserVolumeBefore = holdDao.getOccupiedVolume(loserShipId);
            int winerVolumeBefore = holdDao.getOccupiedVolume(winnerShipId);
            battleEnd.passDestroyGoodsToWinner(winnerShipId, loserShipId);
            int loserVolumeAfter = holdDao.getOccupiedVolume(loserShipId);
            int winerVolumeAfter = holdDao.getOccupiedVolume(winnerShipId);
            assertTrue(loserVolumeBefore > loserVolumeAfter);
            assertTrue(winerVolumeBefore < winerVolumeAfter);
            shipDao.deleteShip(loserShipId);
        }
        
    }
    
    private class DefaultBoardingBattleEnd implements BattleEndVisitor {

        @Override
        public void endCaseVisit(PlayerDao playerDao, ShipDao shipDao, BigInteger winnerShipId, BigInteger loserShipId,
                BigInteger winnerId, BigInteger loserId) {
            int loserVolumeBefore = holdDao.getOccupiedVolume(loserShipId);
            int winerVolumeBefore = holdDao.getOccupiedVolume(winnerShipId);
            battleEnd.passCargoToWinnerAfterBoarding(winnerShipId, loserShipId);
            int loserVolumeAfter = holdDao.getOccupiedVolume(loserShipId);
            int winerVolumeAfter = holdDao.getOccupiedVolume(winnerShipId);
            assertTrue(loserVolumeBefore > loserVolumeAfter);
            assertTrue(winerVolumeBefore < winerVolumeAfter);
        }
        
    }
}
