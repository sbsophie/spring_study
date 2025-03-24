package com.gn.mvc.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gn.mvc.entity.ChatMsg;
import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.repository.ChatMsgRepository;
import com.gn.mvc.repository.ChatRoomRepository;
import com.gn.mvc.specification.ChatMsgSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMsgService {
	
	public final ChatMsgRepository chatMsgRepository;
	public final ChatRoomRepository repository;
	
	public ChatRoom selectChatRoomOne(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	// 1. 기능 : 채팅메시지 목록 조회하기
	// 2. 조건 : 채팅방 번호
	// 3. 결과 : 채팅 메시지 목록 받기
	public List<ChatMsg> selectChatMsgAll(Long id){
		// 조건 : 채팅방번호
		// (1) 전달받은 id를 기준으로 chatRoom 엔티티를 조회하기
		// (2) chatRoom엔티티를 기준으로 Specification을 생성하기
		// (3) spec을 매개변수로 전달하여 findAll을 반환받아야함
		ChatRoom entity = selectChatRoomOne(id);
		
		Specification<ChatMsg> spec =(root,query,criteriaBuilder) -> null;
		spec = spec.and(ChatMsgSpecification.chatRoomEquals(entity));
		
		List<ChatMsg> list = chatMsgRepository.findAll(spec);
		return list;
	
	}
}
