package com.nctc2017.controllers;

import org.springframework.security.access.annotation.Secured;

public class TravelController {

    @Secured("ROLE_USER")
    public boolean isEnemyOnHorizon(int id, int idHash) {
        // TODO implement here
        return false;
    }

    public void decision(int id, int idHash, String dec) {
        // TODO implement here
    }

    public void isBattleStart(int id, int idHash) {
        // TODO implement here
    }

    public void wantsToRelocate(int id, int idHash, String city) {
        // TODO implement here
    }

    public String getCurrentCity(int id, int idHash) {
        // TODO implement here
        return "";
    }

    public String getCities() {
        // TODO implement here
        return "";
    }

}