package com.spring.common.user.service;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.common.util.dataaccess.SqlManager;

import lombok.RequiredArgsConstructor;

/**
 * 유저 Service
 * 
 * 
 * @author YHKIM
 * @file   UserService.java
 * @date   2025. 3. 19.
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
@RequiredArgsConstructor
public class UserService {
    
	private final SqlManager sqlSession;
    private final PasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입
     * @param inParams
     */
    public void insertUser(Map<String, Object> inParams) {
    	String rawPassword = (String) inParams.get("password");
    	String encodePassword = bCryptPasswordEncoder.encode(rawPassword);
    	
    	inParams.put("password", encodePassword);
    	
        sqlSession.insert("userMapper.insertUser", inParams);
    }
    
    /**
     * 트랜잭션이 적용될 insert 메서드
     */
    public void insertUserTest() {
    	Map<String, Object> user = new HashMap<String, Object>();
    	
    	user.put("username", "error");
    	user.put("password", bCryptPasswordEncoder.encode("1234"));
    	user.put("email", "txTest@email.com");
    	
        sqlSession.insert("userMapper.insertUser", user);
        sqlSession.insert("userMapper.insertUser", user);

    }
    
    /**
     * 사용자 정보 조회
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> selectUser() {
        return (Map<String, Object>) sqlSession.selectOne("userMapper.selectUser");
    }

}
