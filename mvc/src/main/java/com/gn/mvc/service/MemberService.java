package com.gn.mvc.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository repository;
	private final PasswordEncoder passwordEncoder;	// 암호화로직을 추가
	
	public MemberDto createMember(MemberDto dto) {
//		String oriPw = dto.getMember_pw();
//		String newPw = passwordEncoder.encode(oriPw);
//		dto.setMember_pw(newPw);
		dto.setMember_pw(passwordEncoder.encode(dto.getMember_pw()));
		
		Member entity = dto.toEntity();
		Member saved = repository.save(entity);
		return new MemberDto().toDto(saved);
	}
}
