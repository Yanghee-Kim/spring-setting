package com.spring.file.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.spring.common.util.dataaccess.SqlManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
	
	private final SqlManager sqlSession;
	
	// value : static 사용 불가
	@Value("${file.upload.path}")
    private String uploadPath;
	
	/**
	 * 업로드된 파일 조회
	 * @return
	 */
	public List<Map<String, Object>> selectFileList() {
		return sqlSession.selectList("fileMapper.selectFileList");
	}

	/**
	 * 파일 삭제
	 * @param fileList
	 * @return
	 */
	public String deleteFiles(List<Map<String, Object>> fileList) {
		try {
	        for (Map<String, Object> fileInfo : fileList) {
	            String savedFileName = (String) fileInfo.get("saved_name");

	            File file = new File(uploadPath + File.separator + savedFileName);
	            
	            // 파일 삭제
	            if (file.exists()) file.delete();

	            // db에 저장
	            sqlSession.update("fileMapper.deleteFile", (String) fileInfo.get("id"));
	        }
	        return "success";
	    } catch (Exception e) {
	    	throw new RuntimeException("파일 삭제 중 오류가 발생했습니다." + e.getMessage(), e);
	    }
	}
	
	/**
	 * 파일 업로드 : form 직접 제출 + MultipartFile
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public String saveFileUpload1(MultipartFile file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException("업로드할 파일이 선택되지 않았습니다.");
        }
		
		try {
			String originalName = file.getOriginalFilename();
            String savedName = UUID.randomUUID() + "_" + originalName;
            long fileSize = file.getSize();

            // 폴더 없으면 만들기
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 파일 저장
            File serverFile = new File(directory.getAbsolutePath() + File.separator + savedName);
            file.transferTo(serverFile);

            // db에 저장
            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("original_name", originalName);
            fileInfo.put("saved_name", savedName);
            fileInfo.put("file_path", serverFile.getAbsolutePath());
            fileInfo.put("file_size", fileSize);
            sqlSession.insert("fileMapper.insertFile", fileInfo);
            
			return "main";
		} catch (IOException e) {
			throw new RuntimeException("파일 업로드 중 오류가 발생했습니다." + e.getMessage(), e);
		}
	}
	
	/**
	 * 파일 업로드 : ajax 방식 + MultipartFile[] 방식
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public String saveFileUpload2(MultipartFile[] files) throws IOException {
		try {
			for(MultipartFile file : files) {
				String originalName = file.getOriginalFilename();
				
				// 확장자 제한
//				if (originalName == null || originalName.lastIndexOf('.') == -1) {
//		            return "파일에 확장자가 없습니다.";
//		        }
//				
//				String ext = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
//		        if (!allowedExtensions.contains(ext)) {
//		            return "허용되지 않은 확장자입니다: " + ext;
//		        }
				
	            String savedName = UUID.randomUUID() + "_" + originalName;
	            long fileSize = file.getSize();

	            // 폴더 없으면 만들기
	            File directory = new File(uploadPath);
	            if (!directory.exists()) {
	                directory.mkdirs();
	            }

	            // 파일 저장
	            File serverFile = new File(directory.getAbsolutePath() + File.separator + savedName);
	            file.transferTo(serverFile);

	            // db에 저장
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
	 * 파일 업로드 : ajax 방식 + MultipartHttpServletRequest 방식
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public String saveFileUpload3(MultipartHttpServletRequest request) throws IOException {
		try {
			Iterator<String> fileNames = request.getFileNames();

			// while을 쓰는 이유 : input의 개수나 명이 유연하기 때문
		    while (fileNames.hasNext()) {
		        String inputName = fileNames.next();
		        MultipartFile file = request.getFile(inputName); // multipartfile로 변환

		        if (file != null && !file.isEmpty()) {
		        	String originalName = file.getOriginalFilename();
		            String savedName = UUID.randomUUID() + "_" + originalName;
		            long fileSize = file.getSize();

		            // 폴더 없으면 만들기
		            File directory = new File(uploadPath);
		            if (!directory.exists()) {
		                directory.mkdirs();
		            }

		            // 파일 저장
		            File serverFile = new File(directory.getAbsolutePath() + File.separator + savedName);
		            file.transferTo(serverFile);

		            // db에 저장
		            Map<String, Object> fileInfo = new HashMap<>();
		            fileInfo.put("original_name", originalName);
		            fileInfo.put("saved_name", savedName);
		            fileInfo.put("file_path", serverFile.getAbsolutePath());
		            fileInfo.put("file_size", fileSize);
		            sqlSession.insert("fileMapper.insertFile", fileInfo);
		        }
		    }
		    return "success";
		} catch (IOException e) {
			 throw new RuntimeException("파일 업로드 중 오류가 발생했습니다." + e.getMessage(), e);
		}
	}

	/**
	 * 파일 다운로드
	 * @param fileName
	 * @param response
	 * @throws IOException
	 */
	public void fileDownload(Map<String, Object> params, HttpServletResponse response) throws IOException {
		String savedName = String.valueOf(params.get("saved_name"));       // 서버에 저장된 파일명 (UUID 포함된)
		String originalName = String.valueOf(params.get("original_name")); // 사용자에게 보여줄 이름
		
		String filePath = uploadPath + File.separator + savedName;
	    File file = new File(filePath);

	    if (!file.exists()) {
	        response.setStatus(HttpServletResponse.SC_NOT_FOUND); // file을 찾을 수 없음
	        return;
	    }

	    response.setContentType("application/octet-stream;charset=UTF-8");
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(originalName, "UTF-8") + "\"");

	    // try-with-resources 방식으로 (...) 에서 자동 close 처리함
	    try (
	    	BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
	    	BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
	    ) {
	    	// 수동 복사 방식
//	    	int length = 0;
//    		byte[] buffer = new byte[1024];
//    		
//	        while ((length = inputStream.read(buffer)) > 0) { // 버퍼 읽기
//	        	outStream.write(buffer, 0, length); // 버퍼 쓰기
//	        }
//	        outStream.flush(); // 버퍼 내보내기
	    	
	    	// Apache Commons IO 라이브러리 사용
	    	IOUtils.copy(inputStream, outStream);
	    	outStream.flush();
	    } catch(IOException e) {
	    	throw new RuntimeException("파일 다운로드 중 오류가 발생했습니다." + e.getMessage(), e);
		}
	}

	/**
	 * 파일 다운로드 Zip
	 * @param fileName
	 * @param response
	 * @throws IOException
	 */
	public void fileDownloadZip(List<Map<String, Object>> params, HttpServletResponse response) throws IOException {
		response.setContentType("application/zip");
	    response.setHeader("Content-Disposition", "attachment; filename=\"files.zip\"");
	    
	    try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
	        for (Map<String, Object> param : params) {
	            String savedName = String.valueOf(param.get("saved_name"));
	            String originalName = String.valueOf(param.get("original_name"));
	            
	            File file = new File(uploadPath + File.separator + savedName);

	            if (!file.exists()) continue;

	            zos.putNextEntry(new ZipEntry(originalName));

	            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
	                IOUtils.copy(in, zos);
	            } catch(IOException e) {
	    	    	throw new RuntimeException("Zip 파일 다운로드 중 오류가 발생했습니다." + e.getMessage(), e);
	    		}

	            zos.closeEntry();
	        }

	        zos.finish();
	    }  catch(IOException e) {
	    	throw new RuntimeException("Zip 파일 다운로드 중 오류가 발생했습니다." + e.getMessage(), e);
		}
	}
}
