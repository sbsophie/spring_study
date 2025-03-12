package com.gn.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gn.demo.vo.Member;

@Controller
public class HomeController {
	
	// ModelAndView
	@GetMapping({"","/"})
	public ModelAndView home() {
		// src/main/resources/templates/home.html
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home");
		mav.addObject("age",1); //지정할게 없으면 안써도 문제는 안된다고함
		return mav;
		// 위 코드는 cotroller에서 html페이지한테 보내주는 거임
	}
	
	// 요즘에는 Model을 많이 써서 수업을 이걸로 간다고함
	@GetMapping("/test")
	public String test(Model model) {
		// request.setAttribute("name","김철수");의 코드롸 아래 코드가 같은 코드임
		model.addAttribute("name","김철수");
		return "test";
	}
	
	@GetMapping("/bye")
	public String selectMemberOne(Model model) {
		Member mb = new Member("홍길동",50);
		model.addAttribute("memberInfo",mb);
		// model.addAttribute("",new member("홍길동",50));
		return "/member/listOut";
		
	}
}
