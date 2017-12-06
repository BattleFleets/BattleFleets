package com.nctc2017.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nctc2017.bean.Cannon;
import com.nctc2017.dao.CannonDao;

@Repository
@Qualifier("cannonDao")
public class CannonDaoImpl implements CannonDao{
	@Autowired
    JdbcTemplate jdbcTemplate;

 
    public Cannon findById(int cannonId) {
        // TODO implement here
        return null;
    }

 
    public String getName(int cannonId) {
        // TODO implement here
        return "";
    }

 
    public int getCost(int cannonId) {
        // TODO implement here
        return 0;
    }

 
    public int getDistance(int cannonId) {
        // TODO implement here
        return 0;
    }

 
    public int getDamage(int cannonId) {
        // TODO implement here
        return 0;
    }

 
    public List<Cannon> getAllCannonFromStock(int stockId) {
        // TODO implement here
        return null;
    }

 
    public List<Cannon> getAllCannonFromHold(int holdId) {
        // TODO implement here
        return null;
    }

 
    public List<Cannon> getAllCannonFromShip(int shipId) {
        // TODO implement here
        return null;
    }

 
    public int createCannon(int cannonTemplateId) {
        // TODO implement here
        return 0;
    }

 
    public void deleteCannon(int cannonId) {
        // TODO implement here
    }

}