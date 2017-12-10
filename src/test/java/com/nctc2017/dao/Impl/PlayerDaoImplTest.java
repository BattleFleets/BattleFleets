package com.nctc2017.dao.Impl;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.PlayerDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@Transactional
public class PlayerDaoImplTest {
    @Autowired
    PlayerDao playerDao;
    @Autowired
    CityDao cityDao;

    @Test
    @Rollback(true)
    public void testAddNewPlayer() throws Exception {
        String succesResult=playerDao.addNewPlayer("qwe","1111","@FWF");
        assertEquals("Registration is successfull",succesResult);
        String existingLogin=playerDao.addNewPlayer("Sasha228","1111","@FWF");
        assertEquals("Login exists, enter another login",existingLogin);
        String existingEmail=playerDao.addNewPlayer("asfdf","1111","Sasha@gmail.com");
        assertEquals("Email exists, enter another email",existingEmail);
    }
    @Test
    @Rollback(true)
    public void testFindPlayerByLogin() throws Exception{
         Player player=playerDao.findPlayerByLogin("Qwerty");
         assertNull(player);
         Player topPlayer1=playerDao.findPlayerByLogin("Sasha228");
         Player topPlayer2=new Player("Sasha228","Sasha@gmail.com",1000000,1000,101,69);
         assertEquals(topPlayer1.getLogin(),topPlayer2.getLogin());
         assertEquals(topPlayer1.getEmail(),topPlayer2.getEmail());
         assertEquals(topPlayer1.getLevel(),topPlayer2.getLevel());
         assertEquals(topPlayer1.getPoints(),topPlayer2.getPoints());
         assertEquals(topPlayer1.getMoney(),topPlayer2.getMoney());
         assertEquals(topPlayer1.getCurCity(),topPlayer2.getCurCity());

    }
    @Test
    @Rollback(true)
    public void testUpdateLogin() throws Exception{
        playerDao.updateLogin(41,"Sasha322");
        String login=playerDao.findPlayerByLogin("Sasha322").getLogin();
        assertEquals("Sasha322",login);
    }
    @Test
    @Rollback(true)
    public void testUpdateLevel() throws Exception{
        playerDao.updateLevel(41,80);
        int level=playerDao.findPlayerByLogin("Sasha228").getLevel();
        assertEquals(80,level);
    }
    @Test
    @Rollback(true)
    public void testUpdateEmail() throws Exception{
        playerDao.updateEmail(41,"Sasha322@gmail.com");
        String email=playerDao.findPlayerByLogin("Sasha228").getEmail();
        assertEquals("Sasha322@gmail.com",email);
    }
    @Test
    @Rollback(true)
    public void testUpdatePoints() throws Exception{
        playerDao.updatePoints(41,100);
        int points=playerDao.findPlayerByLogin("Sasha228").getPoints();
        assertEquals(100,points);
    }
    @Test
    @Rollback(true)
    public void testFindPlayerById() throws Exception{
        Player player=playerDao.findPlayerById(71);
        assertNull(player);
        Player topPlayer1=playerDao.findPlayerById(41);
        Player topPlayer2=new Player("Sasha228","Sasha@gmail.com",1000000,1000,101,69);
        assertEquals(topPlayer1.getLogin(),topPlayer2.getLogin());
        assertEquals(topPlayer1.getEmail(),topPlayer2.getEmail());
        assertEquals(topPlayer1.getLevel(),topPlayer2.getLevel());
        assertEquals(topPlayer1.getPoints(),topPlayer2.getPoints());
        assertEquals(topPlayer1.getMoney(),topPlayer2.getMoney());
        assertEquals(topPlayer1.getCurCity(),topPlayer2.getCurCity());
    }
    @Test
    @Rollback(true)
    public void testFindAllPlayer() throws Exception{
        List<Player> players=playerDao.findAllPlayers();
        Player topPlayer1=players.get(0);
        Player topPlayer2=new Player("Sasha228","Sasha@gmail.com",1000000,1000,101,69);
        assertEquals(topPlayer1.getLogin(),topPlayer2.getLogin());
        assertEquals(topPlayer1.getEmail(),topPlayer2.getEmail());
        assertEquals(topPlayer1.getLevel(),topPlayer2.getLevel());
        assertEquals(topPlayer1.getPoints(),topPlayer2.getPoints());
        assertEquals(topPlayer1.getMoney(),topPlayer2.getMoney());
        assertEquals(topPlayer1.getCurCity(),topPlayer2.getCurCity());
    }
    @Test
    @Rollback(true)
    public void testGetPlayerLogin() throws Exception{
        String login=playerDao.getPlayerLogin(71);
        assertNull(login);
        String login1=playerDao.getPlayerLogin(41);
        Player topPlayer2=new Player("Sasha228","Sasha@gmail.com",1000000,1000,101,69);
        assertEquals(login1,topPlayer2.getLogin());
    }
    @Test
    @Rollback(true)
    public void testGetPlayerPassword() throws Exception{
        String password=playerDao.getPlayerPassword(71);
        assertNull(password);
        String password1=playerDao.getPlayerPassword(41);
        assertEquals(password1,"Password1");
    }
    @Test
    @Rollback(true)
    public void testGetPlayerEmail() throws Exception{
        String email=playerDao.getPlayerEmail(71);
        assertNull(email);
        String email1=playerDao.getPlayerEmail(41);
        Player topPlayer2=new Player("Sasha228","Sasha@gmail.com",1000000,1000,101,69);
        assertEquals(email1,topPlayer2.getEmail());
    }
    @Test
    @Rollback(true)
    public void testGetPlayerMoney() throws Exception{
        int money=playerDao.getPlayerMoney(71);
        assertEquals(money,0);
        int money1=playerDao.getPlayerMoney(41);
        Player topPlayer2=new Player("Sasha228","Sasha@gmail.com",1000000,1000,101,69);
        assertEquals(money1,topPlayer2.getMoney());
    }
    @Test
    @Rollback(true)
    public void testGetPlayerLevel() throws Exception{
        int lvl=playerDao.getPlayerLevel(71);
        assertEquals(lvl,0);
        int lvl1=playerDao.getPlayerLevel(41);
        Player topPlayer2=new Player("Sasha228","Sasha@gmail.com",1000000,1000,101,69);
        assertEquals(lvl1,topPlayer2.getLevel());
    }
    @Test
    @Rollback(true)
    public void testGetPlayerPoints() throws Exception{
        int points=playerDao.getPlayerPoints(71);
        assertEquals(points,0);
        int points1=playerDao.getPlayerPoints(41);
        Player topPlayer2=new Player("Sasha228","Sasha@gmail.com",1000000,1000,101,69);
        assertEquals(points1,topPlayer2.getPoints());
    }
    @Test
    @Rollback(true)
    public void testGetPlayerCity() throws Exception{
        City city=playerDao.getPlayerCity(71);
        assertNull(city.getCityName());
        assertNull(city.getMarket());
        City city1=playerDao.getPlayerCity(41);
        Player topPlayer2=new Player("Sasha228","Sasha@gmail.com",1000000,1000,101,69);
        assertEquals(city1.getCityName(),cityDao.find(topPlayer2.getCurCity()).getCityName());

    }

}