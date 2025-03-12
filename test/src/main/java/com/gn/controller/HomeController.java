package com.gn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	// @GetMapping("/") 이거랑 아래랑 같은거여서 둘 중 하나만 쓰면됨
	// @RequestMapping(value="/", method=RequestMethod.GET)
	@GetMapping({"","/"})
	public String home() {
		// /WEB-INF/views/home.jsp로 감
		return "home";
	}
	
}
