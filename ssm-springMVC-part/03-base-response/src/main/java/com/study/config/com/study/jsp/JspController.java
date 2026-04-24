package com.study.config.com.study.jsp;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.config.com.study.jsp
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/24 19:43
 */
@Controller
@RequestMapping("/jsp")
public class JspController {
	/**
	 * TODO：快速查找视图：
	 *  1. 方法的返回值是字符串类型
	 *  2. 不能添加@ResponseBody，直接返回给字符串给浏览器，如果加了，就不着视图，不走视图解析器了
	 *  3. 返回值 对应中间的视图名就可以了
	 * @return
	 */
	@GetMapping("index")
	public String index(HttpServletRequest request){
		System.out.println("JspController.index");
		request.setAttribute("data","hello jsp!!");
		return "/index";
	}

	// 转发

	/**
	 * 转发：  只能是项目下的资源
	 *      1. 方法的返回值要写成字符串
	 *      2. 不能添加@ResponseBody注解
	 *      3. 在返回的字符串前添加：forword: /转发地址
	 *      4. 如果tomcat的上下文路径(Context)不是/，而是/springmvc的话，那么转发地址是：/jsp/index，忽略application context
	 */
	@GetMapping("forward")
	public String forword(){
		return "forward:/jsp/index";
	}

	/**
	 * 冲顶先：可以使项目外的地址，重定向术语二次请求，和转发不一样，不能忽略application context
	 * 使用spring mvc 路径语法：
	 *      "redirect:路径"
	 * @return
	 */
	@GetMapping("redirect")
	public String redirect(){
		return "redirect:/jsp/index";
	}
	@GetMapping("redirect/baidu")
	public String redirectBaidu(){
		return "redirect:https://www.baidu.com";
	}
}
