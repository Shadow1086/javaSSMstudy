package com.study.config;

import com.study.interceptor.MyInterceptor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 14:01
 */
@Configuration
@ComponentScan({"com.study.controller", "com.study.error"})
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 配置方案一：拦截全部请求
		registry.addInterceptor(new MyInterceptor());
		// 配置方案二：指定地址拦截：也可以使用*/**,s
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/data1");
		// 配置方案三：排除拦截
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/**").excludePathPatterns("/user/data");
	}
}
