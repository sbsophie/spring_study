package com.gn.todo.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gn.todo.dto.AttachDto;
import com.gn.todo.entity.Attach;
import com.gn.todo.service.AttachService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class AttachController {
	
	// 1. 요청이 들어오는 url : /attach/create
	// 2. 응답 : JSON(Map<String,String>)
	// 3. 메소드명 : createAttachApi
	// 4. 매개변수 : List<MultipartFile>
	
	private final AttachService attachService;
	
	@PostMapping("/attach/create")
	@ResponseBody
	public Map<String,String> createAttachApi(@RequestParam("files") List<MultipartFile> files){
		System.out.println("ci/cd test1");
		
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "500");
		resultMap.put("res_msg", "파일 업로드중 오류가 발생하였습니다.");
		
		try {
			for( MultipartFile mf : files ) {
				System.out.println(mf.getOriginalFilename());
				// 1. 파일 자체 업로드
				AttachDto dto = attachService.uploadFile(mf);
				// 2. 파일 데이터베이스 저장
				if(dto != null) attachService.createAttach(dto);
			}
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "파일 업로드되었습니다.");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Object> fileDownlod(@PathVariable("id") Long id) {
		try {
			Attach fileData = attachService.selectAttachOne(id);
			
			// 파일이 없으면 notFound 에러 발생시킴
			if(fileData == null) return ResponseEntity.notFound().build();
			
			Path filePath = Paths.get(fileData.getAttachPath());
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			
			String oriFileName = fileData.getOriName();
			String encodedName = URLEncoder.encode(oriFileName, StandardCharsets.UTF_8);
			
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+encodedName);
			
			return new ResponseEntity<Object>(resource,headers,HttpStatus.OK);

		} catch(Exception e) {
 			e.printStackTrace();
 			return ResponseEntity.badRequest().build();
 		}
		
		
	}
	
}
