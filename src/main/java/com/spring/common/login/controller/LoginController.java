package com.spring.common.login.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
//        request.getSession().invalidate();
        return "redirect:/login?logout";
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
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, Model model) {
    	return loginService.login(username, password, request, model);
    }
}
