package com.study.config;

import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/22 20:41
 */

public class SpringMVCInit extends AbstractAnnotationConfigDispatcherServletInitializer {
	/**
	 */
	@Override
	protected Class<?> @Nullable [] getRootConfigClasses() {
		return new Class[0];
	}

	/**
	 * spring mvc 需要组件的配置类
	 */
	@Override
	protected Class<?> @Nullable [] getServletConfigClasses() {
		return new Class[]{MvcConfig.class};
	}

	/**
	 * 指定servlet的地址
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
}
