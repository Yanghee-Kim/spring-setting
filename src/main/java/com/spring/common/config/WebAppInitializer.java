package com.spring.common.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * spring boot 에서 ~application.java의 @springbootApplication 어노테이션과 같은 역할 (web 시작 지점)
 * 
 * 
 * @author YHKIM
 * @file   WebAppInitializer.java
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
public class WebAppInitializer implements WebApplicationInitializer {
	
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	// 스프링 컨텍스트 설정
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        
        // 설정 클래스 자동 스캔
        ac.scan("com.spring.common.config");

        // DispatcherServlet 등록
        // DispatcherServlet : MVC 컨트롤러 찾는 역할
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(ac));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        
        // DelegatingFilterProxy 등록
        // 이를 통해 login 시 Spring Security 적용됨
        servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy("springSecurityFilterChain"))
                      .addMappingForUrlPatterns(null, false, "/*");
    }
}
