package com.gn.todo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gn.todo.dto.TodoDto;
import com.gn.todo.entity.Todo;
import com.gn.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TodoController {
	
	private final TodoService todoService;
	
	// 할일 수정
	@PostMapping("/todo/{id}/update")
	@ResponseBody
	public Map<String,String> updateTodoApi(@PathVariable("id") Long id){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할일 수정중 오류가 발생했습니다.");
		
		Todo result = todoService.updateTodoOne(id);
		if(result != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "할일이 등록되었습니다.");
		}
		return resultMap;
	}
	
	// 할일 삭제
	@PostMapping("/todo/{id}/delete")
	@ResponseBody
	public Map<String,String> deleteTodoApi(@PathVariable("id") Long id){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할일 삭제중 오류가 발생했습니다.");
		
		int result = todoService.deleteTodoOne(id);
		if(result > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "할일이 삭제되었습니다.");
		}
		return resultMap;
	}
	
	// 할일 추가
	@PostMapping("/todo/create")
	@ResponseBody
	public Map<String,String> createTodoApi(TodoDto dto){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "할일 등록중 오류가 발생했습니다.");
		
		Todo result = todoService.createTodoOne(dto);
		if(result != null) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "할일이 등록되었습니다.");
		}
		return resultMap;
	}
}
