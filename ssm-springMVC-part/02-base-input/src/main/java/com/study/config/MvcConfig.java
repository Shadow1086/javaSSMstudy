package com.study.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/22 20:39
 */
@EnableWebMvc       // 给handlerAdapter配置了json转换器
@Configuration
@ComponentScan("com.study")
public class MvcConfig {
//	@Bean
//	public RequestMappingHandlerMapping handlerMapping(){
//		return new RequestMappingHandlerMapping();
//	}
//	@Bean
//	public RequestMappingHandlerAdapter handlerAdapter(){
//		return new RequestMappingHandlerAdapter();
//	}
}
