package com.nctc2017.dao.Impl;

import com.nctc2017.bean.Player;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.PlayerDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationConfig.class })
@Transactional
public class PlayerDaoImplTest {
    @Autowired
    PlayerDao playerDao;
    @Autowired
    CityDao cityDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
         Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",100,1,1);
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

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updateLoginIncorrectId() throws Exception {
        playerDao.updateLogin(new BigInteger("100"),"Qwerty");
    }

    @Test
    @Rollback(true)
    public void updateLevel() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        playerDao.updateLevel(player.getPlayerId(),80);
        int level=playerDao.findPlayerByLogin("Steve").getLevel();
        assertEquals(80,level);
    }
    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updateLevelIncorrectId() throws Exception {
        playerDao.updateLevel(new BigInteger("100"),80);
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

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updateEmailIncorrectId() throws Exception {
        playerDao.updateEmail(new BigInteger("100"),"80");
    }

    @Test
    @Rollback(true)
    public void updatePoints() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        playerDao.updatePoints(player.getPlayerId(),100);
        int points=playerDao.findPlayerByLogin("Steve").getPoints();
        assertEquals(100,points);
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updatePointsIncorrectId() throws Exception {
        playerDao.updatePoints(new BigInteger("100"),80);
    }

    @Test
    @Rollback(true)
    public void updatePassword() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        playerDao.updatePassword(player.getPlayerId(),"qwerty");
        String password=playerDao.getPlayerPassword(player.getPlayerId());
        assertEquals("qwerty",password);
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updatePasswordIncorrectId() throws Exception {
        playerDao.updatePassword(new BigInteger("100"),"80");
    }

    @Test
    @Rollback(true)
    public void updateMoney() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        playerDao.updateMoney(player.getPlayerId(),300);
        int money=playerDao.findPlayerByLogin("Steve").getMoney();
        assertEquals(300,money);
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updateMoneyIncorrectId() throws Exception {
        playerDao.updateMoney(new BigInteger("100"),80);
    }

    @Test
    @Rollback(true)
    public void updatePassiveIncome() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        BigInteger playerId=player.getPlayerId();
        playerDao.updateLevel(playerId,5);
        playerDao.updatePassiveIncome(playerId,150);
        int money=playerDao.getCurrentPassiveIncome(playerId);
        assertEquals(150,money);
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updatePassiveIncomeFailed() throws Exception{
        playerDao.updatePassiveIncome(new BigInteger("100") ,150);
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updatePassiveIncomeFailedLvl() throws Exception {
        playerDao.addNewPlayer("Steve", "1111", "Rogers@gmail.com");
        Player player = playerDao.findPlayerByLogin("Steve");
        BigInteger playerId = player.getPlayerId();
        playerDao.updatePassiveIncome(playerId, 150);
    }

    @Test
    @Rollback(true)
    public void updateMaxShips() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player player=playerDao.findPlayerByLogin("Steve");
        BigInteger playerId=player.getPlayerId();
        playerDao.updateLevel(playerId,5);
        playerDao.updateMaxShips(playerId,4);
        int ships=playerDao.getCurrentMaxShips(playerId);
        assertEquals(4,ships);
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updateMaxShipsFailed() throws Exception{
        playerDao.updateMaxShips(new BigInteger("100"),4 );
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void updateMaxShipsFailedLvl() throws Exception {
        playerDao.addNewPlayer("Steve", "1111", "Rogers@gmail.com");
        Player player = playerDao.findPlayerByLogin("Steve");
        BigInteger playerId = player.getPlayerId();
        playerDao.updateMaxShips(playerId, 4);

    }

    @Test
    @Rollback(true)
    public void findPlayerById() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        Player topPlayer1=playerDao.findPlayerById(topPlayer.getPlayerId());
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",100,1,1);
        assertEquals(topPlayer1.getLogin(),topPlayer2.getLogin());
        assertEquals(topPlayer1.getEmail(),topPlayer2.getEmail());
        assertEquals(topPlayer1.getLevel(),topPlayer2.getLevel());
        assertEquals(topPlayer1.getPoints(),topPlayer2.getPoints());
        assertEquals(topPlayer1.getMoney(),topPlayer2.getMoney());
        //assertEquals(topPlayer1.getCurCity(),topPlayer2.getCurCity());
    }
    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void findPlayerByIdNotExistPlayer() throws Exception {
        Player player=playerDao.findPlayerById(new BigInteger("53"));
    }


    @Test
    @Rollback(true)
    public void findAllPlayer() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        int j=0;
        List<Player> players=playerDao.findAllPlayers();
        for(int i=0; i<players.size();i++)
        {
            if(players.get(i).getLogin().compareTo("Steve")==0){
                j=i;
            }
        }
        Player topPlayer=players.get(j);
        assertEquals(topPlayer.getLogin(),"Steve");
        assertEquals(topPlayer.getEmail(),"Rogers@gmail.com");
        assertEquals(topPlayer.getLevel(),1);
        assertEquals(topPlayer.getPoints(),1);
        assertEquals(topPlayer.getMoney(),100);
    }

    @Test
    @Rollback(true)
    public void getCountPlayers() throws Exception{
        assertEquals(playerDao.getCountPlayers(),5);

    }

    @Test
    @Rollback(true)
    public void getPlayerLogin() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        String login=playerDao.getPlayerLogin(topPlayer.getPlayerId());
        Player topPlayer1=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",100,1,1);
        assertEquals(login,topPlayer1.getLogin());
    }

    @Test(expected = IllegalArgumentException.class)
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

    @Test(expected = IllegalArgumentException.class)
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
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",100,1,1);
        assertEquals(email,topPlayer2.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void getPlayerEmailFailed() throws Exception{
        playerDao.getPlayerEmail(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerMoney() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        int money=playerDao.getPlayerMoney(topPlayer.getPlayerId());
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",100,1,1);
        assertEquals(money,topPlayer2.getMoney());
    }
    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void getPlayerMoneyFailed() throws Exception{
        playerDao.getPlayerMoney(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerLevel() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        int lvl=playerDao.getPlayerLevel(topPlayer.getPlayerId());
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",100,1,1);
        assertEquals(lvl,topPlayer2.getLevel());
    }
    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void getPlayerLevelFailed() throws Exception{
        playerDao.getPlayerLevel(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerPoints() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        Player topPlayer=playerDao.findPlayerByLogin("Steve");
        int points=playerDao.getPlayerPoints(topPlayer.getPlayerId());
        Player topPlayer2=new Player(new BigInteger("1"),"Steve","Rogers@gmail.com",100,1,1);
        assertEquals(points,topPlayer2.getPoints());
    }
    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void getPlayerPointsFailed() throws Exception{
        playerDao.getPlayerPoints(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void getPlayerCity() throws Exception{
        playerDao.getPlayerCity(new BigInteger("41"));
        assertEquals(new BigInteger("69"),playerDao.getPlayerCity(new BigInteger("41")));
    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void getPlayerCityFailed() throws Exception{
        playerDao.getPlayerCity(new BigInteger("80"));
    }

    @Test
    @Rollback(true)
    public void findAllShips() throws Exception{
      playerDao.findAllShip(new BigInteger("41"));

    }

    @Test(expected = IllegalArgumentException.class)
    @Rollback(true)
    public void findAllShipsFailed() throws Exception{
        playerDao.findAllShip(new BigInteger("71"));
    }

    @Test
    @Rollback(true)
    public void movePlayerToCity() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        BigInteger playerId=playerDao.findPlayerByLogin("Steve").getPlayerId();
        BigInteger cityId=playerDao.getPlayerCity(playerId);
        if(cityId.intValue()>69)
        {
            cityId=new BigInteger("69");
            playerDao.movePlayerToCity(playerId, cityId);
            assertEquals(playerDao.getPlayerCity(playerId).intValue(),69);
        }
        else{
            cityId=new BigInteger("73");
            playerDao.movePlayerToCity(playerId, cityId);
            assertEquals(playerDao.getPlayerCity(playerId).intValue(),73);

        }
    }
    @Test(expected=IllegalArgumentException.class)
    @Rollback(true)
    public void movePlayerToCityFailed() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        playerDao.movePlayerToCity(playerDao.findPlayerByLogin("Steve").getPlayerId(), new BigInteger(Integer.toString(52)));

    }

    @Test
    @Rollback(true)
    public void getPasswordByEmail() throws Exception{
     playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
     Player player=playerDao.findPlayerByLogin("Steve");
     String password=playerDao.getPasswordByEmail(player.getEmail());
     assertEquals(password,"1111");
    }

    @Test(expected=IllegalArgumentException.class)
    @Rollback(true)
    public void getPasswordByEmailFailed() throws Exception{
        playerDao.getPasswordByEmail("qwerty");

    }

    @Test
    @Rollback(true)
    public void getCurrentPassiveIncome() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        BigInteger playerId=playerDao.findPlayerByLogin("Steve").getPlayerId();
        int pas_inc=playerDao.getCurrentPassiveIncome(playerId);
        assertEquals(pas_inc,100);
    }
    @Test(expected=IllegalArgumentException.class)
    @Rollback(true)
    public void getCurrentPassiveIncomeFailed() throws Exception{
        playerDao.getCurrentPassiveIncome(new BigInteger("99"));
    }

    @Test
    @Rollback(true)
    public void getCurrentMaxShips() throws Exception{
        playerDao.addNewPlayer("Steve","1111","Rogers@gmail.com");
        BigInteger playerId=playerDao.findPlayerByLogin("Steve").getPlayerId();
        int ships=playerDao.getCurrentMaxShips(playerId);
        assertEquals(ships,3);
    }
    @Test(expected=IllegalArgumentException.class)
    @Rollback(true)
    public void getCurrentMaxShipsFailed() throws Exception{
        playerDao.getCurrentMaxShips(new BigInteger("99"));
    }
}