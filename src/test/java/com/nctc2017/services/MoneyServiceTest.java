package com.nctc2017.services;

import com.nctc2017.bean.Player;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.dao.PlayerDao;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.OutputStream;
import java.math.BigInteger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationConfig.class })
public class MoneyServiceTest {
    private static Player steve;
    
    @Autowired
    private ApplicationContext context;
    
    @Mock
    PlayerDao playerDao;

    @InjectMocks
    MoneyService moneyService;

    @BeforeClass
    public static void createPlayerSteve() {
        BigInteger playerId = BigInteger.TEN;
        BigInteger cityId = BigInteger.valueOf(11);

        String login = "Steve";
        String email = "Rogers@gmail.com";
        int money = 150;
        int points = 13;
        int lvl = 10;
        steve = new Player(playerId, login, email, money, points, lvl);
    }

    @Before
    public void initMocks() {
        this.context.getBean("moneyServicePrototype");
        MockitoAnnotations.initMocks(this);

        when(playerDao.getPlayerMoney(steve.getPlayerId())).thenReturn(150);



    }

    @Test
    public void addMoney() throws Exception {
        int money=steve.getMoney();
        steve.setMoney(moneyService.addMoney(steve.getPlayerId(),50));
        assertEquals(money+50,steve.getMoney());
        steve.setMoney(150);

    }

    @Test
    public void deductMoney() throws Exception {
        int money=steve.getMoney();
        steve.setMoney(moneyService.deductMoney(steve.getPlayerId(),50));
        assertEquals(money-50,steve.getMoney());
    }

    @Test(expected=IllegalArgumentException.class)
    public void deductMoneyFailed() throws Exception {
        int money=steve.getMoney();
        steve.setMoney(moneyService.deductMoney(steve.getPlayerId(),300));
        assertEquals(money-50,steve.getMoney());
    }

    @Test
    public void isEnoughMoney() throws Exception {
       boolean isEnoughMoney=moneyService.isEnoughMoney(steve.getPlayerId(),100);
       assertTrue(isEnoughMoney);
    }

    @Test
    public void isEnoughMoneyFail() throws Exception {
        boolean isEnoughMoney=moneyService.isEnoughMoney(steve.getPlayerId(),300);
        assertFalse(isEnoughMoney);
    }

    @Test
    public void getPlayersMoney() throws Exception {
        int money1=steve.getMoney();
        int money2=moneyService.getPlayersMoney(steve.getPlayerId());
        assertEquals(money1,money2);
    }

}