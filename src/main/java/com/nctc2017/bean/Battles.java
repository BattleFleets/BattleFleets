package com.nctc2017.bean;

import java.util.*;

/**
 * 
 */
public class Battles {
	private static final int INITIAL_VALUE = 10;
    /**
     * 
     */
	private Map<Integer, Battle> battles;

    /**
     * 
     */
    private Map<Integer, Integer> playerBattle;




    /**
     * Default constructor
     */
    public Battles() {
    	battles = new HashMap<>(INITIAL_VALUE);
    	playerBattle = new HashMap<>(INITIAL_VALUE * 2);
    }
    /**
     * @param int playerId 
     * @return
     */
    public Battle getBattle(int playerId) {
        // TODO implement here
        return null;
    }
    /**
     * 
     * */
    public void addBattle(Battle battle){
        // TODO implement here
    }
}