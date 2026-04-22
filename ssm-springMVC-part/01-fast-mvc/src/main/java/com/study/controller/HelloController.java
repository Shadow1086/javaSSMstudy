package com.study.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Controller;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Controller
public class HelloController implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// 每当web项目启动，就会自动调用调用的onStartup方法
		System.out.println("Main.onStartup");

		// ioc容器的初始化 以及 创建dispatcherServlet

	}

	// handler -> springmvc/hello return "hello springmvc"
	// @RequestMapping("xxx")其中xxx是对外访问的地址，到handlerMapping注册的注解
	@ResponseBody       //直接返回字符串给前端，不会找视图解析器。
	@RequestMapping("springmvc/hello")
	public String hello() {
		System.out.println("HelloController.hello");

		// 返回给前端
		return "hello springMVC";
	}
}
