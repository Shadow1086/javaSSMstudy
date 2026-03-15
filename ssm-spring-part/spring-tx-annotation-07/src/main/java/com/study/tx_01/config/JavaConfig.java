package com.study.tx_01.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Configuration
@ComponentScan("com.study.tx_01")
@PropertySource("classpath:jdbc.properties")
//@EnableAspectJAutoProxy
@EnableTransactionManagement    // 开启事务注解的支持
public class JavaConfig {
	@Value("${practice.url}")
	private String url;
	@Value("${practice.driver}")
	private String driver;
	@Value("${practice.username}")
	private String username;
	@Value("${practice.password}")
	private String password;

	//druid连接池
	@Bean
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	//jdbcTemplate
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}

	@Bean
	public TransactionManager transactionManager(DataSource dataSource){
		//内部要进行事务的操作，基于连接池
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		// 需要连接池对象
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}
}
