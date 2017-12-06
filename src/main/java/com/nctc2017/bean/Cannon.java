package com.nctc2017.bean;
public class Cannon {
	protected int id;
	protected String name;
	protected int damage;
	protected int distance;
	protected int cost;
	public Cannon(int id, String name, int damage, int distance, int cost) {
		this.id = id;
		this.name = name;
		this.damage = damage;
		this.distance = distance;
		this.cost = cost;
	}
	public Cannon() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDamage() {
		return damage;
	}
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	@Override
	public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Cannon [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", damage=");
        builder.append(damage);
        builder.append(", distance=");
        builder.append(distance);
        builder.append(", cost=");
        builder.append(cost);
        builder.append("]");
		return builder.toString();
	}
}
