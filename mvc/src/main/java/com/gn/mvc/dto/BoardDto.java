package com.gn.mvc.dto;

import com.gn.mvc.entity.Board;

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
	
	// 1. BoardDto를 Board라는 엔티티로 바꾸기
	public Board toEntity() {
		return Board.builder()
				.boardTitle(board_title)
				.boardContent(board_content)
				.boardNo(board_no)
				.build();
	}
	// 2. Board라는 엔티티를 BoardDto라는 형태로 바꿔주기
	public BoardDto toDto(Board board) {
		return BoardDto.builder()
				.board_title(board.getBoardTitle())
				.board_content(board.getBoardContent())
				.board_no(board.getBoardNo())
				.build();
	}
	
}
