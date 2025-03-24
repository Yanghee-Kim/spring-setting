package com.spring.common.user.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.common.user.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 유저 Controller
 * 
 * 
 * @author YHKIM
 * @file   UserController.java
 * @date   2025. 3. 12.
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
public class UserController {
	
    private final UserService userService;
	
    /**
     * 회원가입 페이지
     * @return
     */
    @GetMapping("/registerPage")
    public String register() {
    	return "login/register";
    }
	
    /**
     * 회원가입
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String insertUser(@RequestParam Map<String, Object> inParams) {
        userService.insertUser(inParams);
        return "redirect:/loginPage";
    }
    
    /**
     * 트랜잭션 테스트
     * @param inParams
     * @return
     */
    @GetMapping("/testTx")
    public void testTx() {
        userService.insertUserTest();
    }
}
