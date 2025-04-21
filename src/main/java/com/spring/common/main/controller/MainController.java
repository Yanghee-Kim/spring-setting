package com.spring.common.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 메인
 * 
 * 
 * @author YHKIM
 * @file   MainController.java
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
public class MainController {
	
    /**
     * 메인 페이지
     */
    @GetMapping("/")
    public String index() {
        return "main";
    }

    /**
     * 메인 페이지
     */
    @GetMapping("/main")
    public String main() {
        return "main";
    }
}
