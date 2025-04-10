package com.spring.common.util.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlLoggingController {

	public Object logSql(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        log.debug("[AOP SQL] 호출: {} 파라미터: {}", methodName, Arrays.toString(args));

        StopWatch stopWatch = new StopWatch();
		stopWatch.start();

        Object result = joinPoint.proceed(); // 실제 메서드 실행
        
        stopWatch.stop();
        
        log.debug("[AOP SQL] 완료: {} | 실행 시간: {}ms", methodName, stopWatch.getTotalTimeMillis());
        return result;
    }
}
