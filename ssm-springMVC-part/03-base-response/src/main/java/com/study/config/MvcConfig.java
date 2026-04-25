package com.study.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/24 19:39
 */
@Configuration
@ComponentScan("com.study")
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
	// 视图解析器

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// registry 可以快速添加前后缀
		registry.jsp("/WEB-INF/views",".jsp");
		//handler->index
	}

	/**
	 * 告诉浏览器可以直接访问静态资源
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
}
