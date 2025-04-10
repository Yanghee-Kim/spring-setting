package com.spring.common.util.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingController {
	
	// TODO: try-catch 추가할 것
    public Object printMethodName(ProceedingJoinPoint joinPoint) throws Throwable {
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
}
