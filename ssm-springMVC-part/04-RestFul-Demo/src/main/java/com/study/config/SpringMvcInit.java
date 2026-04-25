package com.study.config;

import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 13:35
 */
public class SpringMvcInit extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected Class<?> @Nullable [] getRootConfigClasses() {
		return new Class[0];
	}

	@Override
	protected Class<?> @Nullable [] getServletConfigClasses() {
		return new Class[0];
	}

	@Override
	protected String[] getServletMappings() {
		return new String[0];
	}
}
