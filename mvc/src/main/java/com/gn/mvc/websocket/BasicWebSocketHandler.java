package com.gn.mvc.websocket;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class BasicWebSocketHandler extends TextWebSocketHandler{

	//연결된 정보들의 데이터를 쌓을 수 있는 코드
	private static final List<WebSocketSession> sessionList
		= new ArrayList<WebSocketSession>();
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// 새로운 WebSocket이 연결된(open) 순간 동작하게하는 코드
		// System.out.println("서버 : 연결");
		sessionList.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 클라이언트가 서버한테 메시지를 send한 순간 동작하는 코드
		String payload = message.getPayload();
		System.out.println("서버 : 메시지 받음 : "+payload);
		
		for(WebSocketSession temp : sessionList) {
			temp.sendMessage(new TextMessage(payload));
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// WebSocket 연결이 끊겼을때 동작하는 코드
		// System.out.println("연결 끊김");
		sessionList.remove(session);
	}
	
}
