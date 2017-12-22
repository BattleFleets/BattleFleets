package com.nctc2017.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CityActionsController {


    @Secured("ROLE_USER")
    public String checkSailors(int id, int idHash) {
        // TODO implement here
        return "";
    }

    @Secured("ROLE_USER")
    public String checkSpeed(int id, int idHash) {
        // TODO implement here
        return "";
    }

    @Secured("ROLE_USER")
    public boolean stokIsEmpty(int id, int idHash) {
        // TODO implement here
        return false;
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/city**", method = RequestMethod.GET)
    public ModelAndView getCity() {
        ModelAndView model = new ModelAndView();
        model.addObject("msg", "This is protected page - Only for Users!");
        model.setViewName("CityView");
        return model;
    }

}