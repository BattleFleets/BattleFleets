package com.nctc2017.dao.impl;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.PlayerDao;

import com.nctc2017.dao.ShipDao;
import com.nctc2017.dao.utils.QueryExecutor;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import static java.lang.Integer.parseInt;

@Repository
@Qualifier("playerDao")
public class PlayerDaoImpl implements PlayerDao{
    public static final String createPlayerFunctionName = "CREATE_PLAYER";
    public static final String queryForPlayerAttributesByLogin="SELECT VALUE FROM ATTRIBUTES_VALUE,OBJECTS " +
            "WHERE OBJECTS.OBJECT_TYPE_ID=? " +
            "AND OBJECTS.OBJECT_ID=ATTRIBUTES_VALUE.OBJECT_ID " +
            "AND OBJECTS.NAME=? " +
            "AND ATTRIBUTES_VALUE.ATTR_ID<>?";
    public static final String queryForPlayerCityIdByLogin="SELECT PARENT_ID FROM OBJECTS WHERE NAME=? AND OBJECT_TYPE_ID=?";
    public static final String queryForPlayerIdByLogin="SELECT OBJECT_ID FROM OBJECTS WHERE OBJECT_TYPE_ID=? AND NAME=?";
    public static final String queryForPlayerAttributesById="SELECT VALUE FROM ATTRIBUTES_VALUE,OBJECTS " +
            "WHERE OBJECTS.OBJECT_ID=ATTRIBUTES_VALUE.OBJECT_ID " +
            "AND OBJECTS.OBJECT_ID=? AND OBJECTS.OBJECT_TYPE_ID=? " +
            "AND ATTRIBUTES_VALUE.ATTR_ID<>?";
    public static final String queryForPlayerCityIDById="SELECT PARENT_ID FROM OBJECTS WHERE OBJECT_ID=? AND OBJECT_TYPE_ID=?";
    public static final String queryForPlayersAttributes="SELECT VALUE FROM ATTRIBUTES_VALUE,OBJECTS " +
            "WHERE OBJECTS.OBJECT_ID=ATTRIBUTES_VALUE.OBJECT_ID " +
            "AND OBJECTS.OBJECT_TYPE_ID=? " +
            "AND ATTRIBUTES_VALUE.ATTR_ID<>?";
    public static final String queryForCityName="SELECT city.NAME FROM OBJECTS city, OBJTYPE city_type " +
            "WHERE city_type.NAME=? " +
            "AND city_type.OBJECT_TYPE_ID=city.OBJECT_TYPE_ID " +
            "AND city.OBJECT_ID=?";
    private static Logger log = Logger.getLogger(PlayerDaoImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private QueryExecutor queryExecutor;

    @Override
    public String addNewPlayer(@NotNull String login,@NotNull String password,@NotNull String email) {
        SimpleJdbcCall call = new SimpleJdbcCall(jdbcTemplate).withFunctionName(createPlayerFunctionName);
        String result=call.executeFunction(String.class,login,password,email);
        return result;

    }

    @Override
    public Player findPlayerByLogin(@NotNull String login) {
        try {
            jdbcTemplate.queryForList(queryForPlayerAttributesByLogin, new Object[]{DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact(), login, DatabaseAttribute.PASSWORD_ATR_ID.longValueExact()}, String.class);
        }
        catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Player is not exist or login is incorrect",e);
        }
        List<String>  attributes=jdbcTemplate.queryForList(queryForPlayerAttributesByLogin, new Object[]{DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact(),login, DatabaseAttribute.PASSWORD_ATR_ID.longValueExact()}, String.class);
        BigInteger playerId=jdbcTemplate.queryForObject(queryForPlayerIdByLogin,BigInteger.class,DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact(),login);
        BigInteger cityId = jdbcTemplate.queryForObject(queryForPlayerCityIdByLogin, BigInteger.class, login, DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact());
        Player player = new Player(playerId.intValue(),attributes.get(0), attributes.get(4),  parseInt(attributes.get(1)), parseInt(attributes.get(3)), parseInt(attributes.get(2)), cityId.intValue());
        return player;
    }

    @Override
    public void updateLogin(@NotNull BigInteger playerId,@NotNull String login) {
            findPlayerById(playerId);
            jdbcTemplate.update("UPDATE OBJECTS SET NAME =? WHERE OBJECT_ID=?", login, playerId.longValueExact());
            jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?", login, playerId.longValueExact(), DatabaseAttribute.LOGIN_ATR_ID.longValueExact());

    }

    @Override
    public void updateLevel(@NotNull BigInteger playerId,@NotNull BigInteger level) {
        findPlayerById(playerId);
        jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?", level.longValueExact(), playerId.longValueExact(), DatabaseAttribute.LEVEL_ATR_ID.longValueExact());
    }

    @Override
    public void updatePassword(@NotNull BigInteger playerId,@NotNull String password) {
        findPlayerById(playerId);
        jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?", password, playerId.longValueExact(), DatabaseAttribute.PASSWORD_ATR_ID.longValueExact());

    }

    @Override
    public void updateEmail(@NotNull BigInteger playerId,@NotNull String email) {
        findPlayerById(playerId);
            jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?", email, playerId.longValueExact(), DatabaseAttribute.EMAIL_ATR_ID.longValueExact());

    }

    @Override
    public void updatePoints(@NotNull BigInteger playerId,@NotNull BigInteger points) {
        findPlayerById(playerId);
        jdbcTemplate.update("UPDATE ATTRIBUTES_VALUE SET VALUE =? WHERE OBJECT_ID=? AND ATTR_ID=?", points.longValueExact(), playerId.longValueExact(), DatabaseAttribute.POINTS_ATR_ID.longValueExact());

    }

    @Override
    public Player findPlayerById(@NotNull BigInteger playerId) {
        try {
            jdbcTemplate.queryForList(queryForPlayerAttributesById, new Object[]{playerId.longValueExact(), DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact(), DatabaseAttribute.PASSWORD_ATR_ID.longValueExact()}, String.class);
        }
        catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Player is not exist or playerId is incorrect",e);
        }
            List<String> attributes = jdbcTemplate.queryForList(queryForPlayerAttributesById, new Object[]{playerId.longValueExact(), DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact(), DatabaseAttribute.PASSWORD_ATR_ID.longValueExact()}, String.class);
            BigInteger cityId = jdbcTemplate.queryForObject(queryForPlayerCityIDById, BigInteger.class, playerId.longValueExact(), DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact());
            Player player = new Player(playerId.intValue(),attributes.get(0), attributes.get(4), parseInt(attributes.get(1)), parseInt(attributes.get(3)), parseInt(attributes.get(2)), cityId.intValue());
            return player;
    }



    @Override
    public List<Player> findAllPlayers() {
        List<Player> players=new ArrayList<>();
        List<String> attributes = jdbcTemplate.queryForList(queryForPlayersAttributes, new Object[]{ DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact(), DatabaseAttribute.PASSWORD_ATR_ID.longValueExact()}, String.class);
        List<BigInteger> citiesId = jdbcTemplate.queryForList("SELECT PARENT_ID FROM OBJECTS WHERE OBJECT_TYPE_ID=?", BigInteger.class, DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact());
        List<BigInteger> playersId = jdbcTemplate.queryForList("SELECT OBJECT_ID FROM OBJECTS WHERE OBJECT_TYPE_ID=?", BigInteger.class, DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact());
        for(int i=0; i<attributes.size();i=i+5) {
            players.add(new Player(playersId.get(i/5).intValue(),attributes.get(i), attributes.get(i+4), parseInt(attributes.get(i+1)),parseInt(attributes.get(i+3)), parseInt(attributes.get(i+2)), citiesId.get(i/5).intValue()));
        }
        return players;   
    }

    @Override
    public BigInteger getCountPlayers() {
        return new BigInteger(Integer.toString(findAllPlayers().size()));

    }

    @Override
    public String getPlayerLogin(@NotNull BigInteger playerId) {
      findPlayerById(playerId);
      return findPlayerById(playerId).getLogin();
    }

    @Override
    public String getPlayerPassword(@NotNull BigInteger playerId) {
        findPlayerById(playerId);
        String password = jdbcTemplate.queryForObject("SELECT VALUE FROM ATTRIBUTES_VALUE WHERE OBJECT_ID=? AND ATTR_ID=?", new Object[]{playerId.longValueExact(), DatabaseAttribute.PASSWORD_ATR_ID.longValueExact()}, String.class);
        return password;
    }


    @Override
    public String getPlayerEmail(@NotNull BigInteger playerId) {
        findPlayerById(playerId);
        return findPlayerById(playerId).getEmail();

    }

    @Override
    public BigInteger getPlayerMoney(@NotNull BigInteger playerId) {
        findPlayerById(playerId);
        return new BigInteger(Integer.toString(findPlayerById(playerId).getMoney()));
    }

    @Override
    public BigInteger getPlayerLevel(@NotNull BigInteger playerId) {
        findPlayerById(playerId);
        return new BigInteger(Integer.toString(findPlayerById(playerId).getLevel()));

    }

    @Override
    public BigInteger getPlayerPoints(@NotNull BigInteger playerId) {
        findPlayerById(playerId);
        return new BigInteger(Integer.toString(findPlayerById(playerId).getPoints()));
    }

    @Override
    public City getPlayerCity(@NotNull BigInteger playerId) {
        findPlayerById(playerId);
        String cityName=jdbcTemplate.queryForObject(queryForCityName,new Object[]{"CITY",findPlayerById(playerId).getCurCity()},String.class);
        return new City(cityName,null,findPlayerById(playerId).getCurCity());

    }

    @Override
    public void addShip(@NotNull BigInteger playerId,@NotNull BigInteger shipId) {
        findPlayerById(playerId);
        jdbcTemplate.update("UPDATE OBJECTS SET PARENT_ID=? WHERE OBJECT_ID=?",playerId,shipId);
    }

    @Override
    public void deleteShip(@NotNull BigInteger playerId,@NotNull BigInteger shipId) {
        findPlayerById(playerId);
        jdbcTemplate.update("UPDATE OBJECTS SET PARENT_ID=? WHERE OBJECT_ID=? AND PARENT_ID=?",null,shipId,playerId);
    }

    @Override
    public List<BigInteger> findAllShip(@NotNull BigInteger playerId) {
        findPlayerById(playerId);
        List<BigInteger> ships=jdbcTemplate.queryForList("SELECT OBJECT_ID FROM OBJECTS WHERE PARENT_ID=? AND OBJECT_TYPE_ID=?",BigInteger.class,playerId.longValueExact(),DatabaseObject.SHIP_OBJTYPE_ID.longValueExact());
        return ships;
    }
    
    @Override
    public void movePlayerToCity(@NotNull BigInteger playerId, @NotNull BigInteger cityId) {
        int res = queryExecutor.putEntityToContainer(cityId, playerId, DatabaseObject.CITY_OBJTYPE_ID);
        if (res != 1) {
            RuntimeException ex = new IllegalArgumentException("Wrong city object id = " + cityId);
            log.log(Level.ERROR, "PlayerDAO Exception while moving player with id = " + playerId, ex);
            throw ex;
        }
    }
}
