package com.nctc2017.bean;

import java.math.BigInteger;

public abstract class AbstractThing implements Thing {
    
	protected int quantity;
    protected BigInteger thingId;
    
	public AbstractThing(int quantity, BigInteger thingId) {
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
	public BigInteger getThingId() {
		return thingId;
	}
	public void setThingId(BigInteger thingId) {
		this.thingId = thingId;
	}
    
}