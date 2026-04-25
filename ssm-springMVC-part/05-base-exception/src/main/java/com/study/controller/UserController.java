package com.study.controller;

import com.study.pojo.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.study.controller
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 14:02
 */
@RestController
@RequestMapping("user")
public class UserController {
	//	@ResponseBody
	@PostMapping("register")
	// 接收用户数据，用户有校验注解
	public Object register(@RequestBody @Validated User user, BindingResult result) {
		if (result.hasErrors()) {
			//有绑定错误，就不直接返回了，由我们处理
			Map<String, Object> data = new HashMap<>();
			data.put("code", 400);
			data.put("msg", "参数校验异常");
			return data;
		}
		System.out.println("user = " + user);
		return user;
	}

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
		int i = 1 / 0;        // 算数异常：除以0ArithmeticException
		return "ok";
	}
}
