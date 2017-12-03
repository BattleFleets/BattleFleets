package com.nctc2017.bean;
/**
 * 
 */
public class ShipTemplate {
    /**
     * 
     */
    protected String t_name;

    /**
     * 
     */
    protected int maxHealth;

    /**
     * 
     */
    protected int maxSailorsQuantity;

    /**
     * 
     */
    protected int cost;

    /**
     * 
     */
    protected int maxMastsQuantity;

    /**
     * 
     */
    protected int maxCannonQuantity;

    /**
     * 
     */
    protected int maxCarryingLimit;
    /**
     * Default constructor
     */
    public ShipTemplate() {
    }
	public ShipTemplate(String name, int maxHealth, int maxSailorsQuantity, int cost, int maxMastsQuantity,
			int maxCannonQuantity, int maxCarryingLimit) {
		this.t_name = name;
		this.maxHealth = maxHealth;
		this.maxSailorsQuantity = maxSailorsQuantity;
		this.cost = cost;
		this.maxMastsQuantity = maxMastsQuantity;
		this.maxCannonQuantity = maxCannonQuantity;
		this.maxCarryingLimit = maxCarryingLimit;
	}
	public String getName() {
		return t_name;
	}
	public void setName(String name) {
		this.t_name = name;
	}
	public int getMaxHealth() {
		return maxHealth;
	}
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	public int getMaxSailorsQuantity() {
		return maxSailorsQuantity;
	}
	public void setMaxSailorsQuantity(int maxSailorsQuantity) {
		this.maxSailorsQuantity = maxSailorsQuantity;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getMaxMastsQuantity() {
		return maxMastsQuantity;
	}
	public void setMaxMastsQuantity(int maxMastsQuantity) {
		this.maxMastsQuantity = maxMastsQuantity;
	}
	public int getMaxCannonQuantity() {
		return maxCannonQuantity;
	}
	public void setMaxCannonQuantity(int maxCannonQuantity) {
		this.maxCannonQuantity = maxCannonQuantity;
	}
	public int getMaxCarryingLimit() {
		return maxCarryingLimit;
	}
	public void setMaxCarryingLimit(int maxCarryingLimit) {
		this.maxCarryingLimit = maxCarryingLimit;
	}

}