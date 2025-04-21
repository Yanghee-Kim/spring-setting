package com.spring.common.util.dataaccess;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlManager extends SqlSessionDaoSupport {

    public SqlManager() {
    }
    
	public List<Map<String,Object>> selectList(String sqlId) {
		List<Map<String,Object>> result = getSqlSession().selectList(sqlId);
		return result;
	}
	
	public List<Map<String,Object>> selectList(String sqlId, Map<String,Object> paramMap) {
		List<Map<String,Object>> result = getSqlSession().selectList(sqlId, paramMap);
		return result;
	}
	
	public Object selectOne(String sqlId) {
		Object result = getSqlSession().selectOne(sqlId	);
		return result;
	}
	
	public Object selectOne(String sqlId, Map<String,Object> paramMap) {
		Object result = getSqlSession().selectOne(sqlId, paramMap);
		return result;
	}
	
	public Object selectOne(String sqlId, Object param) {
		Object result = getSqlSession().selectOne(sqlId, param);
		return result;
	}
	
	public int delete(String sqlId) {
		int deleteCount = 0;
		deleteCount = getSqlSession().delete(sqlId);
		return deleteCount;
	}
	
	public int delete(String sqlId, Map<String,Object> paramMap) {
		int deleteCount = 0;
		deleteCount = getSqlSession().delete(sqlId, paramMap);
		return deleteCount;
	}
	
	public int update(String sqlId) {
		int result = getSqlSession().update(sqlId);
		return result;
	}
	
	public int update(String sqlId, Map<String,Object> paramMap) {
		int result = getSqlSession().update(sqlId, paramMap);
		return result;
	}
	
	public int update(String sqlId, Object param) {
		int result = getSqlSession().update(sqlId, param);
		return result;
	}
	
	public int insert(String sqlId) {
		int result = getSqlSession().insert(sqlId);
		return result;
	}
	
	public int insert(String sqlId, Map<String,Object> paramMap) {
		int result = getSqlSession().insert(sqlId, paramMap);
		return result;
	}
}
