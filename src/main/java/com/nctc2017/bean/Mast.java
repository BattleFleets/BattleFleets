package com.nctc2017.bean;
public class Mast extends AbstractThing {
    protected int speed;
    protected int curSpeed;
    protected int cost;
    
	public Mast(int quantity, int thingId, int speed, int curSpeed, int cost) {
		super(quantity, thingId);
		this.speed = speed;
		this.curSpeed = curSpeed;
		this.cost = cost;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getCurSpeed() {
		return curSpeed;
	}
	public void setCurSpeed(int curSpeed) {
		this.curSpeed = curSpeed;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}

}