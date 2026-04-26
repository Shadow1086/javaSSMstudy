package com.study.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * 不保留配置文件，全部mybatis
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 21:15
 */
@Configuration
@MapperScan("com.study.mapper")
public class MyBatisConfig {
	@Bean
	public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		// 指定配置文件等信息
		// 指定数据库连接池对象
		sqlSessionFactoryBean.setDataSource(dataSource);
		//指定mybatis配置文件的功能
		// Configuration ：存储setting的配置文件
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

		// 开启驼峰映射
		configuration.setMapUnderscoreToCamelCase(true);
		// 日志输出
		configuration.setLogImpl(Slf4jImpl.class);
		// 开启resultMap自动映射
		configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);

		sqlSessionFactoryBean.setConfiguration(configuration);

		// 设置别名
		sqlSessionFactoryBean.setTypeAliasesPackage("com.study.pojo");

		// 插件处理
		PageInterceptor pageInterceptor = new PageInterceptor();
		Properties properties = new Properties();
		properties.setProperty("helperDialect", "mysql");
		pageInterceptor.setProperties(properties);

		sqlSessionFactoryBean.addPlugins(pageInterceptor);
		return sqlSessionFactoryBean;
	}

}
