
package com.gn.mvc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gn.mvc.entity.Board;

public interface BoardRepository extends JpaRepository<Board,Long>,JpaSpecificationExecutor<Board>{
	// extends JpaRepository<Board,Long>
	// <관리할 엔티티명,PK의 자료형> 적어주면됨
	
	//3. Specification 사용
	Page<Board> findAll(Specification<Board> spec, Pageable pageable);
	
	//검색
	// 1. 메소드 네이밍을 이용한 방법
	List<Board> findByBoardTitleContaining(String keyword);
	List<Board> findByBoardContentContaining(String keyword);
	List<Board> findByBoardContentContainingOrBoardTitleContaining(String contentkeyword,String titlekeyword);
	// 정렬적용 -> findByBoardTitleContainingOrderByRegDateDesc
	
	// 2. JPQL을 이용한 방법
	@Query(value="SELECT b FROM Board b WHERE b.boardTitle LIKE CONCAT('%',?1,'%')")
	List<Board> findByTitleLike(String keyword);
	
	@Query(value="SELECT b FROM Board b WHERE b.boardContent LIKE CONCAT('%',?1,'%')")
	List<Board> findByContentLike(String keyword);
	
	@Query(value="SELECT b FROM Board b WHERE b.boardTitle LIKE CONCAT('%',?1,'%') OR b.boardContent LIKE CONCAT('%',?2,'%')")
	List<Board> findByTitleOrContentLike(String title, String content);
	
}
