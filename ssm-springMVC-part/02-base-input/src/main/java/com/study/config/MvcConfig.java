package com.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/22 20:39
 */
@Configuration
@ComponentScan("com.study")
public class MvcConfig {
	@Bean
	public RequestMappingHandlerMapping handlerMapping(){
		return new RequestMappingHandlerMapping();
	}
	@Bean
	public RequestMappingHandlerAdapter handlerAdapter(){
		return new RequestMappingHandlerAdapter();
	}
}
