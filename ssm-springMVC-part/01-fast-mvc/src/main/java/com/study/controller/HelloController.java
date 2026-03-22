package com.study.controller;

import org.springframework.stereotype.Controller;
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
public class HelloController {
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
