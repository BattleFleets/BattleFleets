package com.nctc2017.bean;

/**
 * 
 */
public class Goods extends AbstractThing {
    /**
     * 
     */
    protected String name;

    /**
     * 
     */
    protected int purchasePrice;
    /**
     * Default constructor
     */
    public Goods() {
    }
	public Goods(int quantity, int thingId, String name, int purchasePrice) {
		super(quantity, thingId);
		this.name = name;
		this.purchasePrice = purchasePrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(int purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	

}