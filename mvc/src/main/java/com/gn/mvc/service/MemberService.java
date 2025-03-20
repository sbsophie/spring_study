package com.gn.mvc.service;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gn.mvc.dto.MemberDto;
import com.gn.mvc.entity.Board;
import com.gn.mvc.entity.Member;
import com.gn.mvc.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository repository;
	private final PasswordEncoder passwordEncoder;	// 암호화로직을 추가
	private final DataSource dataSource;
	private final UserDetailsService userDetailsService;

	// 회원탈퇴
	public int deleteMember(Long id) {
		int result = 0;
		try {
			Member target = repository.findById(id).orElse(null);
			if (target != null) {
				repository.deleteById(id);
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "DELETE FROM persistent_logins WHERE username= ?";
				jdbcTemplate.update(sql,target.getMemberId());
				SecurityContextHolder.getContext().setAuthentication(null);
				result = 1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
		
	// 회원수정 기능 코드
	public int updateMember(MemberDto param) {
		int result = 0;
		try{
			//역할 
			//(1)데이터베이스 회원정보수정 
			param.setMember_pw(passwordEncoder.encode(param.getMember_pw()));
			Member updated = repository.save(param.toEntity());
			if(updated != null) {
				//(2)remember-me(DB,cookie)가 있다면 무효화 
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "DELETE FROM persistent_logins WHERE username= ?";
				jdbcTemplate.update(sql,param.getMember_id());
				//(3)변경된 회원 정보 Security Context에 즉시반영
				UserDetails updatedUserDetails
					= userDetailsService.loadUserByUsername(param.getMember_id());
				Authentication newAuth = new UsernamePasswordAuthenticationToken(
						updatedUserDetails,
						updatedUserDetails.getPassword(),
						updatedUserDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
				result = 1;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//회원수정 하기위해 데이터를 넘겨주는 코드(PK값 기준 회원 정보 단일 조회 후 반환하는 역할)
	public Member selectMemberOne(Long id) {
		return repository.findById(id).orElse(null);
	}
	
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
