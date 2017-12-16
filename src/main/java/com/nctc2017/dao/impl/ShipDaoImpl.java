package com.nctc2017.dao.impl;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.nctc2017.bean.Mast;
import com.nctc2017.bean.Ship;
import com.nctc2017.bean.ShipTemplate;
import com.nctc2017.dao.ShipDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("shipDao")
public class ShipDaoImpl implements ShipDao {

    private static final Logger log = Logger.getLogger(ShipDaoImpl.class.getSimpleName());

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
	public Ship findShip(int shipId) {
        // TODO implement here
        return null;
    }

    @Override
	public BigInteger createNewShip(BigInteger shipTemplateId) {
        // TODO implement here
        return null;
    }

    @Override
	public boolean deleteShip(int shipId) {
        // TODO implement here
        return false;
    }

    @Override
	public boolean updateShipName(int shipId, int newShipName) {
        // TODO implement here
        return false;
    }

    @Override
	public boolean updateShipHealth(int shipId, int newhealthNumb) {
        // TODO implement here
        return false;
    }

    @Override
	public boolean updateShipSailorsNumber(int shipId, int newsailorsNumb) {
        // TODO implement here
        return false;
    }

    @Override
	public String getCurrentShipName(int shipId) {
        // TODO implement here
        return "";
    }

    @Override
	public int getCurrentShipHealth(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
	public int getCurrentShipSailors(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
	public int getHealthLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
	public int getCarryingLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
	public int getCannonLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
	public int getMastLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
	public int getSailorLimit(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
	public int getShipCost(int shipId) {
        // TODO implement here
        return 0;
    }

    @Override
	public List<ShipTemplate> findAllShipTemplates() {
        // TODO implement here
        return null;
    }

    @Override
	public List<Ship> findAllShips(List<Integer> shipsId) {
        // TODO implement here
		return null;
    }

    @Override
	public boolean setMastOnShip(int mastId, int shipId) {
        // TODO implement here
        return false;
    }

    @Override
	public boolean setCannonOnShip(int cannonId, int shipId) {
        // TODO implement here
        return false;
    }

    /*private final class ShipExtractor implements ResultSetExtractor<Mast> {
        private BigInteger shipId;


        public ShipExtractor(BigInteger shipId) {
            this.shipId = shipId;
        }

        @Override
        public Ship extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<String, String> papamMap = new HashMap<>();
            while (rs.next()) {
                papamMap.put(rs.getString(1), rs.getString(2));
            }
            ShipTemplate shipT = new ShipTemplate(
                    papamMap.remove(ShipTemplate.T_SHIPNAME),
                    Integer.valueOf(ShipTemplate.T_MAX_HEALTH),
                    Integer.valueOf(ShipTemplate.T_MAX_SAILORS_QUANTITY),
                    Integer.valueOf(ShipTemplate.T_MAX_COST),
                    Integer.valueOf(ShipTemplate.MAX_MASTS_QUANTITY),
                    Integer.valueOf(ShipTemplate.MAX_CANNON_QUANTITY),
                    Integer.valueOf(ShipTemplate.MAX_CARRYING_LIMIT),
                    Integer.valueOf(ShipTemplate.START_CANNON_TYPE),
                    Integer.valueOf(ShipTemplate.START_MAST_TYPE),
                    Integer.valueOf(ShipTemplate.START_NUM_CANNON),
                    Integer.valueOf(ShipTemplate.START_NUM_MAST));
            return new Ship(shipT,
                    mastId,
                    papamMap.remove(Mast.MAST_NAME),
                    Integer.valueOf(papamMap.remove(Mast.MAX_SPEED)),
                    Integer.valueOf(papamMap.remove(Mast.Cur_MAST_SPEED)),
                    Integer.valueOf(papamMap.remove(Mast.MAST_COST)));
        }
    }*/
}