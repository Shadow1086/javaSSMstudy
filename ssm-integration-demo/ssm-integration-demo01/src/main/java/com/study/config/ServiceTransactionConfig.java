package com.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * 1. service
 * 2. 开启aop注解的支持aspect : @Before @After @AfterThrowing @Around @Aspect @Order
 * 3. tx声明式事务管理：1. 对应的事务管理器实现[TransactinManager DataSource]   2. 开启事务注解支持 @Transactional
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 20:54
 */
@Configuration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ComponentScan("com.study.service")
public class ServiceTransactionConfig {
	@Bean
	public TransactionManager transactionManager(DataSource dataSource) {
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}
}
