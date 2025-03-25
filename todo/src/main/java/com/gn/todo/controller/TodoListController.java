package com.gn.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoListDto;
import com.gn.todo.entity.TodoList;
import com.gn.todo.service.TodoListService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TodoListController {
	
	private final TodoListService service;
	
	// 목록조회
	@GetMapping({"","/","/list"})
	public String selectTodoListAll(Model model,SearchDto searchDto,
			PageDto pageDto) {
		if(pageDto.getNowPage() == 0) pageDto.setNowPage(1);
		Page<TodoList> resultList = service.selectTodoListAll(searchDto,pageDto);
		pageDto.setTotalPage(resultList.getTotalPages());
		
		if(resultList.isEmpty()) {
			resultList = null;
		}
		
		model.addAttribute("todoList",resultList);
		model.addAttribute("searchDto",searchDto);
		model.addAttribute("pageDto",pageDto);
		return "list";
	}
	
	// 할일 추가
	@PostMapping("/todoList")
	@ResponseBody
	public Map<String,String> createTodoListApi(TodoListDto dto){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할일 등록중 오류가 발생했습니다.");
		
		int result = service.createTodoList(dto);
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "할일이 등록되었습니다.");
		}
		return resultMap;
	}
	
	// 할일 삭제
	@DeleteMapping("/todoList/{id}")
	@ResponseBody
	public Map<String,String> deleteTodoListApi(@PathVariable("id") Long id){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할일 삭제중 오류가 발생하였습니다.");
		
		int result = service.deleteTodoList(id);
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "할일이 삭제되었습니다.");
		}
		return resultMap;
	}
	
}
