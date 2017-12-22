package com.nctc2017.controllers;


import org.springframework.security.access.annotation.Secured;

public class BattlesController {


    @Secured("ROLE_USER")
    public boolean escape(int id, int idHash) {
        // TODO implement here
        return false;
    }

    @Secured("ROLE_USER")
    public void submit(int id, int idHash, int idShip) {
        // TODO implement here
    }

    @Secured("ROLE_USER")
    public boolean waitForEnemy(int id, int idHash) {
        // TODO implement here
        return false;
    }

    @Secured("ROLE_USER")
    public void getBattle(int id, int idHash) {
        // TODO implement here 
    }

    public void fire(int id, int idHash, int[][] cannonMatrix, boolean convergence) {
        // TODO implement here
    }

    public void fireResults(int id, int idHash) {
        // TODO implement here
    }

    public void bording(int id, int idHash) {
        // TODO implement here
    }

    public void getResource(int id, int idHash) {
        // TODO implement here  
    }

    public void getMoney(int id, int idHash) {
        // TODO implement here 
    }

    public void endBattle(int id, int idHash) {
        // TODO implement here
    }

    public void payoff(int id, int idHash) {
        // TODO implement here
    }

    public void surrender(int id, int idHash) {
        // TODO implement here
    }

}