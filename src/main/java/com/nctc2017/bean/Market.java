package com.nctc2017.bean;

import java.util.*;
public class Market {
	private static final int INITIAL_VALUE = 10;
    protected Map<String, GoodsForSale> bar;

    public Market() {
    	bar = new HashMap<>(INITIAL_VALUE);
    }
    public void buy(String nameOfGoods, int quantity, int price) {
        // TODO implement here
    }

    public void sale(String nameOfGoods, int quantity, int price) {
        // TODO implement here
    }

    public List<Thing> getAllGoods() {
        // TODO implement here
        return null;
    }

}