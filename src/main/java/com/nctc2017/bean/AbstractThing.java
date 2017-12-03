package com.nctc2017.bean;


/**
 * 
 */
public abstract class AbstractThing implements Thing {
    /**
     * 
     */
    protected int quantity;

    /**
     * 
     */
    protected int thingId;
    /**
     * Default constructor
     */
    public AbstractThing() {
    }
    
	public AbstractThing(int quantity, int thingId) {
		super();
		this.quantity = quantity;
		this.thingId = thingId;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getThingId() {
		return thingId;
	}
	public void setThingId(int thingId) {
		this.thingId = thingId;
	}
    
}