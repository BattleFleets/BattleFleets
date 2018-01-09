package com.nctc2017.services;

import com.nctc2017.bean.Player;
import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.dao.MastDao;
import com.nctc2017.dao.PlayerDao;
import com.nctc2017.dao.ShipDao;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

;
;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class})
@Transactional
public class ShipTradeServiceTest {

    @Autowired
    private ApplicationContext context;

    @Mock
    private ShipDao shipDao;
    @Mock
    private MastDao mastDao;

    @Mock
    private PlayerDao playerDao;

    @InjectMocks
    private ShipTradeService shipTradeService;
    @InjectMocks
    private MoneyService moneyService;
    @Mock
    private ShipRepairService shipRepairService;
    @Mock
    private LevelUpService levelUpService;

    private static Player steve;
    private static ShipTemplate t_BlackPerl;
    private static Ship blackPerl;
    private static int money;


    @BeforeClass
    public static void createPlayerSteve() {
        BigInteger playerId = BigInteger.TEN;
        BigInteger cityId = BigInteger.valueOf(11);

        String login = "Steve";
        String email = "Rogers@gmail.com";
        int money = 1150;
        int points = 13;
        int lvl = 10;
        steve = new Player(playerId, login, email, money, points, lvl,5);
    }


    @BeforeClass
    public static void createShipTemplate() {
        String t_name = "Full-rigged ship";
        BigInteger shipTemplId = BigInteger.ONE;
        int maxHealth = 100;
        int maxSailorsQuantity = 100;
        int cost = 300;
        int maxMastsQuantity = 5;
        int maxCannonQuantity = 30;
        int maxCarryingLimit = 90;
        int curSailorsQuantity = 80;
        int curCarryingLimit = 60;

        BigInteger shipId = new BigInteger("2");
        String curName = "Black Perl";
        int curHealth = 80;

        t_BlackPerl = new ShipTemplate(shipTemplId, t_name, maxHealth, maxSailorsQuantity,
                cost, maxMastsQuantity, maxCannonQuantity, maxCarryingLimit);
        blackPerl = new Ship(t_BlackPerl, shipId, curName, curHealth, curSailorsQuantity, curCarryingLimit);
    }

    @Before
    public void initMocks() {
        shipTradeService = (ShipTradeService) this.context.getBean("shipTradeService");
        moneyService = (MoneyService) this.context.getBean("moneyServiceSingleton");
        MockitoAnnotations.initMocks(this);

        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                steve.setMoney((int) args[1]);
                return null;
            }
        }).when(playerDao).updateMoney(any(), anyInt());

        when(levelUpService.getMaxShips(steve.getPlayerId())).thenReturn(10);
        when(shipRepairService.countRepairCost(any())).thenReturn(0);
        when(shipDao.createNewShip(any(), any())).thenReturn(any());
        when(shipDao.findShipTemplate(t_BlackPerl.getTemplateId())).thenReturn(t_BlackPerl);
        when(shipDao.findShip(blackPerl.getShipId())).thenReturn(blackPerl);
        when(playerDao.getPlayerMoney(steve.getPlayerId())).thenReturn(steve.getMoney());
        when(playerDao.findAllShip(steve.getPlayerId())).thenReturn(null);
    }

    @Test
    public void buyShipTest() throws Exception {
        money = steve.getMoney();
        BigInteger shipTempId = t_BlackPerl.getTemplateId();

        assertEquals("Congratulations! One more ship is already armed.",
                shipTradeService.buyShip(steve.getPlayerId(), shipTempId));
        assertEquals(money - t_BlackPerl.getCost(), steve.getMoney());
    }

    @Test
    public void soldShipTest() throws Exception {
        money = steve.getMoney();
        playerDao.addShip(steve.getPlayerId(), blackPerl.getShipId());

        assertTrue(shipTradeService.sellShip(steve.getPlayerId(), blackPerl.getShipId()));
        assertEquals(money + t_BlackPerl.getCost(), steve.getMoney());
    }
}
