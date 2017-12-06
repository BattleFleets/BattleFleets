package com.nctc2017.dao.impl;

import com.nctc2017.bean.Player;
import com.nctc2017.dao.PlayerDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Qualifier("playerDao")
public class PlayerDaoImpl implements PlayerDao{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void addNewPlayer(String login, String password, String email) {
        jdbcTemplate.update("insert into OBJECTS(object_id, PARENT_ID,OBJECT_TYPE_ID,SOURCE_ID,NAME) values(obj_sq.nextval, null, 10, null, login)");
        jdbcTemplate.update("insert into ATTRIBUTES_VALUE(ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) values(27, obj_sq.curval, ?, null)",login);
        jdbcTemplate.update("insert into ATTRIBUTES_VALUE(ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) values(28, obj_sq.curval, ?, null)",password);
        jdbcTemplate.update("insert into ATTRIBUTES_VALUE(ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) values(29, obj_sq.curval, ?, null)",100);
        jdbcTemplate.update("insert into ATTRIBUTES_VALUE(ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) values(29, obj_sq.curval, ?, null)",100);
        jdbcTemplate.update("insert into ATTRIBUTES_VALUE(ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) values(30, obj_sq.curval, ?, null)",0);
        jdbcTemplate.update("insert into ATTRIBUTES_VALUE(ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) values(31, obj_sq.curval, ?, null)",0);
        jdbcTemplate.update("insert into ATTRIBUTES_VALUE(ATTR_ID,OBJECT_ID,VALUE,DATE_VALUE) values(41, obj_sq.curval, ?, null)",email);

    }

    @Override
    public void findPlayerByLogin(String login) {
        jdbcTemplate.update("select player.name from objects player, objtype player_type, ATTRIBUTES_VALUE player_value where player_type.name='PLAYER' and player_type.object_type_id=player.object_type_id and player.OBJECT_ID=player_value.OBJECT_ID and player_value.value=?", login);
    }

    @Override
    public void updateLogin(int playerId,String login) {
          jdbcTemplate.update("update ATTRIBUTES_VALUE set value=? where ATTR_ID=27 and OBJECT_ID=? ",login,playerId);
    }

    @Override
    public void updateLevel(int playerId,int level) {
       jdbcTemplate.update("update attributes_value set value=? where ATTR_ID=30 and OBJECT_ID=?",level,playerId);
    }

    @Override
    public void updatePassword(int playerId, String password) {
        jdbcTemplate.update("update attributes_value set value=? where ATTR_ID=28 and OBJECT_ID=?",password,playerId);
    }

    @Override
    public void updateEmail(int playerId,String email) {
        jdbcTemplate.update("update attributes_value set value=? where ATTR_ID=41 and OBJECT_ID=?",email,playerId);
    }

    @Override
    public void updatePoints(int playerId, int points) {
        jdbcTemplate.update("update attributes_value set value=? where ATTR_ID=31 and OBJECT_ID=?",points,playerId);
    }

    @Override
    public Player findPlayer(int playerId) {
        String login= jdbcTemplate.queryForObject("select value from ATTRIBUTES_VALUE where attr_id=27 and object_id=?;", new Object[]{playerId},String.class);
        String email= jdbcTemplate.queryForObject("select value from ATTRIBUTES_VALUE where attr_id=28 and object_id=?;", new Object[]{playerId},String.class);
        int money= jdbcTemplate.queryForObject("select value from ATTRIBUTES_VALUE where attr_id=29 and object_id=?;", new Object[]{playerId},int.class);
        int points= jdbcTemplate.queryForObject("select value from ATTRIBUTES_VALUE where attr_id=31 and object_id=?;", new Object[]{playerId},int.class);
        int level= jdbcTemplate.queryForObject("select value from ATTRIBUTES_VALUE where attr_id=30 and object_id=?;", new Object[]{playerId},int.class);
        int cityID=jdbcTemplate.queryForObject("select parent_id from objects where object_id=?;", new Object[]{playerId},int.class);
        Player player=new Player(login, email, money, points, level, cityID);
        return player;
    }

    @Override
    public List<Player> findAllPlayers() {
        return null;
    }

    @Override
    public int getCountPlayers() {
        return 0;
    }

    @Override
    public String getPlayerLogin(int playerId) {
        return null;
    }

    @Override
    public String getPlayerPassword(int playerId) {
        return null;
    }

    @Override
    public String getPlayerEmail(int playerId) {
        return null;
    }

    @Override
    public int getPlayerMoney(int playerId) {
        return 0;
    }

    @Override
    public int getPlayerLevel(int playerId) {
        return 0;
    }

    @Override
    public int getPlayerPoints(int playerId) {
        return 0;
    }

    @Override
    public void getPlayerCity(int playerId) {

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
