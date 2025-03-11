package com.spring.common.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
public class LoginController {

    /**
     * 로그인 페이지
     * @return
     */
    @GetMapping("/loginPage")
    public String login() {
        return "login/login";
    }

    /**
     * 홈 페이지
     * @return
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * 로그아웃 후 로그인 페이지로 리다이렉트
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login?logout";
    }
}
