package com.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Package: com.study.controller
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 14:02
 */
@Controller
@RequestMapping("user")
public class UserController {
	@GetMapping("data")
	public String data() {
		// 空指针异常
		String name = null;
		name.toString();// 空指针异常NullPointerException
		return "ok";
	}

	@GetMapping("data1")
	public String data1() {
		// 算数异常
		int i = 1/0;        // 算数异常：除以0ArithmeticException
		return "ok";
	}
}
