package com.study.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.controller
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 11:07
 */
@RestController
public class HelloController {
	@GetMapping("hello")
	public String ret(){
		return "hello";
	}
}
