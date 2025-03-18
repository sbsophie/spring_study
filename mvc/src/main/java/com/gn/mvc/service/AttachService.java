package com.gn.mvc.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.AttachRepository;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.AttachSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachService {
	
	@Value("${ffupload.location}")
	private String fileDir;
	
	private final BoardRepository boardRepository;
	private final AttachRepository attachRepository;
	
	public Attach selectAttachOne(Long id) {
		return attachRepository.findById(id).orElse(null);
	}
	
	public List<Attach> selectAttachList(Long boardNo) {
		// 1. boardNo 기준 Board엔티티 조회
		Board board = boardRepository.findById(boardNo).orElse(null);
		// 2. Specification 생성(Attach)
		Specification<Attach> spec = (root,query,criteriaBuilder) -> null;
		spec = spec.and(AttachSpecification.boardEquals(board));
		// 3. findAll 메소드에 전달(spec)
		return attachRepository.findAll(spec);
	}
	
	public AttachDto uploadFile(MultipartFile file) {
		AttachDto dto = new AttachDto();
		try {
			// 1. 정상파일인지의 여부 확인하기(비어있는 파일인지 아닌지)
			if(file == null || file.isEmpty()) {
				throw new Exception("존재하지 않는 파일입니다.");
			}
			// 2. 파일의 최대 용량 체크하기
			// Spring에서 허용하는 최대 용량은 1MB(1048576byte)임
			// byte -> KB -> MB(1024*1024 = 1MB)
			long fileSize = file.getSize();
			if(fileSize >= 1048576) {
				throw new Exception("허용 용량을 초과한 파일입니다.");
			}
			// 3. 파일 최초 이름 읽어오기
			String oriName = file.getOriginalFilename();
			dto.setOri_name(oriName);
			
			// 4. 파일 확장자를 자르기
			String fileExt
				= oriName.substring(oriName.lastIndexOf("."),oriName.length());
			
			// 5. 파일 명칭 바꾸기(UUID)
			UUID uuid = UUID.randomUUID();
			
			// 6. uuid의 8자리마다 반복되는 하이픈 제거하기(replace사용)
			String uniqueName = uuid.toString().replace("-", "");
			
			// 7. 새로운 파일명을 생성해주기 (4,6번을 합치면됨)
			String newName = uniqueName+fileExt;
			dto.setNew_name(newName);
			
			// 8. 파일 저장 경로를 설정해주기
			String downDir = fileDir+"board/"+newName;
			dto.setAttach_path(downDir);
			
			// 9. 파일을 업로드할 수 있는 상태로 만들어주기(파일 껍데기 생성)
			// io로 import해줘야함
			File saveFile = new File(downDir);
			
			// 10. 경로 존재 유무 확인하기
			if(saveFile.exists() == false) {
				saveFile.mkdirs();
			}
			
			// 11. 껍데기에 파일 정보를 복제하기 (알멩이 : file, 껍데기 : saveFile)
			file.transferTo(saveFile);
			
			
		}catch(Exception e) {
			dto = null;
			e.printStackTrace();
		}
		return dto;
	}

}
