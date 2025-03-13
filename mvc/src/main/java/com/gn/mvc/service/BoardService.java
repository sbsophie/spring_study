package com.gn.mvc.service;

import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

//	@Autowired
//	BoardRepository repository;
	
	private final BoardRepository repository;
	
	public BoardDto createBoard(BoardDto dto) {
		// 1. 매개변수dto를 entity로 변경해줘야함
		Board param = dto.toEntity();
		
		// 2. Repository의 save()매소드를 호출해야함
		Board result = repository.save(param);
		
		// 3. 엔티티를 dto로 바꿔주기
		return new BoardDto().toDto(result);
		
	}
	
}
