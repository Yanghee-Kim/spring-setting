package com.spring.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * mvc 패턴 설정
 * 
 * 
 * @author YHKIM
 * @file   WebMvcConfig.java
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
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	/**
	 * viewResolver 설정
	 */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(new InternalResourceViewResolver("/WEB-INF/views/", ".jsp"));
    }

    /**
     * 정적 파일 설정
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
    
}