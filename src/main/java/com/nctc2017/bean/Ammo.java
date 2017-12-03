package com.nctc2017.bean;

/**
 * 
 */
public class Ammo extends AbstractThing {
    /**
     * 
     */
    protected String name;

    /**
     * 
     */
    protected int cost;

    /**
     * Default constructor
     */
    public Ammo() {
    }

	public Ammo(int quantity, int thingId, String name, int cost) {
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