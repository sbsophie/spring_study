package com.gn.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
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
	public String selectBoardAll(Model model,SearchDto searchDto,
			PageDto pageDto) {
		
		if(pageDto.getNowPage() == 0) pageDto.setNowPage(1);
		
		// 1. DB에서 목록 SELECT
		Page<Board> resultList = service.selectBoardAll(searchDto,pageDto);
		
		pageDto.setTotalPage(resultList.getTotalPages());
		
		// 2. 목록에 Model에 등록
		model.addAttribute("boardList",resultList);
		model.addAttribute("searchDto", searchDto);
		model.addAttribute("pageDto",pageDto);
		// 3. list.html에 데이터 셋팅
		// 위 3가지를 셋팅해야 목록화면 나옴
		return "board/list";
		
	}
	//목록조회 코드
	@GetMapping("/board/{id}")
	public String selectBoardOne(@PathVariable("id") Long id,Model model) {
		//(@PathVariable("id") Long id,Model model)여기엔 프론트쪽데이터를 받아온것
		//public String 의 String은 화면쪽으로 보내주는거 
		logger.info("게시글 단일 조회 : "+id);
		Board result = service.selectBoardOne(id);
		model.addAttribute("board",result);
		return "board/detail";
	}
	
	//수정화면을 전화하는 코드
	@GetMapping("/board/{id}/update")
	public String updateBoardView(@PathVariable("id") Long id,Model model) {
		Board board = service.selectBoardOne(id);
		model.addAttribute("board",board);
		return "board/update";
	}
	
	//수정하는 코드
	@PostMapping("/board/{id}/update")
	@ResponseBody
	public Map<String,String> updateBoardApi(BoardDto param){
		// Map<String,String>은 JSON데이터를 보내줄때(responce) 사용
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 수정중 오류가 발생하였습니다.");
		//1.BoardDto가 출력되는지 확인
		logger.debug(param.toString());
		//2.BoardService와 BoardRepository를 거쳐서 게시글 수정
		Board result = service.updateBoard(param);
		//3.수정 결과를 Entity가 null이 아니면 성공 그외에는 실패
		if(result != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 수정되었습니다.");
		}
		return resultMap;
	}
	
	//삭제코드
	@DeleteMapping("/board/{id}/delete")
	@ResponseBody
	public Map<String,String> deleteBoardApi(@PathVariable("id") Long id) {
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "게시글 삭제중 오류가 발생하였습니다.");
		
		int result = service.deleteBoard(id);
		if(result >0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "게시글이 삭제되었습니다.");
		}
		return resultMap;
	}
	
	
}
