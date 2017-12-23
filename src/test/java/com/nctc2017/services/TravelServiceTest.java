package com.nctc2017.services;

import java.math.BigInteger;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.PlayerDao;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationConfig.class })
public class TravelServiceTest {

    private static Player nik;
    private static Player steve;

    private static City netcracken;
    private static City vataArt;

    @InjectMocks
    private TravelService travelService;

    @Autowired
    private ApplicationContext context;

    @Mock
    private PlayerDao mockPlayerDao;

    @Mock
    private CityDao mockCityDao;

    @BeforeClass
    public static void createPlayerNik() {
        BigInteger playerIdNik = BigInteger.ONE;
        BigInteger cityId = BigInteger.valueOf(2);

        String loginNik = "Nik";
        String emailNik = "q@q.q";
        int moneyNik = 10000;
        int pointsNik = 1;
        int lvlNik = 5;

        netcracken = new City("NetCracken", null, cityId);
        nik = new Player(playerIdNik, loginNik, emailNik, moneyNik, pointsNik, lvlNik);
    }

    @BeforeClass
    public static void createPlayerSteve() {
        BigInteger playerIdSteve = BigInteger.TEN;
        BigInteger cityId = BigInteger.valueOf(11);

        String loginSteve = "Steve";
        String emailSteve = "qw@qw.qw";
        int moneySteve = 11000;
        int pointsSteve = 2;
        int lvlSteve = 10;

        vataArt = new City("VataArt", null, cityId);
        steve = new Player(playerIdSteve, loginSteve, emailSteve, moneySteve, pointsSteve, lvlSteve);
    }

    @Before
    public void initMocks() {
        travelService = (TravelService)this.context.getBean("travelServicePrototype");
        MockitoAnnotations.initMocks(this);

        when(mockCityDao.find(vataArt.getCityId())).thenReturn(vataArt);
        when(mockCityDao.find(netcracken.getCityId())).thenReturn(netcracken);

        when(mockPlayerDao.findPlayerById(steve.getPlayerId())).thenReturn(steve);
        when(mockPlayerDao.getPlayerCity(steve.getPlayerId())).thenReturn(vataArt.getCityId());

        when(mockPlayerDao.findPlayerById(nik.getPlayerId())).thenReturn(nik);
        when(mockPlayerDao.getPlayerCity(nik.getPlayerId())).thenReturn(netcracken.getCityId());
    }

    private void testRelocate(Player player, City city) {
        // When
        travelService.relocate(player.getPlayerId(), city.getCityId());
        int time = travelService.getRelocateTime(player.getPlayerId());
        // Then
        // between 1minut and 5minutes
        assertTrue("Time: " + time, time >= 60 && time<= 300);
    }

    @Test
    public void testRelocateOnePerson() {
        testRelocate(nik, vataArt);
    }

    private void relocateTwoPersons() {
        testRelocate(nik, vataArt);
        testRelocate(steve, netcracken);
    }

    @Test
    public void testRelocateTwoPersons() {
        relocateTwoPersons();
    }

    private void testIsEnemyOnHorizon(Player player) {
        boolean ret = travelService.isEnemyOnHorizon(player.getPlayerId());
        assertTrue(ret);
    }

    @Test
    public void test_IsEnemyOnHorizon_For_Players_With_Lvl_Difference_Less_Then_5() {
        relocateTwoPersons();
        testIsEnemyOnHorizon(nik);
        testIsEnemyOnHorizon(steve);
    }

    @Test
    public void testCurrentCity() {
        City city1 = travelService.getCurrentCity(nik.getPlayerId());
        City city2 = travelService.getCurrentCity(steve.getPlayerId());
        assertEquals(netcracken, city1);
        assertEquals(vataArt, city2);
    }

    private void confirmAttackNik(boolean decision) {
        relocateTwoPersons();
        testIsEnemyOnHorizon(nik);
        travelService.confirmAttack(nik.getPlayerId(), decision);
    }

    @Test
    public void testConfirmAttackTrue() {
        confirmAttackNik(true);
        boolean isBattleStart = travelService.isBattleStart(nik.getPlayerId());
        assertTrue(isBattleStart);
    }

    private void testConfirmAttackNikFalse() {
        confirmAttackNik(false);
        boolean isBattleStart = travelService.isBattleStart(nik.getPlayerId());
        assertFalse(isBattleStart);
    }

    @Test
    public void testConfirmAttackFalse() {
        testConfirmAttackNikFalse();
    }

    @Test
    public void testConfirmAttackAnotherPlayer() {
        testConfirmAttackNikFalse();

        testIsEnemyOnHorizon(steve);
        travelService.confirmAttack(steve.getPlayerId(), true);

        boolean isBattleStart = travelService.isBattleStart(nik.getPlayerId());
        assertTrue(isBattleStart);
    }
}
