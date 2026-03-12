package com.study.ioc01.config;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * java的配置类，用来代替xml配置文件
 * 1. 包扫描注解配置
 * 2. 引用外部的配置文件
 * 3. 声明第三方依赖的bean组件
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * 步骤1 ：添加@Configuration，代表是配置类
 * 步骤二：实现上面的三个功能注解
 */
//@ComponentScan(value = "com.study.ioc01")   //value可以使用
@ComponentScan({"com.study.ioc01", "com.study"})     //多个值使用集合的方式表示

@PropertySource(value = "classpath:jdbc.properties")
@Configuration
public class JavaConfiguration {
	@Value("${practice.url}")
	private String url;
	@Value("${practice.driver}")
	private String driver;
	@Value("${practice.username}")
	private String username;
	@Value("${practice.password}")
	private String password;
	/**
	 * <bean -> 对应着一个方法
	 * <p>
	 * 方法的返回值类型= bean组件的类型或者是他的接口和父类
	 * 方法的名字 = bean 的ID
	 * 方法提可以自定义实现过程即可
	 *
	 * 最重要的一步：添加@Bean，会真正让配置类的方法创建的组件存储到ioc容器中
	 */
	@Bean
	public DataSource dataSource() {


		//实现具体的实例化过程
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}
}
