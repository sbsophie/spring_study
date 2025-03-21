package com.gn.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	// 관리자페이지 화면으로 전환 코드
	@GetMapping("/read")
	public String adminPage() {
		return "admin/home";
	}
	
}
