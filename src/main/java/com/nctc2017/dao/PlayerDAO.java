package com.nctc2017.dao;

import java.util.*;

import com.nctc2017.bean.Player;

/**
 * 
 */
public class PlayerDAO {

    /**
     * Default constructor
     */
    public PlayerDAO() {
    }

    /**
     * @param String login 
     * @param String password 
     * @param String email 
     * @return
     */
    public void addNewPlayer(String login, String password, String email) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @return
     */
    public void updateLogin(int playerId) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @return
     */
    public void updateLevel(int playerId) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @return
     */
    public void updatePassword(int playerId) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @return
     */
    public void updateEmail(int playerId) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @return
     */
    public void updatePoints(int playerId) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @return
     */
    public Player findPlayer(int playerId) {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public List<Player> findAllPlayers() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public int getCountPlayers() {
        // TODO implement here
        return 0;
    }

    /**
     * @param int playerId 
     * @return
     */
    public String getPlayerLogin(int playerId) {
        // TODO implement here
        return "";
    }

    /**
     * @param int playerId 
     * @return
     */
    public String getPlayerPassword(int playerId) {
        // TODO implement here
        return "";
    }

    /**
     * @param int playerId 
     * @return
     */
    public String getPlayerEmail(int playerId) {
        // TODO implement here
        return "";
    }

    /**
     * @param int playerId 
     * @return
     */
    public int getPlayerMoney(int playerId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int playerId 
     * @return
     */
    public int getPlayerLevel(int playerId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int playerId 
     * @return
     */
    public int getPlayerPoints(int playerId) {
        // TODO implement here
        return 0;
    }

    /**
     * @param int playerId 
     * @return
     */
    public void getPlayerCity(int playerId) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @param int shipId 
     * @return
     */
    public void addShip(int playerId, int shipId) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @param int shipId 
     * @return
     */
    public void deleteShip(int playerId, int shipId) {
        // TODO implement here
    }

    /**
     * @param int playerId 
     * @return
     */
    public List<Integer> findAllShip(int playerId) {
        // TODO implement here
        return null;
    }

}