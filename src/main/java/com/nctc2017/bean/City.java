package com.nctc2017.bean;

/**
 * 
 */
public class City {
    /**
     * 
     */
    protected String cityName;
    /**
     * 
     */
    protected Market market;
    /**
     * Default constructor
     */
    public City() {
    }
	public City(String cityName, Market market) {
		this.cityName = cityName;
		this.market = market;
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
    
}