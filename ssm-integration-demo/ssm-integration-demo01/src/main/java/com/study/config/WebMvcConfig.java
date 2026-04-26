package com.study.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 20:35
 */
@Configuration
@EnableWebMvc
@ComponentScan({"com.study.controller","com.study.exceptionHandler"})
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views",".jsp");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor();
	}
}
