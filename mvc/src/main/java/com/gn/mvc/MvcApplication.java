package com.gn.mvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MvcApplication implements WebMvcConfigurer{
	
	// spring이 가지고 있는걸로 import해야함
	@Value("${ffupload.location}")
	private String fileDir;

	public static void main(String[] args) {
		SpringApplication.run(MvcApplication.class, args);
	}
	
	@Override //바깥쪽에 불러온 것을 안에서 뭐라고 부를건지
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**") //뭐라고 부를건지를 적어주기
			.addResourceLocations("file:///"+fileDir);
	}

}
