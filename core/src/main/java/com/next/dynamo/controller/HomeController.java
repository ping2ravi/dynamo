package com.next.dynamo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	@RequestMapping("/")
	@ResponseBody
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView();
        mav.addObject("user", "Ravi");
        mav.setViewName("home");
        return mav;
	}
}
