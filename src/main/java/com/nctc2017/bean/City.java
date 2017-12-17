package com.nctc2017.bean;

import java.math.BigInteger;

public class City {
    protected String cityName;
    protected Market market;
    protected int cityId;
	public City(String cityName, Market market, int cityId) {
		this.cityName = cityName;
		this.market = market;
		this.cityId=cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Market getMarket() {
		return market;
	}
	public void setMarket(Market market) {
		this.market = market;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
}