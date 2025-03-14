package com.gn.mvc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.BoardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository repository;
	
	public List<Board> selectBoardAll(SearchDto searchDto){
//		List<Board> list = new ArrayList<Board>();
//		if(searchDto.getSearch_type() == 1) {
//			// 제목을 기준으로 검색하겠다는 의미
//			list = repository.findByTitleLike(searchDto.getSearch_text());
//		}else if(searchDto.getSearch_type() == 2) {
//			// 내용 기준으로 검색
//			list = repository.findByContentLike(searchDto.getSearch_text());
//		}else if(searchDto.getSearch_type() == 3) {
//			// 제목 또는 내용 기준으로 검색
//			list = repository.findByTitleOrContentLike(searchDto.getSearch_text(),searchDto.getSearch_text());
//		}else {
//			// WHERE절 없이 검색
//			list = repository.findAll();
//		}
//		return list;
		Specification<Board> spec = (root,query,criteriaBuilder) -> null;
		if(searchDto.getSearch_type() == 1) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		} else if(searchDto.getSearch_type() == 2) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		} else if(searchDto.getSearch_type() == 3){
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()))
					.or(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		}
		List<Board> list = repository.findAll(spec);
		return list;
		
		
	}
	
	public BoardDto createBoard(BoardDto dto) {
		// 1. 매개변수dto를 entity로 변경해줘야함
		Board param = dto.toEntity();
		
		// 2. Repository의 save()매소드를 호출해야함
		Board result = repository.save(param);
		
		// 3. 엔티티를 dto로 바꿔주기
		return new BoardDto().toDto(result);
		
	}

	
}
