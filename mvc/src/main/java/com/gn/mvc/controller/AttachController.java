package com.gn.mvc.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gn.mvc.entity.Attach;
import com.gn.mvc.service.AttachService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AttachController {
	
	private final AttachService service;
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Object> fileDownload(@PathVariable("id") Long id) {
		try {
			// 1. id를 기준으로 파일 메타 데이터를 조회
			Attach fileData = service.selectAttachOne(id);
			// 2. 해당하는 파일이 없다면 NotfundException발생시킨다는 404에러 발생시키기
			if(fileData == null) {
				return ResponseEntity.notFound().build();
			}
			// 3. 파일 경로로 가서 바깥쪽에 있는파일을 자바 안으로 inputstream해서 읽어들이기
			// nio걸로 import하기, spring거로 import
			Path filePath = Paths.get(fileData.getAttachPath());
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			// 4. 한글 파일명으로 인코딩해주기
			String oriFileName = fileData.getOriName();
			String encodedName = URLEncoder.encode(oriFileName,StandardCharsets.UTF_8);
			// 5. 브라우저에게 파일정보를 전달해주기
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+encodedName);
			
			return new ResponseEntity<Object>(resource,headers,HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			// 아래 return 코드 => 400에러가 발생했다는 의미
			return ResponseEntity.badRequest().build();
		}
	}
	
}
