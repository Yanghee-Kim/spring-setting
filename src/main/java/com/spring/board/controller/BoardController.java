package com.spring.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.board.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 게시판 컨트롤러
 * 
 * @author YHKIM
 * @file   BoardController.java
 * @date   2025. 3. 27.
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
public class BoardController {
	
	private final BoardService boardService;

	/**
	 * 게시판 페이지 로드
	 * @return
	 */
	@GetMapping("/boardPage")
	public String boardPage() {
		return "board/board";
	}
	
	/**
	 * 게시판 상세 페이지 로드
	 * @return
	 */
	@GetMapping("/boardDetail")
	public String boardDetail() {
		return "board/boardDetail";
	}
	
	/**
	 * 게시판 리스트 조회
	 * @return
	 */
	@PostMapping("/boardList")
	@ResponseBody
	public List<Map<String, Object>> boardList(@RequestBody(required = false) Map<String, Object> inParams) {
		return boardService.boardList(inParams);
	}
	
	/**
	 * 게시판 저장
	 * @return
	 */
	@PostMapping("/insertBoard")
	@ResponseBody
	public void insertBoard(@RequestParam Map<String, Object> inParams) {
		boardService.insertBoard(inParams);
	}
	
	/**
	 * 게시판 수정
	 * @return
	 */
	@PostMapping("/updateBoard")
	@ResponseBody
	public void updateBoard(@RequestParam Map<String, Object> inParams) {
		boardService.updateBoard(inParams);
	}
	
	/**
	 * 게시판 삭제
	 * @return
	 */
	@PostMapping("/deleteBoard")
	@ResponseBody
	public void deleteBoard(@RequestParam Map<String, Object> inParams) {
		boardService.deleteBoard(inParams);
	}
	
	/**
	 * 조회수 증가
	 * @return
	 */
	@PostMapping("/updateViewCount")
	@ResponseBody
	public void updateViewCount(@RequestBody(required = false) Map<String, Object> inParams) {
		boardService.updateViewCount(inParams);
	}
}
