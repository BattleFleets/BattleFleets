package com.nctc2017.dao.impl;

import com.nctc2017.bean.City;
import com.nctc2017.dao.CityDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
@Qualifier("cityDao")
public class CityDaoImpl implements CityDao {
    public static final String queryForCityNameById="SELECT city.NAME FROM OBJECTS city, OBJTYPE city_type WHERE city_type.NAME=? and city_type.OBJECT_TYPE_ID=city.OBJECT_TYPE_ID and city.OBJECT_ID=?";
    public static final String queryForCity="SELECT city.NAME FROM OBJECTS city, OBJTYPE city_type WHERE city_type.NAME='CITY' AND city_type.OBJECT_TYPE_ID=city.OBJECT_TYPE_ID";
    private static Logger log = Logger.getLogger(CityDaoImpl.class.getName());
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public City find(BigInteger cityId) {
        try{
            jdbcTemplate.queryForObject(queryForCityNameById, new Object[]{"CITY",cityId.longValueExact()}, String.class);
        }
        catch (EmptyResultDataAccessException e) {
            log.log(Level.SEVERE, "City is not exist or cityId is incorrect", e);
            throw e;
        }
       String cityName = jdbcTemplate.queryForObject(queryForCityNameById, new Object[]{"CITY",cityId.longValueExact()}, String.class);
       return new City(cityName, null,cityId);
    }

    @Override
    public List<City> findAll() {
        List<City> cities=new ArrayList<>();
        List<String> citiesNames=jdbcTemplate.queryForList(queryForCity, String.class);
        for(int i=0; i<citiesNames.size();i++)
        {
            BigInteger cityId=jdbcTemplate.queryForObject("SELECT OBJECT_ID FROM OBJECTS WHERE NAME=?",new Object[]{citiesNames.get(i)},BigInteger.class);
            cities.add(new City(citiesNames.get(i),null,cityId));
        }
        return cities;
    }
}
