package com.nctc2017.bean;

import java.math.BigInteger;

public class Ammo extends AbstractThing {
    protected String name;

    protected int cost;


	public Ammo(int quantity, BigInteger thingId, String name, int cost) {
		super(quantity, thingId);
		this.name = name;
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}


}