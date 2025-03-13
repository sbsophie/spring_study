package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {

//	1. 필드 주입방법 -> 이건 순환참조 문제가 있음
//	@Autowired
//	BoardService service;
	
//	2. 메소드(setter) 주입방법 -> 불변성 보장이 안되는 문제가 있음
//	private BoardService service;
//	
//	@Autowired
//	public void setBoardService(BoardService service) {
//		this.service = service;
//	}
	
//	3. 생성자 주입방법 + final
	private final BoardService service;
	
//	@Autowired
//	public BoardController(BoardService service) {
//		this.service = service;
//	}
	
	// nav.html에 적어준 url이 여기로 들어오느거여서 GetMapping에 맞춰줘야함
	@GetMapping("/board/create")  //하나쓸때는 {}안써도됨
	public String createBoardView() {
		return "board/create";
	}
	
	@PostMapping("/board")
	@ResponseBody
	public Map<String,String> createBoardApi(BoardDto dto) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 등록중 오류가 발생하였습니다.");
		System.out.println(dto);
		
		// Service가 가지고 있는 createBoard메소드를 호출해야함
		service.createBoard(dto);
		
		return resultMap;
	}
	
}
