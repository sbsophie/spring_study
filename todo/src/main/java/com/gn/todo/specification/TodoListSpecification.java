package com.gn.todo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.gn.todo.entity.TodoList;

public class TodoListSpecification {
	
	public static Specification<TodoList> todoContentContains(String keyword){
		return (root,query,criteriaBuilder) -> 
			criteriaBuilder.like(root.get("todoContent"),"%"+keyword+"%");
	}
	
}
