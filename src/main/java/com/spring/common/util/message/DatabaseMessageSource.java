package com.spring.common.util.message;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractMessageSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class DatabaseMessageSource extends AbstractMessageSource { // AbstractMessageSource : Spring 에서 자동으로 해당 클래스를 통해 메시지 처리
	
	private final MessageResourceService messageResourceService;
	
	// Spring이 내부적으로 <spring:message>를 처리할 때 호출하는 메서드
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		// DB 데이터
	    String message = messageResourceService.getMessage(code, locale.getLanguage());
	    if (message != null) {
	        return new MessageFormat(message, locale);
	    }

	    // DB에 없으면 parentMessageSource 조회
	    MessageSource parent = getParentMessageSource();
	    if (parent != null) {
	        try {
	            message = parent.getMessage(code, null, locale);
	            return new MessageFormat(message, locale);
	        } catch (NoSuchMessageException e) {
	        	log.debug(e.getMessage());
	        }
	    }

	    return new MessageFormat(code, locale);
	}
}
