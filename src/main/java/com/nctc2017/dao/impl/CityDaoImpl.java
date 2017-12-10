package com.nctc2017.dao.impl;

import com.nctc2017.bean.City;
import com.nctc2017.dao.CityDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("cityDao")
public class CityDaoImpl implements CityDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public City find(int cityId) {
        List<String> cityName = jdbcTemplate.queryForList("SELECT city.NAME FROM OBJECTS city, OBJTYPE city_type WHERE city_type.NAME=? and city_type.OBJECT_TYPE_ID=city.OBJECT_TYPE_ID and city.OBJECT_ID=?", new Object[]{"CITY",cityId}, String.class);
        if(cityName.size()!=0) {
            City city = new City(cityName.get(0), null);
            return city;
        }
        else return new City(null, null);
    }

    @Override
    public List<City> findAll() {
        List<City> cities=jdbcTemplate.query("SELECT city.NAME FROM OBJECTS city, OBJTYPE city_type WHERE city_type.NAME='CITY' AND city_type.OBJECT_TYPE_ID=city.OBJECT_TYPE_ID", new BeanPropertyRowMapper(City.class));
      return cities;
    }
}
