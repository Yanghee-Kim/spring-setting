package com.spring.common.login.service;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService (spring security 관련)
 * 
 * 
 * @author YHKIM
 * @file   CustomUserDetailsService.java
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
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {    	
    	Map<String, String> inParams = new HashMap<String, String>();
    	
    	inParams.put("username", username);
        Map<String, Object> user = sqlSessionTemplate.selectOne("userMapper.findByUsername", inParams);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.builder()
                .username((String) user.get("username"))
                .password((String) user.get("password"))
                .roles("USER") // 임시
                .build();
    }
}