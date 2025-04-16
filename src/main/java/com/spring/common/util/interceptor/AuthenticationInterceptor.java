package com.spring.common.util.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

// spring 5.3 이후 HandlerInterceptor 구현하는 것으로 바뀜
// implements : 클래스에 어떤 역할/기능을 부여하여 구현하는 것 | 다중상속 o , extends : 클래스 그대로 상속받는 것 | 다중상속 x
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
	
	// Interceptor 종류
	// preHandle : 컨트롤러 실행 전
	// postHandle : 컨트롤러 실행 후
	// afterCompletion : View 렌더링 후
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	String requestedWith = request.getHeader("X-Requested-With");
    	boolean isAjax = "XMLHttpRequest".equals(requestedWith);

        // getSession(true) : 세션이 없을 경우 새로 생성 (default)
        // getSession(false) : 세션이 없을 경우 null 반환
    	HttpSession session = request.getSession(false);
    	
    	log.debug("---------------------------------SESSION-------------------------------------");
    	log.debug("session :: "+session);
        
    	if (session == null || session.getAttribute("LOGIN_USER") == null) {
        	if (isAjax) { // ajax일 경우
        		response.setStatus(440);
        		response.getWriter().flush();
        		response.getWriter().close();
        	} else { // 일반요청일 경우
        		response.sendRedirect("/loginPage");
        	}
        	
        	return false;
        }

        return true; // 로그인된 사용자만 통과
    }
}
