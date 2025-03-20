package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService service;
	
	//회원탈퇴 기능 코드
	@DeleteMapping("/member/{id}")
	@ResponseBody
	public Map<String,String> memberDeleteApi(@PathVariable("id") Long id,
			HttpServletResponse response){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 탈퇴중 오류가 발생하였습니다.");
		
		int result = service.deleteMember(id);
		if(result > 0) {
			Cookie rememberMe = new Cookie("remember-me",null);
			rememberMe.setMaxAge(0);
			rememberMe.setPath("/");
			response.addCookie(rememberMe);
			
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "정상적으로 회원탈퇴가 되었습니다.");
		}
		return resultMap;
	}
	
	//회원수정 기능 코드
	@PostMapping("/member/{id}/update")
	@ResponseBody
	public Map<String,String> memberUpdateApi(MemberDto param,
			HttpServletResponse response){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원 수정중 오류가 발생하였습니다.");
		
		int result = service.updateMember(param);
		if(result > 0){
			// 쿠키(remember-me) 무효화 코드
			Cookie rememberMe = new Cookie("remember-me",null);
			rememberMe.setMaxAge(0);
			rememberMe.setPath("/");
			response.addCookie(rememberMe);
			
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원정보가 수정되었습니다.");
		}
		return resultMap;
	}
	
	//회원수정페이지로 이동하는 코드
	@GetMapping("/member/{id}/update")
	public String memberUpdateView(@PathVariable("id") Long id, Model model) {
		Member member = service.selectMemberOne(id);
		model.addAttribute("member",member);
		return "member/update";
	}
	
	// 로그인페이지로 이동하는 코드 (+MyLoginFailureHandler코드가 넘어오는 곳)
	@GetMapping("/login")
	public String loginView(
			@RequestParam(value="error", required=false) String error,
			@RequestParam(value="errorMsg", required=false) String errorMsg,
			Model model) {
		model.addAttribute("error",error);
		model.addAttribute("errorMsg",errorMsg);
		return "member/login";
	}
	
	// 회원가입페이지로 이동하는 코드
	@GetMapping("/signup") //화면을 전환하는 메소드
	public String createMemberView() {
		return "/member/create"; //templates>member>create.html로 이동
	}
	
	// 회원가입 기능구현하는 코드
	@PostMapping("/signup")
	@ResponseBody
	public Map<String,String> createMemberApi(MemberDto dto){ //사용자가 입력하는 form태그에 있는 정보
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "회원가입중 오류가 발생하였습니다.");
		System.out.println(dto);
		
		MemberDto saved = service.createMember(dto);
		
		if(saved.getMember_no() != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회원가입이 완료되었습니다.");
		}
		return resultMap;
		
	}
	
}
