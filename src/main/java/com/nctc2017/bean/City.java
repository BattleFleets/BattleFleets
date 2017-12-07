package com.nctc2017.bean;
public class City {
    protected String cityName;
    protected Market market;
    protected int cityID;
	public City(String cityName, Market market,int cityID) {
		this.cityName = cityName;
		this.market = market;
		this.cityID=cityID;
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
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
}