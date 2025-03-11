package com.spring.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * DB 설정 
 * 
 * 
 * @author YHKIM
 * @file   DatabaseConfig.java
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
public class DatabaseConfig {

	/**
	 * DB 정보 세팅
	 * @return
	 */
    @Bean
    public DataSource dataSource() {
    	// workbench mysql testdb 사용
    	HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/testdb?serverTimezone=Asia/Seoul");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        return dataSource;
    }

    /**
     * 트랜잭션 설정
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
