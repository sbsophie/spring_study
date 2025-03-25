package com.gn.todo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.todo.dto.PageDto;
import com.gn.todo.dto.SearchDto;
import com.gn.todo.dto.TodoListDto;
import com.gn.todo.entity.TodoList;
import com.gn.todo.repository.TodoListRepository;
import com.gn.todo.specification.TodoListSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoListService {

	private final TodoListRepository repository;

	// 목록조회
	public Page<TodoList> selectTodoListAll(SearchDto searchDto, PageDto pageDto) {
		Pageable pageable 
			= PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage());
		Specification<TodoList> spec = (root, query, criteriaBuilder) -> null;
		spec = spec.and(TodoListSpecification.todoContentContains(searchDto.getSearch_text()));
		Page<TodoList> list = repository.findAll(pageable);
		return list;

	}
	
	// 할일 추가
	public int createTodoList(TodoListDto dto) {
		int result = 0;
		try {
			TodoList entity = dto.toEntity();
			TodoList saved = repository.save(entity);
			if(saved != null) {
				result = 1;				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 할일 삭제
	public int deleteTodoList(Long id) {
		int result = 0;
		try {
			TodoList target = repository.findById(id).orElse(null);
			if(target != null) {
				repository.deleteById(id);
				result = 1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	
	
	
	
}
