package com.nctc2017.dao.impl;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.CityDao;
import com.nctc2017.dao.PlayerDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


import static java.lang.Integer.parseInt;

@Repository
@Qualifier("playerDao")
public class PlayerDaoImpl implements PlayerDao{
    public static final String createPlayerFunctionName = "CREATE_PLAYER";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public String addNewPlayer(String login, String password, String email) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName(createPlayerFunctionName);
        String result=call.executeFunction(String.class,login,password,email);
        return result;

    }

    @Override
    public Player findPlayerByLogin(String login) {
        Player player;
        List<String> attributes = jdbcTemplate.queryForList("SELECT VALUE FROM ATTRIBUTES_VALUE,OBJECTS WHERE OBJECTS.OBJECT_ID=ATTRIBUTES_VALUE.OBJECT_ID AND OBJECTS.NAME=? AND ATTRIBUTES_VALUE.ATTR_ID<>?", new Object[]{login, DatabaseAttribute.PASSWORD_ATR_ID}, String.class);
        if (attributes.size() == 0) {
            player=null;
        }
        else {
            int cityId = jdbcTemplate.queryForObject("SELECT PARENT_ID FROM OBJECTS WHERE NAME=?", int.class, login);
            player = new Player(attributes.get(0), attributes.get(4), parseInt(attributes.get(1)), parseInt(attributes.get(3)), parseInt(attributes.get(2)), cityId);
        }
        return player;
    }

    @Override
    public void updateLogin(int playerId,String login) {
        jdbcTemplate.update("UPDATE OBJECTS SET NAME =? WHERE OBJECT_ID=?",login,playerId);
        jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?",login,playerId,DatabaseAttribute.LOGIN_ATR_ID);

    }

    @Override
    public void updateLevel(int playerId,int level) {
        jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?",level,playerId,DatabaseAttribute.LEVEL_ATR_ID);
    }

    @Override
    public void updatePassword(int playerId, String password) {
        jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?",password,playerId,DatabaseAttribute.PASSWORD_ATR_ID);
    }

    @Override
    public void updateEmail(int playerId,String email) {
        jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?",email,playerId,DatabaseAttribute.EMAIL_ATR_ID);

    }

    @Override
    public void updatePoints(int playerId, int points) {
        jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?",points,playerId,DatabaseAttribute.POINTS_ATR_ID);
    }

    @Override
    public Player findPlayerById(int playerId) {
        Player player;
        List<String> attributes = jdbcTemplate.queryForList("SELECT VALUE FROM ATTRIBUTES_VALUE,OBJECTS WHERE OBJECTS.OBJECT_ID=ATTRIBUTES_VALUE.OBJECT_ID AND OBJECTS.OBJECT_ID=? AND OBJECTS.OBJECT_TYPE_ID=? AND ATTRIBUTES_VALUE.ATTR_ID<>?", new Object[]{playerId, DatabaseObject.PLAYER_TEMPLATE_ID, DatabaseAttribute.PASSWORD_ATR_ID}, String.class);
        if (attributes.size() == 0) {
            player=null;
        }
        else {
            int cityId = jdbcTemplate.queryForObject("SELECT PARENT_ID FROM OBJECTS WHERE OBJECT_ID=?", int.class, playerId);
            player = new Player(attributes.get(0), attributes.get(4), parseInt(attributes.get(1)), parseInt(attributes.get(3)), parseInt(attributes.get(2)), cityId);
        }
        return player;
    }


    @Override
    public List<Player> findAllPlayers() {
        List<Player> players=new ArrayList<>();
        List<String> attributes = jdbcTemplate.queryForList("SELECT VALUE FROM ATTRIBUTES_VALUE,OBJECTS WHERE OBJECTS.OBJECT_ID=ATTRIBUTES_VALUE.OBJECT_ID AND OBJECTS.OBJECT_TYPE_ID=? AND ATTRIBUTES_VALUE.ATTR_ID<>?", new Object[]{ DatabaseObject.PLAYER_TEMPLATE_ID, DatabaseAttribute.PASSWORD_ATR_ID}, String.class);
        List<Integer> citiesId = jdbcTemplate.queryForList("SELECT PARENT_ID FROM OBJECTS WHERE OBJECT_TYPE_ID=?", Integer.class, DatabaseObject.PLAYER_TEMPLATE_ID);
        for(int i=0; i<attributes.size();i=i+5) {
            players.add(new Player(attributes.get(i), attributes.get(i+4), parseInt(attributes.get(i+1)), parseInt(attributes.get(i+3)), parseInt(attributes.get(i+2)), citiesId.get(i/5)));
        }
        return players;   
    }

    @Override
    public int getCountPlayers() {
        return findAllPlayers().size();
    }

    @Override
    public String getPlayerLogin(int playerId) {
    if(findPlayerById(playerId)!=null) {
        return findPlayerById(playerId).getLogin();
    }
    else{
        return null;
    }
    }

    @Override
    public String getPlayerPassword(int playerId) {
        List<String> password = jdbcTemplate.queryForList("SELECT VALUE FROM ATTRIBUTES_VALUE WHERE OBJECT_ID=? AND ATTR_ID=?", new Object[]{playerId, DatabaseAttribute.PASSWORD_ATR_ID}, String.class);
        if (password.size() == 0) {
            return null;
        }
        else return password.get(0);

    }


    @Override
    public String getPlayerEmail(int playerId) {
        if(findPlayerById(playerId)!=null) {
            return findPlayerById(playerId).getEmail();
        }
        else{
            return null;
        }
    }

    @Override
    public int getPlayerMoney(int playerId) {
        if(findPlayerById(playerId)!=null) {
            return findPlayerById(playerId).getMoney();
        }
        else{
            return 0;
        }
    }

    @Override
    public int getPlayerLevel(int playerId) {
        if(findPlayerById(playerId)!=null) {
            return findPlayerById(playerId).getLevel();
        }
        else{
            return 0;
        }
    }

    @Override
    public int getPlayerPoints(int playerId) {
        if(findPlayerById(playerId)!=null) {
            return findPlayerById(playerId).getPoints();
        }
        else{
            return 0;
        }
    }

    @Override
    public City getPlayerCity(int playerId) {
        if(findPlayerById(playerId)!=null) {
            String cityName=jdbcTemplate.queryForObject("SELECT city.NAME FROM OBJECTS city, OBJTYPE city_type WHERE city_type.NAME=? and city_type.OBJECT_TYPE_ID=city.OBJECT_TYPE_ID and city.OBJECT_ID=?",new Object[]{"CITY",findPlayerById(playerId).getCurCity()},String.class);
             return new City(cityName,null);
        }
        else {
            return new City(null, null);
        }
    }

    @Override
    public void addShip(int playerId, int shipId) {

    }

    @Override
    public void deleteShip(int playerId, int shipId) {

    }

    @Override
    public List<Integer> findAllShip(int playerId) {
        return null;
    }
}
