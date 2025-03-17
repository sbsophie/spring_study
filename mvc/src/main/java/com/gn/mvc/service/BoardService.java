package com.gn.mvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.BoardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository repository;

	//삭제
	public int deleteBoard(Long id) {
		int result = 0;
		try {
			Board target = repository.findById(id).orElse(null);
			if(target != null) {
				repository.deleteById(id);
			}
			result = 1;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	//수정
	public Board updateBoard(BoardDto param) {
		Board result = null;
		// 1. @Id를 쓴 필드를 기준으로 타겟 조회
		Board target = repository.findById(param.getBoard_no()).orElse(null);
		// 2. 타겟이 존재하는 경우 업데이트
		if(target != null) {
			result = repository.save(param.toEntity());
		}
		return result;
	}
	
	//조회
	public Board selectBoardOne(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	//페이징
	public Page<Board> selectBoardAll(SearchDto searchDto, PageDto pageDto){
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
		
//		Sort sort = Sort.by("regDate").descending();
//		if(searchDto.getOrder_type() == 2) {
//			sort = Sort.by("regDate").ascending();
//		}
		
		Pageable pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").descending());
		if(searchDto.getOrder_type() ==2) {
			pageable = PageRequest.of(pageDto.getNowPage()-1, pageDto.getNumPerPage(), Sort.by("regDate").ascending());
		}
		
		Specification<Board> spec = (root,query,criteriaBuilder) -> null;
		if(searchDto.getSearch_type() == 1) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		} else if(searchDto.getSearch_type() == 2) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		} else if(searchDto.getSearch_type() == 3){
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()))
					.or(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		}
		Page<Board> list = repository.findAll(spec,pageable);
		return list;
		
		
	}
	//
	public BoardDto createBoard(BoardDto dto) {
		// 1. 매개변수dto를 entity로 변경해줘야함
		Board param = dto.toEntity();
		
		// 2. Repository의 save()매소드를 호출해야함
		Board result = repository.save(param);
		
		// 3. 엔티티를 dto로 바꿔주기
		return new BoardDto().toDto(result);
	}


}
