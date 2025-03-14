package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	// 로그백을 써서 기록을 남기겠다는 의미의 코드
	private Logger logger = LoggerFactory.getLogger(BoardController.class);

	//3. 생성자 주입방법 + final
	private final BoardService service;
	
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
		
		// Service가 가지고 있는 createBoard메소드를 호출해야함
		BoardDto result = service.createBoard(dto);
		
		logger.debug("1 : "+result.toString());
		logger.info("2 : "+result.toString());
		logger.warn("3 : "+result.toString());
		logger.error("4 : "+result.toString());
		
		
		return resultMap;
	}
	@GetMapping("/board")
	public String selectBoardAll(Model model,SearchDto searchDto) {
		// 1. DB에서 목록 SELECT
		// 2. 목록에 Model에 등록
		// 3. list.html에 데이터 셋팅
		// 위 3가지를 셋팅해야 목록화면 나옴
		List<Board> resultList = service.selectBoardAll(searchDto);
		model.addAttribute("boardList",resultList);
		model.addAttribute("searchDto", searchDto);
		return "board/list";
	}
}
