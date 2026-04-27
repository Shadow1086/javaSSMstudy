package com.study.controller;

import com.study.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package: com.study.controller
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 21:02
 */
@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private User user;

	@GetMapping("show")
	public User show() {
		return user;
	}
}
