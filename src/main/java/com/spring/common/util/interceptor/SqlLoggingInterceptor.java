package com.spring.common.util.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler; // 쿼리 -> 자바 객체
import org.apache.ibatis.session.RowBounds; // 페이징 처리
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

// Mybatis 의 Executor 클래스의 update(), query() 메서드 인터셉터
// update() : insert, update, delete | query() : select
// args : 파라미터
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
            RowBounds.class, ResultHandler.class})
})
@Slf4j
public class SqlLoggingInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
    	Object result = new Object();

    	MappedStatement ms = (MappedStatement) invocation.getArgs()[0]; // SQL에 대한 정보
        Object parameter   = invocation.getArgs()[1]; // 파라미터
        BoundSql boundSql  = ms.getBoundSql(parameter); // ?로 바인딩된 sql
        
        try {
        	String sql = getBoundSql(boundSql); // sql + 실제 파라미터
        	
        	log.debug("\n[SQL] SQL 실행 전\n"
			+ "[SQL ID] : {}\n"
			+ "[SQL]    : {}\n"
			+ "[PARAM]  : {}",
			ms.getId(), sql, parameter);

        	StopWatch stopWatch = new StopWatch();
        	stopWatch.start();
        	
        	result = invocation.proceed(); // 실제 SQL 실행
        	
        	stopWatch.stop();
        	
        	log.debug("[SQL] SQL 실행 완료 | 소요 시간: {} ms\n", stopWatch.getTotalTimeMillis());
		} catch (Exception e) {
			log.error("SqlLoggingInterceptor.Exception : ", e);
		}

        return result;
    }
    
    @SuppressWarnings("unchecked")
	private String getBoundSql(BoundSql boundSql) {
        try {
            String sql = boundSql.getSql(); // sql 문자열
            List<ParameterMapping> paramMappings = boundSql.getParameterMappings(); // 파라미터 리스트 (파라미터의 컬럼명)
            Object paramObj = boundSql.getParameterObject(); // 파라미터 객체
            
//            log.debug("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ sql : "+sql);
//            log.debug("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ paramMappings : "+paramMappings);
//            log.debug("▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶▶ paramObj : "+paramObj);

            // 파라미터 없으면 그냥 sql 반환
            if (paramMappings == null || paramMappings.isEmpty() || paramObj == null) {
                return sql;
            }

            // 파라미터가 map일 경우와 아닐 경우
            Map<String, Object> paramMap;
            if (paramObj instanceof Map) {
                paramMap = (Map<String, Object>) paramObj;
            } else {
                paramMap = new HashMap<>();
                MetaObject metaObject = SystemMetaObject.forObject(paramObj);
                
                for (ParameterMapping pm : paramMappings) {
                    paramMap.put(pm.getProperty(), metaObject.getValue(pm.getProperty()));
                }
            }

            // ? -> "파라미터" 바인딩
            StringBuilder sb = new StringBuilder();
            String[] sqlParts = sql.split("\\?");
            for (int i = 0; i < paramMappings.size(); i++) {
                String key = paramMappings.get(i).getProperty();
                Object value = boundSql.hasAdditionalParameter(key) ? boundSql.getAdditionalParameter(key) : paramMap.get(key);
                
                sb.append(sqlParts[i]).append(formatValue(value));
            }
            
            if (sqlParts.length > paramMappings.size()) {
                sb.append(sqlParts[sqlParts.length - 1]);
            }

            return sb.toString();
        } catch (Exception e) {
            log.error("getBoundSql() Exception: ", e);
            return null;
        }
    }

    private String formatValue(Object value) {
        if (value == null) return "null";
        if (value instanceof Number) return value.toString();
        if (value instanceof Date) return "'" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value) + "'";
        return "'" + value.toString().replace("'", "''") + "'";
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 커스텀 설정 있으면 여기에 처리 가능
    }
}

