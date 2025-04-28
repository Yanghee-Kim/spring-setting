package com.spring.common.util.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

//import org.springframework.stereotype.Service;

import com.spring.common.util.dataaccess.SqlManager;

public class MessageResourceService {
	
	public static List<Map<String,Object>> messageList = new ArrayList<Map<String,Object>>();
	public static final Map<String, Map<String, Object>> properties = new HashMap<String, Map<String, Object>>(); // 메모리 캐시 가공
	
	private SqlManager sqlManager = null;
	public void setSqlManager(SqlManager sqlManager) {
		this.sqlManager = sqlManager;
	}
	
	@PostConstruct // 빈이 생성되고 , 빈의 의존관계 주입이 완료된 후 호출
	public void loadAllMessages() {
		Map<String, Object> param = new HashMap<String, Object>();
		MessageResourceService.messageList = sqlManager.selectList("messageMapper.selectI18nMessageList", param);
		
		properties.clear();
		
	    for (Map<String, Object> row : messageList) {
	        String code = String.valueOf(row.get("msg_cd"));
	        String message = String.valueOf(row.get("msg_nm"));
	        String locale = String.valueOf(row.get("locale"));

	        properties.computeIfAbsent(code, k -> new HashMap<>()).put(locale, message);
	    }
	}
	
	public String getMessage(String code, String locale) {
	    Map<String, Object> localeMap = properties.get(code);
	    if (localeMap != null) {
	        Object msg = localeMap.get(locale.toLowerCase());
	        return msg != null ? msg.toString() : null;
	    }
	    return null;
	}
}
