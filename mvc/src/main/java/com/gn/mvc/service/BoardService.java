package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.dto.BoardDto;
import com.gn.mvc.dto.PageDto;
import com.gn.mvc.dto.SearchDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.AttachRepository;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.repository.MemberRepository;
import com.gn.mvc.specification.BoardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
//	@Autowired
//	BoardRepository repository

	private final BoardRepository repository;
	private final AttachRepository attachRepository;
	private final AttachService attachService;
	
	//BoardService 클래스 생성 및 기능 추가(CRUD)
//	public BoardDto createBoard(Board board) {
//		Board saved = boardRepository.save(board);
//		return new BoardDto().toDto(saved);
//	}

	// 삭제
	public int deleteBoard(Long id) {
		int result = 0;
		try {
			Board target = repository.findById(id).orElse(null);
			if (target != null) {
				repository.deleteById(id);
			}
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 게시글 수정(CRUD)
//	public Board updateBoard(BoardDto param) {
//		Board result = null;
//		// 1. id를 기준으로 타킷 조회
//		Board target = boardRepository.findById(param.getBoard_no()).orElse(null);
//		// 2. 타깃이 존재하는 경우 업데이트
//		if(target != null) {
//			result = boardRepository.save(param.toEntity());
//		}
//		return result;
//	}
	
	// 하나의 게시글에 들어가서 수정
	@Transactional(rollbackFor = Exception.class)
	public Board updateBoard(BoardDto param,List<AttachDto> attachList) {
		Board result = null;
		try {
			// 1. @Id를 쓴 필드를 기준으로 타겟 조회
			Board target = repository.findById(param.getBoard_no()).orElse(null);
			// 2. 타겟이 존재하는 경우 업데이트
			if (target != null) { 	// 
				// 3. (삭제하고자 하는)파일이 존재하는 경우
				if(param.getDelete_files() != null && !param.getDelete_files().isEmpty()) {
					for(Long attach_no : param.getDelete_files()) {
						// (1) db에서 메타 데이터 삭제(사용자 입장에서 보여지는것)
						if(attachService.deleteMetaData(attach_no) > 0) {
							// (2) 메모리에서 파일 자체 삭제
							attachService.deleteFileData(attach_no);
						}
					}
				}
				Board entity = param.toEntity();
				Board saved = repository.save(entity);
				Long boardNo = saved.getBoardNo();
				if (attachList.size() != 0) {
					for (AttachDto attachDto : attachList) {
						attachDto.setBoard_no(boardNo);
						Attach attach = attachDto.toEntity();
						attachRepository.save(attach);
					}
				}
				result = repository.save(param.toEntity());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 조회
	public Board selectBoardOne(Long id) {
		return repository.findById(id).orElse(null);
	}

	// 검색 , 페이징
	public Page<Board> selectBoardAll(SearchDto searchDto, PageDto pageDto) {
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

		Pageable pageable = PageRequest.of(pageDto.getNowPage() - 1, pageDto.getNumPerPage(),
				Sort.by("regDate").descending());
		if (searchDto.getOrder_type() == 2) {
			pageable = PageRequest.of(pageDto.getNowPage() - 1, pageDto.getNumPerPage(),
					Sort.by("regDate").ascending());
		}

		Specification<Board> spec = (root, query, criteriaBuilder) -> null;
		if (searchDto.getSearch_type() == 1) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		} else if (searchDto.getSearch_type() == 2) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		} else if (searchDto.getSearch_type() == 3) {
			spec = spec.and(BoardSpecification.boardContentContains(searchDto.getSearch_text()))
					.or(BoardSpecification.boardContentContains(searchDto.getSearch_text()));
		}
		Page<Board> list = repository.findAll(spec, pageable);
		return list;
	}

	// 게시글 등록 코드
	@Transactional(rollbackFor = Exception.class)
	public int createBoard(BoardDto dto, List<AttachDto> attachList) {
		int result = 0;
		try {
			// 1. Board 엔티티를 insert해주기
			Board entity = dto.toEntity();
			Board saved = repository.save(entity);
			// 2. insert 결과로 반환받은 PK
			Long boardNo = saved.getBoardNo();
			// 3. 만약에 attachList에 데이터가 있는경우 Attach엔티티도 insert해줘야함
			if (attachList.size() != 0) {
				for (AttachDto attachDto : attachList) {
					attachDto.setBoard_no(boardNo);
					Attach attach = attachDto.toEntity();
					attachRepository.save(attach);
				}
			}
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

//		Board param = dto.toEntity();	 1. 매개변수dto를 entity로 변경해줘야함
//		Board result = repository.save(param);	 2. Repository의 save()매소드를 호출해야함
//		return new BoardDto().toDto(result);	 3. 엔티티를 dto로 바꿔주기

	}

}
