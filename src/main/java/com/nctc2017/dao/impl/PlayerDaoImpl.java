package com.nctc2017.dao.impl;

import com.nctc2017.bean.City;
import com.nctc2017.bean.Player;
import com.nctc2017.constants.DatabaseAttribute;
import com.nctc2017.constants.DatabaseObject;
import com.nctc2017.dao.PlayerDao;

import com.nctc2017.dao.ShipDao;
import com.nctc2017.dao.extractors.EntityExtractor;
import com.nctc2017.dao.extractors.ExtractingVisitor;
import com.nctc2017.dao.utils.QueryBuilder;
import com.nctc2017.dao.utils.QueryExecutor;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public static final String queryForPlayerIdByLogin="SELECT OBJECT_ID FROM OBJECTS WHERE OBJECT_TYPE_ID=? AND NAME=?";
    public static final String queryForPlayersAttributes="SELECT VALUE FROM ATTRIBUTES_VALUE,OBJECTS " +
            "WHERE OBJECTS.OBJECT_ID=ATTRIBUTES_VALUE.OBJECT_ID " +
            "AND OBJECTS.OBJECT_TYPE_ID=? " +
            "AND ATTRIBUTES_VALUE.ATTR_ID<>?";
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
        Player player = new Player(playerId,attributes.get(0), attributes.get(4),  parseInt(attributes.get(1)), parseInt(attributes.get(3)), parseInt(attributes.get(2)));
        return player;
    }

    @Override
    public void updateLogin(@NotNull BigInteger playerId,@NotNull String login) {
            findPlayerById(playerId);
            jdbcTemplate.update("UPDATE OBJECTS SET NAME =? WHERE OBJECT_ID=?", login, playerId.longValueExact());
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(playerId)
                .setAttribute(DatabaseAttribute.LOGIN_ATR_ID, login)
                .build();
        jdbcTemplate.update(psc);
    }

    @Override
    public void updateLevel(@NotNull BigInteger playerId,@NotNull int level) {
        findPlayerById(playerId);
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(playerId)
                .setAttribute(DatabaseAttribute.LEVEL_ATR_ID, level)
                .build();
        jdbcTemplate.update(psc);
    }

    @Override
    public void updatePassword(@NotNull BigInteger playerId,@NotNull String password) {
        findPlayerById(playerId);
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(playerId)
                .setAttribute(DatabaseAttribute.PASSWORD_ATR_ID, password)
                .build();
        jdbcTemplate.update(psc);
    }

    @Override
    public void updateEmail(@NotNull BigInteger playerId,@NotNull String email) {
        findPlayerById(playerId);
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(playerId)
                .setAttribute(DatabaseAttribute.EMAIL_ATR_ID, email)
                .build();
        jdbcTemplate.update(psc);
    }

    @Override
    public void updatePoints(@NotNull BigInteger playerId,@NotNull int points) {
        findPlayerById(playerId);
        PreparedStatementCreator psc = QueryBuilder.updateAttributeValue(playerId)
                .setAttribute(DatabaseAttribute.POINTS_ATR_ID, points)
                .build();
        jdbcTemplate.update(psc);
    }

    @Override
    public Player findPlayerById(@NotNull BigInteger playerId) {

        Player player = queryExecutor.findEntity(playerId,DatabaseObject.PLAYER_OBJTYPE_ID,new EntityExtractor<>(playerId, new PlayerVisitor()));
        if (player == null){
            RuntimeException ex = new IllegalArgumentException("Wrong player object id = " + playerId);
            log.error("PlayerDAO Exception while find by id.", ex);
            throw ex;
        }
        return player;
    }



    @Override
    public List<Player> findAllPlayers() {
        List<Player> players=new ArrayList<>();
        List<String> attributes = jdbcTemplate.queryForList(queryForPlayersAttributes, new Object[]{ DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact(), DatabaseAttribute.PASSWORD_ATR_ID.longValueExact()}, String.class);
        List<BigInteger> playersId = jdbcTemplate.queryForList("SELECT OBJECT_ID FROM OBJECTS WHERE OBJECT_TYPE_ID=?", BigInteger.class, DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact());
        for(int i=0; i<attributes.size();i=i+5) {
            players.add(new Player(playersId.get(i/5),attributes.get(i), attributes.get(i+4), parseInt(attributes.get(i+1)),parseInt(attributes.get(i+3)), parseInt(attributes.get(i+2))));
        }
        return players;   
    }

    @Override
    public int getCountPlayers() {
        return findAllPlayers().size();

    }

    @Override
    public String getPlayerLogin(@NotNull BigInteger playerId) {
        try {
            return queryExecutor.getAttrValue(playerId, DatabaseAttribute.LOGIN_ATR_ID, String.class);
        } catch (EmptyResultDataAccessException e) {
            RuntimeException ex = new IllegalArgumentException("Invalid playerId = " + playerId, e);
            log.error("PlayerDAO Exception while getting player login.", ex);
            throw ex;
        }
    }

    @Override
    public String getPlayerPassword(@NotNull BigInteger playerId) {
        try {
            return queryExecutor.getAttrValue(playerId, DatabaseAttribute.PASSWORD_ATR_ID, String.class);
        } catch (EmptyResultDataAccessException e) {
            RuntimeException ex = new IllegalArgumentException("Invalid playerId = " + playerId, e);
            log.error("PlayerDAO Exception while getting player password.", ex);
            throw ex;
        }
    }


    @Override
    public String getPlayerEmail(@NotNull BigInteger playerId) {
        try {
            return queryExecutor.getAttrValue(playerId, DatabaseAttribute.EMAIL_ATR_ID, String.class);
        } catch (EmptyResultDataAccessException e) {
            RuntimeException ex = new IllegalArgumentException("Invalid playerId = " + playerId, e);
            log.error("PlayerDAO Exception while getting player email.", ex);
            throw ex;
        }

    }

    @Override
    public int getPlayerMoney(@NotNull BigInteger playerId) {
        try {
            return queryExecutor.getAttrValue(playerId, DatabaseAttribute.MONEY_ATR_ID, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            RuntimeException ex = new IllegalArgumentException("Invalid playerId = " + playerId, e);
            log.error("PlayerDAO Exception while getting player money.", ex);
            throw ex;
        }
    }

    @Override
    public int getPlayerLevel(@NotNull BigInteger playerId) {
        try {
            return queryExecutor.getAttrValue(playerId, DatabaseAttribute.LEVEL_ATR_ID, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            RuntimeException ex = new IllegalArgumentException("Invalid playerId = " + playerId, e);
            log.error("PlayerDAO Exception while getting player level.", ex);
            throw ex;
        }

    }

    @Override
    public int getPlayerPoints(@NotNull BigInteger playerId) {
        try {
            return queryExecutor.getAttrValue(playerId, DatabaseAttribute.POINTS_ATR_ID, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            RuntimeException ex = new IllegalArgumentException("Invalid playerId = " + playerId, e);
            log.error("PlayerDAO Exception while getting player points.", ex);
            throw ex;
        }
    }

    @Override
    public BigInteger getPlayerCity(@NotNull BigInteger playerId) {
        try {
            return jdbcTemplate.queryForObject("SELECT PARENT_ID FROM OBJECTS WHERE OBJECT_ID=? AND OBJECT_TYPE_ID=?",new Object[]{playerId.longValueExact(),DatabaseObject.PLAYER_OBJTYPE_ID.longValueExact()},BigInteger.class);
        } catch (EmptyResultDataAccessException e) {
            RuntimeException ex = new IllegalArgumentException("Invalid playerId = " + playerId, e);
            log.error("PlayerDAO Exception while getting player city.", ex);
            throw ex;
        }
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
    private final class PlayerVisitor implements ExtractingVisitor<Player> {

        @Override
        public Player visit(BigInteger entityId, Map<String, String> papamMap) {
            return new Player(entityId,
                    papamMap.get(Player.LOGIN),
                    papamMap.get(Player.EMAIL),
                    Integer.valueOf(papamMap.get(Player.MONEY)),
                    Integer.valueOf(papamMap.get(Player.POINTS)),
                    Integer.valueOf(papamMap.get(Player.LEVEL)));
        }

    }
}
