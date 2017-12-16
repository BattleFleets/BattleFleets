package com.nctc2017.bean;
public class Ship extends ShipTemplate {

	protected int shipId;
	protected String name;
	protected int curHealth;
	protected int curSailorsQuantity;
	protected int curMastsQuantity;
	protected int curCannonQuantity;
	protected int curCarryingLimit;

	public static final String NAME = "CurShipName";
	public static final String CUR_HEALTH = "CurShipHealth";
	public static final String CUR_SAILORS_QUANTITY = "CurShipSailors";



	public Ship(ShipTemplate shipT,int shipId, String cur_name, int curHealth, int curSailorsQuantity, int curMastsQuantity,
				int curCannonQuantity, int curCarryingLimit) {
		super(shipT.t_name, shipT.maxHealth, shipT.maxSailorsQuantity, shipT.cost, shipT.maxMastsQuantity,
				shipT.maxCannonQuantity, shipT.maxCarryingLimit);
		this.shipId = shipId;
		this.name = cur_name;
		this.curHealth = curHealth;
		this.curSailorsQuantity = curSailorsQuantity;
		this.curMastsQuantity = curMastsQuantity;
		this.curCannonQuantity = curCannonQuantity;
		this.curCarryingLimit = curCarryingLimit;
	}


	public Ship(ShipTemplate shipT, int shipId, String cur_name) {
		this(shipT,shipId, cur_name, shipT.maxHealth, shipT.maxSailorsQuantity, shipT.maxMastsQuantity,
				shipT.maxCannonQuantity, shipT.maxCarryingLimit);
	}



	public int getShipId() {
		return shipId;
	}

	public void setShipId(int shipId) {
		this.shipId = shipId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurHealth() {
		return curHealth;
	}

	public void setCurHealth(int curHealth) {
		this.curHealth = curHealth;
	}

	public int getCurSailorsQuantity() {
		return curSailorsQuantity;
	}

	public void setCurSailorsQuantity(int curSailorsQuantity) {
		this.curSailorsQuantity = curSailorsQuantity;
	}

	public int getCurMastsQuantity() {
		return curMastsQuantity;
	}

	public void setCurMastsQuantity(int curMastsQuantity) {
		this.curMastsQuantity = curMastsQuantity;
	}

	public int getCurCannonQuantity() {
		return curCannonQuantity;
	}

	public void setCurCannonQuantity(int curCannonQuantity) {
		this.curCannonQuantity = curCannonQuantity;
	}

	public int getCurCarryingLimit() {
		return curCarryingLimit;
	}

	public void setCurCarryingLimit(int curCarryingLimit) {
		this.curCarryingLimit = curCarryingLimit;
	}

}