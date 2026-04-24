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
 * {@code @Create} 2026-2026/4/24 19:41
 */

public class SpringMVCInit extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected Class<?> @Nullable [] getRootConfigClasses() {
		return new Class[0];
	}

	@Override
	protected Class<?> @Nullable [] getServletConfigClasses() {
		return new Class[]{MvcConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
}
