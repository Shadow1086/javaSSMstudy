package com.study.header;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.header
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/23 17:20
 */
@Controller
@RequestMapping("header")
@ResponseBody
public class HeaderController {
	// 这里的参数默认是param形式，所以需要使用param的方式接收参数
	@GetMapping("data")
	public String data(@RequestHeader("Host") String host){
		System.out.println("host = "+host);
		return host;
	}
}
