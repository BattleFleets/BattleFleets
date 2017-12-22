package com.nctc2017.controllers;

import com.nctc2017.exception.PlayerValidationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.bean.Player;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.services.AuthRegService;

@Controller
@RequestMapping("/")
public class HelloWorldController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView helloWorld() {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        AuthRegService authRegService = (AuthRegService) context.getBean("authRegService");

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