package com.study.config;

import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Package: com.study.config
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 20:50
 */

public class SsmWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	// Root Ioc容器的配置类
	@Override
	protected Class<?> @Nullable [] getRootConfigClasses() {
		return new Class[]{MyBatisConfig.class, DataSourceConfig.class, ServiceTransactionConfig.class};
	}

	// Web Ioc容器的配置类指定
	@Override
	protected Class<?> @Nullable [] getServletConfigClasses() {
		return new Class[]{WebMvcConfig.class};
	}

	// dispatcherServlet的拦截路径
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
}
