package com.gn.mvc.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gn.mvc.dto.ChatMsgDto;
import com.gn.mvc.entity.ChatMsg;
import com.gn.mvc.entity.ChatRoom;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.ChatMsgRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// 여긴 서버 본부
public class ChatWebSocketHandler extends TextWebSocketHandler{

	private final ChatMsgRepository chatMsgRepository;
	
	private static final Map<Long,WebSocketSession> userSessions
		= new HashMap<Long,WebSocketSession>();
	private static final Map<Long,Long> userRooms
		= new HashMap<Long,Long>();
	
	// 채팅방 들어가는거
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 1. ws://localhost:8080/ws/chat?senderNo=3
		// 2. senderNo=3
		// 3. 0번 인덱스 -> senderNo , 1번 인덱스 -> 3
		
		//String userNo = session.getUri().getQuery().split("=")[1];
		
		String userNo = getQueryParam(session,"senderNo");
		String roomNo = getQueryParam(session,"roomNo");
		
		userSessions.put(Long.parseLong(userNo), session);
		userRooms.put(Long.parseLong(userNo), Long.parseLong(roomNo));
	}

	// 채팅방
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		ChatMsgDto dto = objectMapper.readValue(message.getPayload(), ChatMsgDto.class);
		
		// 채팅 메시지를 데이터베이스에 저장
		// 1. dto 받아온걸 entity로 바꿔줘야함
		if(userSessions.containsKey(dto.getSender_no())) {
			Member member = Member.builder()
					.memberNo(dto.getSender_no())
					.build();
			
			ChatRoom chatRoom = ChatRoom.builder()
					.roomNo(dto.getRoom_no())
					.build();
			
			ChatMsg entity = ChatMsg.builder()
					.sendMember(member)
					.chatRoom(chatRoom)
					.msgContent(dto.getMsg_content())
					.build();
			// 2. entity를 save 해줘야함  -> 이후 하이디에 들어가서 트리거 만들어줘서 마지막 메시지 보여주기
			chatMsgRepository.save(entity);	
		}
		
		
		// 받는 사람의 정보
		WebSocketSession receiverSession
			= userSessions.get(dto.getReceiver_no());
		Long receiverRoom = userRooms.get(dto.getReceiver_no());
		if(receiverSession != null && receiverSession.isOpen()
				&& receiverRoom == dto.getRoom_no()) {
			// receiverSession.sendMessage(new TextMessage(dto.getMsg_content()));
			// 위 코드를 메시지 JSON데이버 전달하는 방식으로 코드 고침 (누가 메세지를 보낸건지 )
			receiverSession.sendMessage(new TextMessage(message.getPayload()));
		}
		// 보내는 사람의 정보
		WebSocketSession senderSession
			= userSessions.get(dto.getSender_no());
		Long senderRoom = userRooms.get(dto.getSender_no());
		if(senderSession != null && senderSession.isOpen()
				&& senderRoom == dto.getRoom_no()) {
			senderSession.sendMessage(new TextMessage(message.getPayload()));
		}
		
	}

	// 채팅방 나가는거
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		//String userNo = session.getUri().getQuery().split("=")[1];
		String userNo = getQueryParam(session,"senderNo");
		userSessions.remove(Long.parseLong(userNo));
		userRooms.remove(Long.parseLong(userNo));
	}

	// 기능 : WebsocketSession의 url을 파싱하는 걸 만들거임
	// 파라미터 : url,key값
	// 반환값 : value값
	
	/*
	 * author : 김가남
	 * history : 2025-03-24
	 * param : url, key data
	 * return : value data
	 * role : WebSocketSesseion url parsing
	 */
	// getQueryParam(session,"senderNo"); ->3
	// getQueryParam(session,"roomNo"); ->1
	private String getQueryParam(WebSocketSession session, String key) {
		// senderNo=3&roomNo=1
		String query = session.getUri().getQuery();
		if(query != null) {
			String[] arr = query.split("&");
			// 0번 인덱스 :senderNo=3
			// 1번 인덱스 :roomNo=1
			for(String target : arr) {
				String[] keyArr = target.split("=");
				if(keyArr.length == 2 && keyArr[0].equals(key)) {
					return keyArr[1];
				}
			}
		}
		return null;
	}
	
}
