package com.spring.file.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
	
	private final SqlSessionTemplate sqlSession;
	
	/**
	 * 업로드된 파일 조회
	 * @return
	 */
	public List<Map<String, Object>> selectFileList() {
		return sqlSession.selectList("fileMapper.selectFileList");
	}

	/**
	 * 파일 업로드
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public String saveFileUpload(MultipartFile[] files) throws IOException {
		String uploadPath = "C:/spring-setting/upload"; // 업로드될 저장소

		if (files == null || files.length == 0) {
			throw new IllegalArgumentException("업로드할 파일이 선택되지 않았습니다.");
        }
		
		try {
			for(MultipartFile file : files) {
				String originalName = file.getOriginalFilename();
	            String savedName = UUID.randomUUID() + "_" + originalName;
	            long fileSize = file.getSize();

	            File directory = new File(uploadPath);
	            if (!directory.exists()) {
	                directory.mkdirs();
	            }

	            File serverFile = new File(directory.getAbsolutePath() + File.separator + savedName);
	            file.transferTo(serverFile);

	            Map<String, Object> fileInfo = new HashMap<>();
	            fileInfo.put("original_name", originalName);
	            fileInfo.put("saved_name", savedName);
	            fileInfo.put("file_path", serverFile.getAbsolutePath());
	            fileInfo.put("file_size", fileSize);

	            sqlSession.insert("fileMapper.insertFile", fileInfo);
			}
			return "success";
		} catch (IOException e) {
			throw new RuntimeException("파일 업로드 중 오류가 발생했습니다." + e.getMessage(), e);
		}
	}

	/**
	 * 파일 삭제
	 * @param fileList
	 * @return
	 */
	public String deleteFiles(List<Map<String, Object>> fileList) {
		try {
			String uploadPath = "C:/spring-setting/upload";
			
	        for (Map<String, Object> fileInfo : fileList) {
	            String savedFileName = (String) fileInfo.get("saved_name");

	            File file = new File(uploadPath + File.separator + savedFileName);
	            if (file.exists()) file.delete();

	            sqlSession.insert("fileMapper.deleteFile", savedFileName);
	        }
	        return "success";
	    } catch (Exception e) {
	    	throw new RuntimeException("파일 삭제 중 오류가 발생했습니다." + e.getMessage(), e);
	    }
	}
}
