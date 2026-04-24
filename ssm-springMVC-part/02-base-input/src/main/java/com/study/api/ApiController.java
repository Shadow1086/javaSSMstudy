package com.study.api;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Package: com.study.api
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/24 15:14
 */
@Controller
public class ApiController {
	@Autowired
	private ServletContext context;

	public void data(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		// 正常使用原生对象就可以了
		// ServletContext   1.最大的配置文件   2. 全局最大的共享域     3. 核心api，getRealPath
		// 方案一：request 获取session获取
		ServletContext context = request.getServletContext();
		ServletContext context1 = session.getServletContext();
		// 方案二：全局声明ServletContext对象，并且使用@Autowired注解即可，会自动装载到ioc容器中
	}
}
