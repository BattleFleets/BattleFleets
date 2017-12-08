package com.nctc2017.dao;

import java.util.*;

import com.nctc2017.bean.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nctc2017.bean.Player;

public interface PlayerDao {

    void addNewPlayer(String login, String password, String email);
    
    void findPlayerByLogin(String login);

    void updateLogin(int playerId, String login);

    void updateLevel(int playerId, int level);

    void updatePassword(int playerId, String password);

    void updateEmail(int playerId, String email);

    void updatePoints(int playerId, int points);

    Player findPlayer(int playerId);

    List<Player> findAllPlayers();

    int getCountPlayers();

    String getPlayerLogin(int playerId);

    String getPlayerPassword(int playerId);

    String getPlayerEmail(int playerId);

    int getPlayerMoney(int playerId);

    int getPlayerLevel(int playerId);

    int getPlayerPoints(int playerId);

    City getPlayerCity(int playerId);

    void addShip(int playerId, int shipId);

    void deleteShip(int playerId, int shipId);

    List<Integer> findAllShip(int playerId);



}