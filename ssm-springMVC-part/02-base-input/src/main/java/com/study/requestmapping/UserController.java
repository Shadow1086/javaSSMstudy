package com.study.requestmapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Package: com.study.requestmapping
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/22 19:38
 */
@Controller
@RequestMapping("/user")
public class UserController {
	/**
	 * @WebServlet("必须使用'/'开头")
	 * @RequestMapping(不是必须使用/开头)
	 *
	 * 地址写法：
	 *
	 */
	// handler -> handlerMapping 指定访问地址
	@RequestMapping(value = "/login",method = RequestMethod.POST)     // 注册地址，将handler注册到handlerMapping
	public String login(){
		return null;
	}

	@RequestMapping(value = "/register",method = {RequestMethod.GET,RequestMethod.POST})
	 public String register(){
		return null;
	 }
}
