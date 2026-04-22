package com.study.path;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.path
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/22 21:19
 */
@Controller
@RequestMapping("/path")
@ResponseBody
public class PathController {
	// path/账号/密码
	// 动态路径的设计  {key}= *   ;  {key}在形参列表中获取传入的参数
	// 接收路径参数       必须使用@PathVariable
	@RequestMapping("{count}/{password}")
	public String login(@PathVariable String count,@PathVariable String password){
		System.out.println("username = "+count+",password = "+password);
		return "ok";
	}
}
