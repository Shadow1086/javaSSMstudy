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
import org.springframework.jdbc.core.JdbcTemplate;

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
	 * <p>
	 * 最重要的一步：添加@Bean，会真正让配置类的方法创建的组件存储到ioc容器中
	 * <p>
	 * 问题一： beanName问题：
	 *          默认：方法名
	 *          指定：name/value属性起名字，覆盖方法名
	 * 问题二： 周期方法如何指定
	 *          原有注解方案：postConstruct/PreDestory 注解指定
	 *          bean属性指定：initMethod / destroyMethod 指定
	 * 问题三： 作用域：
	 *          和之前还是一样@Scope注解，默认是单例
	 * <p>
	 * 问题四：如何引用其他的ioc容器
	 *          直接调用对方的bean 方法即可
	 *          直接形参变量进行引入，要求必须有对应的组件，如果有多个，形参名 = 组件id标识即可
	 */
	@Bean(name = "ergouzi", initMethod = "", destroyMethod = "")
	public DataSource dataSource() {
		//实现具体的实例化过程
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(url);
		dataSource.setDriverClassName(driver);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		// 需要DataSource ,也就是需要ioc容器中的其他组件
		// 方案一：如果其他组件也是@Bean方法，可以直接调用，从ioc容器中获取组件。不推荐
		jdbcTemplate.setDataSource(dataSource());
		return jdbcTemplate;
	}

	@Bean
	public JdbcTemplate jdbcTemplate1(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		// 需要DataSource ,也就是需要ioc容器中的其他组件
		// 方案二：通过形参列表声明想要的组件类型，可以是一个也可以是多个,ioc容器会自动注入
		// 要求：
		// 1. 必须有对应的类型的组件，如果没有就会抛出异常
		// 2. 如果有多个类型可以使用形参名称等于对应的bean id标识即可
		jdbcTemplate.setDataSource(dataSource());
		return jdbcTemplate;
	}

}
