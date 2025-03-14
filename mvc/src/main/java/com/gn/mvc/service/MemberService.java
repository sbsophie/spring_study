package com.gn.mvc.service;

import org.springframework.stereotype.Service;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository repository;
	
	public MemberDto createMember(MemberDto dto) {
		Member entity = dto.toEntity();
		Member saved = repository.save(entity);
		return new MemberDto().toDto(saved);
	}
}
