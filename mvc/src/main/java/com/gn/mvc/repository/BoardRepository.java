package com.gn.mvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gn.mvc.entity.Board;

public interface BoardRepository extends JpaRepository<Board,Long>{
	// extends JpaRepository<Board,Long>
	// <관리할 엔티티명,PK의 자료형> 적어주면됨
}
