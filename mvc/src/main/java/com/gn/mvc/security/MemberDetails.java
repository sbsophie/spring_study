package com.gn.mvc.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gn.mvc.entity.Member;

import lombok.RequiredArgsConstructor;

// UserDetails(스프링이 사용하는 사용자 정보 객체)를 구현한 구현체
@RequiredArgsConstructor
public class MemberDetails implements UserDetails{
	private static final long serialVersionUID = 1L;
	
	private final Member member;
	
	public Member getMember() {
		return member;
	}
	
	// 사용자의 권한을 설정해주기(여러개의 권한을 가질수있어서 리스트 형식으로)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return List.of(new SimpleGrantedAuthority("user"));
		return List.of(new SimpleGrantedAuthority(member.getMemberRole()));		
	}

	// 사용자의 비밀번호를 반환하는 코드
	@Override
	public String getPassword() {
		return member.getMemberPw();
	}

	// 사용자의 이름을 반환하는 코드
	@Override
	public String getUsername() {
		return member.getMemberId();
	}

	// 계정 상태를 관리하는
	// is~로 시작하는 메소드를 가지고 있고 boolean타입으로 반환을 함
	// 계정 만료 여부 반환 메소드
	// 임시계정, 비활성화된 계정
	@Override
	public boolean isAccountNonExpired() {
//		if(member.getExpired().equals("Y")) {
//			return false;
//		}else {
//			return true;
//		}
		return true;
	}
	
	// 계정 잠금 여부를 설정할 수 있음
	// 예)비밀번호가 5번 틀리면 10분간 로그인 금지 같은거
	@Override
	public boolean isAccountNonLocked() {
//		if(member.getLockedDate() + 10 > 현재시간) {
//			return false;
//		}else {
//			return true;
//		}
		return true;
	}
	
	// 패스워드 만료 여부
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	// 계정 사용 가능 여부
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
	
	
	
}
