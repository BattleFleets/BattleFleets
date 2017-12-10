package com.nctc2017.dao;

import java.util.*;

import com.nctc2017.bean.City;

public interface CityDao {

    City find(int cityId);

    List<City> findAll();

}