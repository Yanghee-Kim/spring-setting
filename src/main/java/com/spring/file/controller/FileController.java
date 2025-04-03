package com.spring.file.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.spring.file.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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
	 * 파일 삭제 
	 * @param fileList
	 * @return
	 */
	@PostMapping("/fileDelete")
	@ResponseBody
	public String deleteFiles(@RequestBody List<Map<String, Object>> fileList) {
	    return fileService.deleteFiles(fileList);
	}
	
	/**
	 * 파일 업로드 : form 직접 제출 + MultipartFile
	 * @param files
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/upload")
	public String saveFileUpload1(@RequestParam("file") MultipartFile file) throws IOException {
		return fileService.saveFileUpload1(file);
	}
	
	/**
	 * 파일 업로드 : ajax 방식 + MultipartFile[] 방식
	 * @param files
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/fileUpload1")
	@ResponseBody
	public String saveFileUpload2(@RequestParam("files") MultipartFile[] files) throws IOException {
		return fileService.saveFileUpload2(files);
	}
	
	/**
	 * 파일 업로드 : ajax 방식 + MultipartHttpServletRequest 방식
	 * @param files
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/fileUpload2")
	@ResponseBody
	public String saveFileUpload3(MultipartHttpServletRequest request) throws IOException {
		return fileService.saveFileUpload3(request);
	}
	
	/**
	 * 파일 다운로드
	 * @param files
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/fileDownload")
	@ResponseBody
	public void fileDownload(@RequestBody Map<String, Object> params, HttpServletResponse response) throws IOException {
		fileService.fileDownload(params, response);
	}
	
	/**
	 * 파일 다운로드 Zip
	 * @param files
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/fileDownloadZip")
	@ResponseBody
	public void fileDownloadZip(@RequestBody List<Map<String, Object>> params, HttpServletResponse response) throws IOException {
		fileService.fileDownloadZip(params, response);
	}
	
	/**
	 * test
	 * @param uploadFile
	 * @return
	 */
	@PostMapping("/maxInMemorySizeTEST")
	public String maxInMemorySizeTEST(@RequestParam("file") MultipartFile uploadFile) {
	    CommonsMultipartFile commonsMultipartFile = (CommonsMultipartFile) uploadFile;
	    DiskFileItem fileItem = (DiskFileItem) commonsMultipartFile.getFileItem();

	    // 실제로 메모리에 있는지 여부 확인
	    System.out.println("isInMemory: " + fileItem.isInMemory());
	    System.out.println("file size: " + uploadFile.getSize());
	    
	    return "ok";
	}
}
