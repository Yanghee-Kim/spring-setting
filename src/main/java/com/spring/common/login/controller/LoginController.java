package com.spring.common.login.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.common.login.service.LoginService;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;

/**
 * 로그인
 * 
 * 
 * @author YHKIM
 * @file   LoginController.java
 * @date   2025. 3. 11.
 *
 * <pre> 
 * << 개정이력(Modification Information) >> 
 * 
 * 수정일       수정자      수정내용 
 * -------   --------  --------------------------- 
 *  
 * </pre>
 */
@Controller
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;
    /**
     * 로그인 페이지
     * @return
     */
    @GetMapping("/loginPage")
    public String loginPage() {
        return "login/loginPage";
    }

    /**
     * 로그아웃 후 로그인 페이지로 리다이렉트
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/loginPage";
    }
    
    /**
     * 로그인 처리
     * @param username
     * @param password
     * @param request
     * @param model
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> login(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
    	return loginService.login(params, request, response);
    }
    
    /**
     * 세션 확인 테스트
     * @param request
     * @return
     */
    @GetMapping("/checkSession")
    @ResponseBody
    public String checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            return "세션 없음 (만료됨)";
        }

        long now = System.currentTimeMillis();
        long lastAccessed = session.getLastAccessedTime();
        int maxInactiveInterval = session.getMaxInactiveInterval(); // 초 단위

        return "세션 유지 중<br>" +
               "세션 ID: " + session.getId() + "<br>" +
               "세션 만료까지: " + (maxInactiveInterval - (now - lastAccessed) / 1000) + "초";
    }
}
