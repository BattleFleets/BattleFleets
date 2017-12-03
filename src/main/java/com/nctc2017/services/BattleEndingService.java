package com.nctc2017.services;

import java.util.*;

import com.nctc2017.bean.Battles;
import com.nctc2017.bean.Goods;

/**
 * 
 */
public class BattleEndingService {

    /**
     * Default constructor
     */
    public BattleEndingService() {
    }

    /**
     * 
     */
    protected Battles battles;

    /**
     * @param int idPlayer 
     * @return
     */
    public List<Goods> passBoardGoodsToWinner(int idPlayer) {
        // TODO implement here
        return null;
    }

    /**
     * @param int idPlayer 
     * @return
     */
    public List<Goods> passDestroyGoodsToWinner(int idPlayer) {
        // TODO implement here
        return null;
    }

    /**
     * @param int idShip 
     * @return
     */
    public boolean destroyShip(int idShip) {
        // TODO implement here
        return false;
    }

    /**
     * @param int idPlayer 
     * @return
     */
    public List<Goods> passSurrenderGoodsToWinner(int idPlayer) {
        // TODO implement here
        return null;
    }

    /**
     * @param int idPlayer 
     * @return
     */
    public int passPayOffGoodsToWinner(int idPlayer) {
        // TODO implement here
        return 0;
    }

}