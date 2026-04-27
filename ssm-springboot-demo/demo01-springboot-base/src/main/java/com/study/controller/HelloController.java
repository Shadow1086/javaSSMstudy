package com.study.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.controller
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 19:26
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
	@GetMapping("/boot")
	public String hello(){
		return "hello springboot3!!";
	}
}
