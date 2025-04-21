package com.spring.common.util.aop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.spring.common.event.service.EventHistService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingAop {
	
	@Autowired
    private EventHistService eventHistoryService;
	
	// TODO: try-catch 추가할 것
    public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 서버주소 추출
        ServletRequestAttributes sr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        String target       = joinPoint.getTarget().toString(); // 객체 클래스 정보 + 해시코드
		String classPath    = target.substring(0, target.lastIndexOf('@')); // 객체 클래스 정보
		Signature signature = joinPoint.getSignature(); // 메서드 정보
		
        // 패키지 + 클래스명 + 메서드명 추출
        String serviceFQN = new StringBuffer(classPath).append(".").append(signature.getName()).toString();
        
		String path       = "";
        if(sr != null) {
			HttpServletRequest curRequest = sr.getRequest();
			path = curRequest.getScheme() + "://" + curRequest.getServerName() + ":"+curRequest.getServerPort();
		}
        
        // 파라미터 추출
        Object[] args = joinPoint.getArgs();
        
        log.info("-------------------------------------------------------------------------");
        log.info("[AOP] ▶ 주소: " + path);
        log.info("[AOP] ▶ " + serviceFQN + " START...");
        log.info("[AOP] ▶ 파라미터: " + Arrays.toString(args));
        
        StopWatch stopWatch = new StopWatch();
		stopWatch.start();

        Object result = joinPoint.proceed(); // 실제 메서드 실행
        
        stopWatch.stop();

        log.info("[AOP] ◀ 리턴값: " + result);
        log.info("[AOP] ▶ [" + serviceFQN + "()] 실행시간(ms) : " + stopWatch.getTotalTimeMillis());
        log.info("-------------------------------------------------------------------------");

        return result;
    }
    
    @SuppressWarnings("unchecked")
	public Object eventHistLog(ProceedingJoinPoint joinPoint) throws Throwable {
    	Object result;

    	ServletRequestAttributes sr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request  = sr.getRequest();
        HttpSession session         = request.getSession(false);
        String username;
        
        if (session == null || session.getAttribute("LOGIN_USER") == null) {
        	log.debug("로그인하지 않은 사용자 요청이므로 이벤트 이력 제외");
        	return joinPoint.proceed(); // 이력 저장 없이 그냥 진행
        } else {
        	Map<String, Object> loginUser = (Map<String, Object>) session.getAttribute("LOGIN_USER");        	
        	username = (String) loginUser.get("username");
        }

        String url      = (request != null) ? request.getRequestURI() : "UNKNOWN";
        boolean success = true;
        String message  = null;

        try {
        	result = joinPoint.proceed();
        } catch (Throwable ex) {
            success = false;
            message = ex.getMessage();
            throw ex; // 예외는 다시 던져야 정상 흐름 유지됨
        } finally {
            Map<String, Object> params = new HashMap<>();
            params.put("EVENT_USER", username);
            params.put("EVENT_URL", url);
            params.put("EVENT_STATUS", success ? "SUCCESS" : "FAIL");
            params.put("EVENT_MESSAGE", message);

            try {
                eventHistoryService.insertEventHist(params);
            } catch (Exception e) {
                System.err.println("이력 저장 실패: " + e.getMessage());
            }
        }

        return result;
    }
}
