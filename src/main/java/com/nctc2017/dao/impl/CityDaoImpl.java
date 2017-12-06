package com.nctc2017.dao.impl;

import com.nctc2017.bean.City;
import com.nctc2017.dao.CityDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Qualifier("cityDao")
public class CityDaoImpl implements CityDao {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public City find(int cityId) {
        City city=new City(getCityName(cityId),null);
        return city;
    }

    @Override
    public List<City> findAll() {
        List<City> cities=jdbcTemplate.query("select city.name from objects city, objtype city_type where city_type.name='CITY' and city_type.object_type_id=city.object_type_id", new BeanPropertyRowMapper(City.class));
      return cities;
    }

    @Override
    public String getCityName(int cityId) {
        Map<String, Object> map =jdbcTemplate.queryForMap("select city.name city_name from objects city, objtype city_type where city_type.name='CITY' and city_type.object_type_id=city.object_type_id and city.object_id=?",new Object[]{cityId}, new BeanPropertyRowMapper(City.class));
        String city_name = (String) map.get("city_name");
         return city_name;
    }
}
