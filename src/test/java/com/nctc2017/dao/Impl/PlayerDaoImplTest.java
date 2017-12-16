package com.nctc2017.dao.Impl;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.dao.CannonDao;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.PlayerDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@Transactional
public class PlayerDaoImplTest {
    @Autowired
    PlayerDao playerDao;


    @Test
    @Rollback(true)
    public void addNewPlayer() throws Exception {
        String succesResult=playerDao.addNewPlayer("qwe","1111","@FWF");
        assertEquals("Registration is successfull",succesResult);
        String existingLogin=playerDao.addNewPlayer("qwe","1111","fghj");
        assertEquals("Login exists, enter another login",existingLogin);
        String existingEmail=playerDao.addNewPlayer("asfdf","1111","@FWF");
        assertEquals("Email exists, enter another email",existingEmail);
    }
    @Test
    @Rollback(true)
    public void findPlayerByLogin() throws Exception{
         playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
         Player topPlayer1=playerDao.findPlayerByLogin("Steve");
         Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",new BigInteger("100"),new BigInteger("1"),new BigInteger("1"),new BigInteger("69"));
         assertEquals(topPlayer1.getLogin(),topPlayer2.getLogin());
         assertEquals(topPlayer1.getEmail(),topPlayer2.getEmail());
         assertEquals(topPlayer1.getLevel(),topPlayer2.getLevel());
         assertEquals(topPlayer1.getPoints(),topPlayer2.getPoints());
         assertEquals(topPlayer1.getMoney(),topPlayer2.getMoney());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void findPlayerByLoginNotExistPlayer() throws Exception {
        Player player=playerDao.findPlayerByLogin("Qwerty");
    }


    @Test
    @Rollback(true)
    public void updateLogin() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        playerDao.updateLogin(player.getPlayerId(),"Captain_America");
        String login=playerDao.findPlayerByLogin("Captain_America").getLogin();
        assertEquals("Captain_America",login);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void updateLoginIncorrectId() throws Exception {
        playerDao.updateLogin(new BigInteger("100"),"Qwerty");
    }

    @Test
    @Rollback(true)
    public void updateLevel() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        playerDao.updateLevel(player.getPlayerId(),new BigInteger("80"));
        BigInteger level=playerDao.findPlayerByLogin("Steve").getLevel();
        assertEquals(new BigInteger("80"),level);
    }
    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void updateLevelIncorrectId() throws Exception {
        playerDao.updateLevel(new BigInteger("100"),new BigInteger("80"));
    }
    @Test
    @Rollback(true)
    public void updateEmail() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        playerDao.updateEmail(player.getPlayerId(),"80");
        String email=playerDao.findPlayerByLogin("Steve").getEmail();
        assertEquals("80",email);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void updateEmailIncorrectId() throws Exception {
        playerDao.updateEmail(new BigInteger("100"),"80");
    }

    @Test
    @Rollback(true)
    public void updatePoints() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        playerDao.updatePoints(player.getPlayerId(),new BigInteger("100"));
        BigInteger points=playerDao.findPlayerByLogin("Steve").getPoints();
        assertEquals(new BigInteger("100"),points);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void updatePointsIncorrectId() throws Exception {
        playerDao.updatePoints(new BigInteger("100"),new BigInteger("80"));
    }
    @Test
    @Rollback(true)
    public void findPlayerById() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        Player topPlayer1=playerDao.findPlayerById(topPlayer.getPlayerId());
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",new BigInteger("100"),new BigInteger("1"),new BigInteger("1"),new BigInteger("69"));
        assertEquals(topPlayer1.getLogin(),topPlayer2.getLogin());
        assertEquals(topPlayer1.getEmail(),topPlayer2.getEmail());
        assertEquals(topPlayer1.getLevel(),topPlayer2.getLevel());
        assertEquals(topPlayer1.getPoints(),topPlayer2.getPoints());
        assertEquals(topPlayer1.getMoney(),topPlayer2.getMoney());
        //assertEquals(topPlayer1.getCurCity(),topPlayer2.getCurCity());
    }
    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void findPlayerByIdNotExistPlayer() throws Exception {
        Player player=playerDao.findPlayerById(new BigInteger("53"));
    }


    @Test
    @Ignore
    @Rollback(true)
    public void findAllPlayer() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        int j=0;
        List<Player> players=playerDao.findAllPlayers();
        for(int i=0; i<players.size();i++)
        {
            if(players.get(i).getLogin()=="Steve"){
                j=i;
            }
        }
        Player topPlayer=players.get(j);
        assertEquals(topPlayer.getLogin(),"Steve");
        assertEquals(topPlayer.getEmail(),"Rogers@gmail.com");
        assertEquals(topPlayer.getLevel(),new BigInteger("1"));
        assertEquals(topPlayer.getPoints(),new BigInteger("1"));
        assertEquals(topPlayer.getMoney(),new BigInteger("100"));
    }

    @Test
    @Rollback(true)
    public void getCountPlayers() throws Exception{
        assertEquals(playerDao.getCountPlayers(),new BigInteger("5"));

    }

    @Test
    @Rollback(true)
    public void getPlayerLogin() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        String login=playerDao.getPlayerLogin(topPlayer.getPlayerId());
        Player topPlayer1=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",new BigInteger("100"),new BigInteger("1"),new BigInteger("1"),new BigInteger("69"));
        assertEquals(login,topPlayer1.getLogin());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void getPlayerLoginFailed() throws Exception{
      playerDao.getPlayerLogin(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerPassword() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        String password=playerDao.getPlayerPassword(topPlayer.getPlayerId());
        assertEquals(password,"1111");
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void testGetPlayerPasswordFailed() throws Exception{
        playerDao.getPlayerPassword(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerEmail() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        String email=playerDao.getPlayerEmail(topPlayer.getPlayerId());
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",new BigInteger("100"),new BigInteger("1"),new BigInteger("1"),new BigInteger("69"));
        assertEquals(email,topPlayer2.getEmail());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void getPlayerEmailFailed() throws Exception{
        playerDao.getPlayerEmail(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerMoney() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        BigInteger money=playerDao.getPlayerMoney(topPlayer.getPlayerId());
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",new BigInteger("100"),new BigInteger("1"),new BigInteger("1"),new BigInteger("69"));
        assertEquals(money,topPlayer2.getMoney());
    }
    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void getPlayerMoneyFailed() throws Exception{
        playerDao.getPlayerMoney(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerLevel() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        BigInteger lvl=playerDao.getPlayerLevel(topPlayer.getPlayerId());
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",new BigInteger("100"),new BigInteger("1"),new BigInteger("1"),new BigInteger("69"));
        assertEquals(lvl,topPlayer2.getLevel());
    }
    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void getPlayerLevelFailed() throws Exception{
        playerDao.getPlayerLevel(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerPoints() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        BigInteger points=playerDao.getPlayerPoints(topPlayer.getPlayerId());
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",new BigInteger("100"),new BigInteger("1"),new BigInteger("1"),new BigInteger("69"));
        assertEquals(points,topPlayer2.getPoints());
    }
    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void getPlayerPointsFailed() throws Exception{
        playerDao.getPlayerPoints(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerCity() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        City city=playerDao.getPlayerCity(topPlayer.getPlayerId());
        assertEquals(topPlayer.getCurCity(),city.getCityId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void getPlayerCityFailed() throws Exception{
        playerDao.getPlayerCity(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void findAllShips() throws Exception{
      playerDao.findAllShip(new BigInteger("41"));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    @Rollback(true)
    public void findAllShipsFailed() throws Exception{
        playerDao.findAllShip(new BigInteger("71"));

    }
}