package com.nctc2017.controllers;

import java.util.*;

import com.nctc2017.bean.Ship;
import org.springframework.security.access.annotation.Secured;

public class TavernController {


    public int sailorCost;


    @Secured("ROLE_USER")
    public List<Ship> getListOfShips(int id, int idHash) {
        // TODO implement here
        return null;
    }

    public int getSailorsNumber(int id, int idHash, int idShip) {
        // TODO implement here
        return 0;
    }

    public int buySailors(int id, int idHash, int idShip) {
        // TODO implement here
        return 0;
    }

    public int getMoney(int id, int idHash) {
        // TODO implement here
        return 0;
    }

}