package com.nctc2017.bean;

import java.util.*;

/**
 * 
 */
public class Market {
	private static final int INITIAL_VALUE = 10;
    /**
     * 
     */
    protected Map<String, GoodsForSale> bar;

    /**
     * Default constructor
     */
    public Market() {
    	bar = new HashMap<>(INITIAL_VALUE);
    }
    /**
     * @param String nameOfGoods 
     * @param int quantity 
     * @param int price
     */
    public void buy(String nameOfGoods, int quantity, int price) {
        // TODO implement here
    }

    /**
     * @param String nameOfGoods 
     * @param int quantity 
     * @param int price
     */
    public void sale(String nameOfGoods, int quantity, int price) {
        // TODO implement here
    }

    /**
     * @return
     */
    public List<Thing> getAllGoods() {
        // TODO implement here
        return null;
    }

}