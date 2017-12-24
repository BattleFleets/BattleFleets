package com.nctc2017.services;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.AmmoDao;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.HoldDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import com.nctc2017.exception.BattleEndException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class})
@Transactional
public class BattleServiceIntegrationTest {
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
    private AmmoDao ammoDao;
    
    @Autowired
    private TravelService travelService;
    @Autowired
    private BattlePreparationService prepService;
    @Autowired
    private BattleService battleService;
    
    private BigInteger nikId;
    private BigInteger steveId;
    private BigInteger nikShipId;
    private BigInteger steveShipId;
    
    private Player newCombatant(String login, String email) {
        playerDao.addNewPlayer(login, "123", email);
        Player player = playerDao.findPlayerByLogin(login);
        
        return player;
    }
    
    @Before
    public void setUpCombatant() {
        travelService = (TravelService)this.context.getBean("travelServicePrototype");
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
        
        BigInteger cannonballId = 
                ammoDao.createAmmo(DatabaseObject.CANNONBALL_TEMPLATE_OBJECT_ID, 25);
        BigInteger buckshotId = 
                ammoDao.createAmmo(DatabaseObject.BUCKSHOT_TEMPLATE_OBJECT_ID, 25);
        BigInteger chainId = 
                ammoDao.createAmmo(DatabaseObject.CHAIN_TEMPLATE_OBJECT_ID, 25);
        
        BigInteger nikHoldId = holdDao.createHold(nikShipId);
        holdDao.addCargo(buckshotId, nikHoldId);
        holdDao.addCargo(cannonballId, nikHoldId);
        holdDao.addCargo(chainId, nikHoldId);
        
        BigInteger cityIdNik = playerDao.getPlayerCity(nik.getPlayerId());
        BigInteger cityIdSteve = playerDao.getPlayerCity(steve.getPlayerId());
        List<City> cities = cityDao.findAll();
        
        travelService.relocate(nikId, cityIdNik.equals(cities.get(0).getCityId()) 
                ? cities.get(1).getCityId() 
                : cities.get(0).getCityId());
        travelService.relocate(steveId, cityIdSteve.equals(cities.get(0).getCityId()) 
                ? cities.get(1).getCityId() 
                : cities.get(0).getCityId());
        
        testIsEnemyOnHorizon(nik);
        
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
    
    private void testIsEnemyOnHorizon(Player player) {
        boolean ret = travelService.isEnemyOnHorizon(player.getPlayerId());
        assertTrue(ret);
    }
    
    @Test
    @Ignore
    public void calculateDamage() throws SQLException {
        /*List<Integer> mortars = new ArrayList<>(3);
        mortars.add(8);
        mortars.add(7);
        mortars.add(5);
        List<Integer> kulevrins = new ArrayList<>(3);
        kulevrins.add(0);
        kulevrins.add(0);
        kulevrins.add(0);
        List<Integer> bombards = new ArrayList<>(3);
        bombards.add(0);
        bombards.add(0);
        bombards.add(0);
        List<List<Integer>> cannonAmmo = new ArrayList<>(3);
        cannonAmmo.add(mortars);
        cannonAmmo.add(kulevrins);
        cannonAmmo.add(bombards);*/
        int[][] cannonAmmo = new int [3][];
        int[] mortars = new int[] {8, 7, 5};
        int[] kulevrins = new int[] {0, 0, 0};
        int[] bombards = new int[] {0, 0, 0};
        cannonAmmo[0] = mortars;
        cannonAmmo[1] = kulevrins;
        cannonAmmo[2] = bombards;
            boolean res = battleService.calculateDamage(cannonAmmo, nikId);
            assertTrue(res);
    }
}
