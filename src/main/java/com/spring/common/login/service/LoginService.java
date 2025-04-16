package com.spring.common.login.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.common.util.SqlManager;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
	
	// 비밀번호 틀린 횟수
    @Value("${pw_fail_cnt}")
    private int pwFailCnt;

    private final BCryptPasswordEncoder encoder;
    
    private final SqlManager sqlSession;
    
	/**
	 * 로그인 처리
	 * @param username
	 * @param password
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> login(Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> outParams = new HashMap<String, Object>();

		// 사용자 정보 조회
		Map<String, Object> userInfo = (Map<String, Object>) sqlSession.selectOne("userMapper.selectUser", params);

        // 1. 사용자 없음
        if (userInfo == null) {
        	outParams.put("errorMsg","입력하신 사용자 아이디가 존재하지 않습니다.");
            return outParams;
        }

        // 2. 비밀번호 불일치
        if (!encoder.matches((String) params.get("password"), (String) userInfo.get("password"))) {
        	Long pwCnt = (Long) userInfo.get("pw_fail_cnt");
            int failCnt = pwCnt.intValue() + 1;
            
            userInfo.put("pw_fail_cnt", failCnt);
            
            if (failCnt >= pwFailCnt) {
            	userInfo.put("status", "LOCKED");
        		sqlSession.update("userMapper.updatePwFailCntAndStat", userInfo);
            	outParams.put("errorMsg","비밀번호 "+failCnt+"회 오류로 최대 횟수를 초과하였습니다.\n관리자에게 문의하세요.");
                return outParams;
            } else {
            	sqlSession.update("userMapper.updatePwFailCntAndStat", userInfo);
            	outParams.put("errorMsg","비밀번호가 일치하지 않습니다.");
                return outParams;
            }
        } else {
        	userInfo.put("pw_fail_cnt", 0);
        	userInfo.put("status", "ACTIVE");
        }
        
        // 3. 계정 상태 확인
        String status = (String) userInfo.get("status");
        if(status.equals("LOCKED")) {
        	outParams.put("errorMsg","비밀번호 최대 횟수 초과로 계정잠금 상태입니다.\n관리자에게 문의하세요.");
            return outParams;
        }

        // 4. 로그인 성공 → 실패 카운터 초기화
        sqlSession.update("userMapper.updatePwFailCntAndStat", userInfo);
        
        HttpSession session = request.getSession(false);
        session.setAttribute("LOGIN_USER", userInfo);
        
        return outParams;
	}
    
}
