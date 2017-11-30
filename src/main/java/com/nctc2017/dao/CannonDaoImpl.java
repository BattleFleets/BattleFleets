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
 
    public void addCannon(Cannon cannon, int idContainer) {
        jdbcTemplate.update("INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID, SOURCE_ID, NAME) VALUES (?, ?, 8 , 11, 'Mortar')", cannon.getId(), idContainer);
        System.out.println("Cannon Added!!");//TODO
    }
 
    public void editCannon(Cannon cannon, int cannonId) {
        System.out.println("Cannon Updated!!");//TODO
    }
 
    public void deleteCannon(int cannonId) {
        System.out.println("Cannon Deleted!!");//TODO
    }
 
    public Cannon find(int cannonId) {
    	Map<String, Object> row = (Map<String, Object>) jdbcTemplate.queryForMap("SELECT * FROM OBJECTS where OBJECT_ID = ? ",
            new Object[] { cannonId });
    	System.out.println(row);//TODO
        return new Cannon(
        		((BigDecimal)row.get("OBJECT_ID")).intValue(),
        		(String)row.get("NAME"), 
        		0, 
        		0, 
        		0);
    }
 
    public List < Cannon > findAll() {
        List < Cannon > cannons = jdbcTemplate.query("SELECT * FROM OBJECTS WHERE OBJECT_TYPE_ID = 8", new BeanPropertyRowMapper(Cannon.class));
        return cannons;
    }

}
