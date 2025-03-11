package com.spring.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.spring.common.login.service.CustomUserDetailsService;

/**
 * spring security 설정
 * 
 * 
 * @author YHKIM
 * @file   SecurityConfig.java
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
@EnableWebSecurity
public class SecurityConfig {
	
	private final CustomUserDetailsService customUserDetailsService;
	
	public SecurityConfig(final CustomUserDetailsService customUserDetailsService) {
		this.customUserDetailsService = customUserDetailsService;
	}
	
    /**
     * 비밀번호 암호화 설정
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * AuthenticationProvider 설정 (CustomUserDetailsService를 사용)
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder()); // 비밀번호 검증을 위해 BCrypt 적용
        return provider;
    }

    /**
     * AuthenticationManager 설정 (Spring Security의 인증을 처리)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    /**
     * Security 설정 (로그인, 로그아웃, 접근 권한)
     * @param http
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
        http
            .csrf(csrf -> csrf // CSRF : 신뢰할 수 있는 사용자를 사칭해 웹 사이트에 원하지 않는 명령을 보내는 공격
            	.disable()
            ) // CSRF 비활성화 (테스트용)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/loginPage").permitAll()  // 로그인 페이지 접근 허용
                .anyRequest().authenticated()               // 나머지 요청은 인증 필요
            )
            .formLogin(form -> form
                .loginPage("/loginPage")          // 로그인 페이지 (GET)
                .loginProcessingUrl("/login")     // 로그인 처리 (POST)
                .defaultSuccessUrl("/home", true) // 로그인 성공 시 이동할 URL
                .failureUrl("/loginPage?error=true") // 로그인 실패 시 이동할 URL
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/loginPage?logout=true")
                .permitAll()
            );
        
        return http.build();
    }

}
