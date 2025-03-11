package com.spring.common.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * Mybatis 설정
 * 
 * 
 * @author YHKIM
 * @file   MyBatisConfig.java
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
//@MapperScan(basePackages = "com.spring.mapper") // Mapper 인터페이스 자동 스캔 -> mapper 인터페이스 사용 없이 sqlSessionTemplate과 xml로만 db 접근하는 방식으로 사용하기로
public class MyBatisConfig {
	
	/**
	 * xml 파일 위치 설정
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // XML Mapper 위치 설정
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));

        return sessionFactory.getObject();
    }
    
    /**
     * SqlSessionTemplate 설정
     * @param sqlSessionFactory
     * @return
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    	return new SqlSessionTemplate(sqlSessionFactory);
    }
}
