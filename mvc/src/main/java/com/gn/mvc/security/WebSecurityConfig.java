package com.gn.mvc.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration	// 이 어노테이션은 환경설정 파일입니다. 라는 의미
@EnableWebSecurity	//스프링 시큐리티 쓸거라는 의미
public class WebSecurityConfig {
	//환경설정 흐름
	// 1. 요청중에 정적인 리소스(static)가 있는 경우 시큐리티를 비활성화 해주기
	@Bean
	WebSecurityCustomizer configure() {
		return web -> web.ignoring()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	// 2. 특정 요청이 들어왔을때 어떻게 처리할 것인가
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http,UserDetailsService customUserDetailsService) throws Exception {
		http.userDetailsService(customUserDetailsService)
			.authorizeHttpRequests(requests -> requests
					.requestMatchers("/login","/signup","/logout","/").permitAll()
					.anyRequest().authenticated())
			.formLogin(login -> login.loginPage("/login")
									.successHandler(new MyLoginSuccessHandler())
									.failureHandler(new MyLoginFailureHandler()))
			.logout(logout -> logout.clearAuthentication(true)
									.logoutSuccessUrl("/login")
									.invalidateHttpSession(true));
		return http.build();
	}
	// 3. 비밀번호 암호화에 사용될 Bean을 등록해줘야함
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	// 4. AuthenticationManager(인증을 관리하는 아이)
	@Bean
	AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration) throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
	}
}
