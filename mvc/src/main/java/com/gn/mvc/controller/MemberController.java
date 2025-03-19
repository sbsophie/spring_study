package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

	private final MemberService service;
	
	// 로그인페이지로 이동하는 코드
	@GetMapping("/login")
	public String loginView() {
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
