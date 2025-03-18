package com.gn.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	// home화면 (화면전환 코드)
	@GetMapping({"","/"})
	public String home() {
		// src/main/resources/templates/home.html로 가주세요라는 의미
		return "home";
	}
	
}
