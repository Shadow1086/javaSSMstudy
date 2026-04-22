package com.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * <p>
 * 1. 将Controller配置到ioc容器
 * 2. handlerMapping handlerAdapter 加入到ioc容器
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Configuration
@ComponentScan("com.study.controller")
public class MvcConfig {
	@Bean
	public RequestMappingHandlerMapping handlerMapping() {
		return new RequestMappingHandlerMapping();
	}

	@Bean
	public RequestMappingHandlerAdapter handlerAdapter() {
		return new RequestMappingHandlerAdapter();
	}

}
