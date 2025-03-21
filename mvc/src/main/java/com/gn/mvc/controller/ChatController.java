package com.gn.mvc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatRoomService chatRoomService;
	
	// 채팅방의 정보를 조회해와서 화면전환
	@GetMapping("/{id}/detail")
	public String selectChatRoomOne(@PathVariable("id") Long id, Model model) {
		ChatRoom chatRoom = chatRoomService.selectChatRoomOne(id);
		model.addAttribute("chatRoom",chatRoom);
		return "chat/detail";
	}
	
	//내가 소속되어 있는 채팅방 전체 목록 조회 코드
	@GetMapping("/list")
	public String selectChatRoomAll(Model model) {
		List<ChatRoom> resultList = chatRoomService.selectChatRoomAll();
		model.addAttribute("chatRoomList", resultList);
		return "chat/list";
	}
	
}
