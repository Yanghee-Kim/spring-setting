package com.spring.common.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LogTestController {
    
	@GetMapping("/log-test")
    public String testLog() {
		
		System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+log.getClass().getName());
		
        log.debug("🔥 DEBUG 로그입니다");
        log.info("✅ INFO 로그입니다");
        log.warn("⚠️ WARN 로그입니다");
        log.error("❌ ERROR 로그입니다");
        return "로그 테스트 완료";
    }
}
