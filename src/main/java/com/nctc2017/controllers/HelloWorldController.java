package com.nctc2017.controllers;

import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nctc2017.bean.Cannon;
import com.nctc2017.configuration.ApplicationConfig;
import com.nctc2017.services.CannonService;

@Controller
@RequestMapping("/welcome")
public class HelloWorldController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView helloWorld() {
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        CannonService cannonService = (CannonService) context.getBean("cannonService");
        //cannonService.addCannon(new Cannon(5000, "qwertyuiop", 123, 100, 5000));
        Cannon cannon = cannonService.find(37);
		ModelAndView model = new ModelAndView("HelloWorldPage");
		model.addObject("msg", "hello world");
		model.addObject(cannon.getClass().getSimpleName(), cannon.toString());
		
		return model;
	}
}