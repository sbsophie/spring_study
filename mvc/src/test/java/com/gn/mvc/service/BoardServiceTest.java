package com.gn.mvc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gn.mvc.entity.Board;

@SpringBootTest
class BoardServiceTest {
	
	@Autowired
	private BoardService service;

	@Test
	void selectBoardOne_success() {
		// 1. 예상 데이터
		Long id = 1L;
		Board expected = Board.builder().boardTitle("제목").build();
		// 2. 실제 데이터
		Board real = service.selectBoardOne(id);
		// 3. 비교 및 검증 assertEquals(기대값,실제값);
		assertEquals(expected.getBoardTitle(),real.getBoardTitle());
	}

	// 실패 테스트
	// 존재하지 않는 PK기준으로 조회 요청
	@Test
	void selectBoardOne_fail() {
		Long id = 2L;
		Board expected = null;
		Board real = service.selectBoardOne(id);
		assertEquals(expected,real);
	}
	
}
