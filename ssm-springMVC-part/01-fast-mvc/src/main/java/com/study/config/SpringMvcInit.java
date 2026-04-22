package com.study.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * <p>
 * 可以被web项目加载，会初始化ioc容器，会设置dispatcherServlet的地址
 *  用来替代web.xml配置文件
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class SpringMvcInit extends AbstractAnnotationConfigDispatcherServletInitializer {
	/**
	 * 后期创建service mapper层的ioc容器
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[0];
	}

	/**
	 * 设置我们项目对应的配置spring mvc controller
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{MvcConfig.class};
	}

	// 配置Spring mvc 内部自带的servlet的访问地址
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
}
