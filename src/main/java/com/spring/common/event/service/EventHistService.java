package com.spring.common.event.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.spring.common.util.dataaccess.SqlManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventHistService {

	private final SqlManager sqlsession;
	
	public void insertEventHist(Map<String, Object> inParams) {
		sqlsession.insert("eventMapper.insertEventHistory", inParams);
	}
}
