package com.nctc2017.bean;

import java.math.BigInteger;

public class Player {
	protected BigInteger playerId;

    protected String login;

	protected String email;

	protected int money;

    protected int points;

    protected int level;

    protected int curCity;
    

	public Player(BigInteger playerId, String login,String email, int money, int points, int level, int curCity) {
		this.playerId=playerId;
		this.login = login;
		this.email=email;
		this.money = money;
		this.points = points;
		this.level = level;
		this.curCity = curCity;
	}

	public BigInteger getPlayerId() {
		return playerId;
	}

	public void setPlayerId(BigInteger playerId) {
		this.playerId = playerId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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