package com.nctc2017.bean;

/**
 * 
 */
public class Player {
    /**
     * 
     */
    protected String login;

    /**
     * 
     */
    protected int money;

    /**
     * 
     */
    protected int points;

    /**
     * 
     */
    protected int level;

    /**
     * 
     */
    protected int curCity;
    
    /**
     * Default constructor
     */
    public Player() {
    }

	public Player(String login, int money, int points, int level, int curCity) {
		this.login = login;
		this.money = money;
		this.points = points;
		this.level = level;
		this.curCity = curCity;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getCurCity() {
		return curCity;
	}

	public void setCurCity(int curCity) {
		this.curCity = curCity;
	}
    
    
}