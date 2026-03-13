package com.study.practice.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Scanner;
import java.util.Arrays;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@ComponentScan("com.study.practice")
@PropertySource(value = "classpath:jdbc.properties")
@Configuration
public class StudentConfiguration {
	@Bean
	public DataSource dataSource (@Value("${practice.url}")String url,
                                  @Value("${practice.password}") String password,
                                  @Value("${practice.username}") String username,
                                  @Value("${practice.driver}") String driver
									){
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(dataSource);
		return jdbcTemplate;
	}
}
