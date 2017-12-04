package com.nctc2017.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.nctc2017.bean.Cannon;

@Repository
@Qualifier("cannonDao")
public class CannonDaoImpl implements CannonDao{
	@Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * @param int cannonId 
     * @return
     */
    public Cannon findById(int cannonId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int cannonId 
     * @return
     */
    public String getName(int cannonId) {
        // TODO implement here
        return "";
    }

    /**
     * @param int cannonId 
     * @return
     */
    public int getCost(int cannonId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int cannonId 
     * @return
     */
    public int getDistance(int cannonId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int cannonId 
     * @return
     */
    public int getDamage(int cannonId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int stockId 
     * @return
     */
    public List<Cannon> getAllCannonFromStock(int stockId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int holdId 
     * @return
     */
    public List<Cannon> getAllCannonFromHold(int holdId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int shipId 
     * @return
     */
    public List<Cannon> getAllCannonFromShip(int shipId) {
        // TODO implement here
        return null;
    }

    /**
     * @param int cannonTemplateId 
     * @return
     */
    public int createCannon(int cannonTemplateId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int cannonId 
     * @return
     */
    public void deleteCannon(int cannonId) {
        // TODO implement here
    }

}