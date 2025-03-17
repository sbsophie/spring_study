package com.gn.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PageDto {
	
	private int numPerPage=5;	//한 페이지에 보이는 데이터 개수
	private int nowPage;	//현재 페이지
	private int pageBarSize = 5;	//페이징바
	private int pageBarStart;
	private int pageBarEnd;
	private boolean prev = true;	//이전,다음 여부
	private boolean next = true;
	
	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}
	
	private int totalPage;
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
		calcPaging();
	}
	
	private void calcPaging() {
		// 1. 페이징바 시작번호 계산
		pageBarStart = ((nowPage-1)/pageBarSize)*pageBarSize +1;
		// 2. 페이징바 끝 번호 계산
		pageBarEnd = pageBarStart + pageBarSize -1;
		if(pageBarEnd > totalPage) pageBarEnd = totalPage;
		// 3. 이전,이후 버튼이 보이는지 여부 확인
		if(pageBarStart == 1) prev = false;
		if(pageBarEnd >= totalPage) next = false;
	}
}
