package com.gn.mvc.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.gn.mvc.entity.Board;
import com.gn.mvc.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BoardDto {
	private Long board_no;
	private String board_title;
	private String board_content;
	private Long board_writer;
	private LocalDateTime reg_date;
	private LocalDateTime mod_date;
	
	private List<Long> delete_files;
	private List<MultipartFile> files;
	
	// 1. BoardDto를 Board라는 엔티티로 바꾸기
	public Board toEntity() {
		return Board.builder()
				.boardTitle(board_title) //왼쪽:엔티티의 필드명 , 오른쪽:BoardDto의 필드명
				.boardContent(board_content)
				.boardNo(board_no)
				.member(Member.builder().memberNo(board_writer).build())
				.build();
	}
	// 2. Board라는 엔티티를 BoardDto라는 형태로 바꿔주기
	public BoardDto toDto(Board board) {
		return BoardDto.builder()
				.board_title(board.getBoardTitle())
				.board_content(board.getBoardContent())
				.board_no(board.getBoardNo())
				.reg_date(board.getRegDate())
				.mod_date(board.getModDate())
				.build();
	}
	
}
