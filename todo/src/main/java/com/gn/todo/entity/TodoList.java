package com.gn.todo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="todo_list")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoList {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long todoNo;
	
	@Column(name="todo_content")
	private String todoContent;
	
	@Column(name="todo_flag")
	private String todoFlag;
	
}
