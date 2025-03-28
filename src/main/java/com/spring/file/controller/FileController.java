package com.spring.file.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.spring.file.service.FileService;

import lombok.RequiredArgsConstructor;

/**
 * 파일 컨트롤러
 * 
 * 
 * @author YHKIM
 * @file   FileController.java
 * @date   2025. 3. 28.
 *
 * <pre> 
 * << 개정이력(Modification Information) >> 
 * 
 * 수정일       수정자      수정내용 
 * -------   --------  --------------------------- 
 *  
 * </pre>
 */
@Controller
@RequiredArgsConstructor
public class FileController {

	private final FileService fileService;
	
	/**
	 * 파일 페이지
	 * @return
	 */
	@GetMapping("/filePage")
	public String init() {
		return "file/file";
	}
	
	/**
	 * 업로드된 파일 조회
	 * @return
	 */
	@PostMapping("/fileList")
	@ResponseBody
	public List<Map<String, Object>> fileList() {
	    return fileService.selectFileList();
	}
	
	/**
	 * 파일 업로드
	 * @param files
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/fileUpload")
	@ResponseBody
	public String saveFileUpload(@RequestParam("files") MultipartFile[] files) throws IOException {
		return fileService.saveFileUpload(files);
	}
	
	/**
	 * 파일 삭제 
	 * @param fileList
	 * @return
	 */
	@PostMapping("/fileDelete")
	@ResponseBody
	public String deleteFiles(@RequestBody List<Map<String, Object>> fileList) {
	    return fileService.deleteFiles(fileList);
	}
}
