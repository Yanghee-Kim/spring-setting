package com.spring.board.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.spring.common.util.dataaccess.SqlManager;

import lombok.RequiredArgsConstructor;

/**
 * 
 * 게시판 서비스
 * 
 * @author YHKIM
 * @file   BoardService.java
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
@Service
@RequiredArgsConstructor
public class BoardService {

	private final SqlManager sqlSession;
	
	/**
	 * 게시판 리스트 조회
	 * @return
	 */
	public List<Map<String, Object>> boardList(Map<String, Object> inParams) {		
		return sqlSession.selectList("boardMapper.boardList", inParams);
	}

	/**
	 * 게시판 저장
	 * @param inParams
	 * @return
	 */
	public void insertBoard(Map<String, Object> inParams) {
		sqlSession.insert("boardMapper.insertBoard", inParams);
	}
	
	/**
	 * 게시판 수정
	 * @param inParams
	 * @return
	 */
	public void updateBoard(Map<String, Object> inParams) {
		sqlSession.update("boardMapper.updateBoard", inParams);
	}
	
	/**
	 * 게시판 삭제
	 * @param inParams
	 * @return
	 */
	public void deleteBoard(Map<String, Object> inParams) {
		sqlSession.update("boardMapper.deleteBoard", inParams);
	}
	
	/**
	 * 조회수 증가
	 * @param inParams
	 * @return
	 */
	public void updateViewCount(Map<String, Object> inParams) {
		sqlSession.update("boardMapper.updateViewCount", inParams);
	}
	
}
