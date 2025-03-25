package com.gn.todo.dto;

import java.time.LocalDateTime;

import com.gn.todo.entity.TodoList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TodoListDto {
	private Long todoNo;
	private String todoContent;
	private String todoFlag;

	public TodoList toEntity() {
		return TodoList.builder()
				.todoNo(todoNo)
				.todoContent(todoContent)
				.todoFlag(todoFlag)
				.build();
	}
	
	public TodoList toDto(TodoList todoList) {
		return TodoList.builder()
				.todoNo(todoList.getTodoNo())
				.todoContent(todoList.getTodoContent())
				.todoFlag(todoList.getTodoFlag())
				.build();
	}
	
	
}
