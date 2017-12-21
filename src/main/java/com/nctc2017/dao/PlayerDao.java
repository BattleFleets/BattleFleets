package com.nctc2017.dao;

import java.math.BigInteger;
import java.util.*;

import com.nctc2017.bean.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nctc2017.bean.Player;

public interface PlayerDao {

    String addNewPlayer(String login, String password, String email);

    Player findPlayerByLogin(String login);

    void updateLogin(BigInteger playerId, String login);

    void updateLevel(BigInteger playerId, BigInteger level);

    void updatePassword(BigInteger playerId, String password);

    void updateEmail(BigInteger playerId, String email);

    void updatePoints(BigInteger playerId, BigInteger points);

    Player findPlayerById(BigInteger playerId);

    List<Player> findAllPlayers();

    BigInteger getCountPlayers();

    String getPlayerLogin(BigInteger playerId);

    String getPlayerPassword(BigInteger playerId);

    String getPlayerEmail(BigInteger playerId);

    BigInteger getPlayerMoney(BigInteger playerId);

    BigInteger getPlayerLevel(BigInteger playerId);

    BigInteger getPlayerPoints(BigInteger playerId);

    City getPlayerCity(BigInteger playerId);

    void addShip(BigInteger playerId, BigInteger shipId);

    void deleteShip(BigInteger playerId, BigInteger shipId);

    List<BigInteger> findAllShip(BigInteger playerId);

}