package com.nctc2017.bean;

import java.math.BigInteger;

public class Player {
	protected BigInteger playerId;

    protected String login;

	protected String email;

	protected BigInteger money;

    protected BigInteger points;

    protected BigInteger level;

    protected BigInteger curCity;
    

	public Player(BigInteger playerId, String login,String email, BigInteger money, BigInteger points, BigInteger level, BigInteger curCity) {
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

	public BigInteger getMoney() {
		return money;
	}

	public void setMoney(BigInteger money) {
		this.money = money;
	}

	public BigInteger getPoints() {
		return points;
	}

	public void setPoints(BigInteger points) {
		this.points = points;
	}

	public BigInteger getLevel() {
		return level;
	}

	public void setLevel(BigInteger level) {
		this.level = level;
	}

	public BigInteger getCurCity() {
		return curCity;
	}

	public void setCurCity(BigInteger curCity) {
		this.curCity = curCity;
	}
    
    
}