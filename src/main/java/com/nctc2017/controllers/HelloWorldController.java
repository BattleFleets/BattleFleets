package com.nctc2017.controllers;

import com.nctc2017.exception.PlayerValidationException;
import com.nctc2017.services.TravelService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.bean.Player;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.services.AuthRegService;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class HelloWorldController {

    @Resource(name="authRegService")
    private AuthRegService authRegService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView helloWorld() {


        ModelAndView model = new ModelAndView("HelloWorldPage");
        model.addObject("msg", "hello world");
        String messange;
        try {
            Player player = authRegService.authorization("", "");
            messange = player.getLogin();
        } catch (PlayerValidationException e) {
            messange = e.getMessage();
        }
        model.addObject(Player.class.getSimpleName(), messange);

        return model;
    }
}