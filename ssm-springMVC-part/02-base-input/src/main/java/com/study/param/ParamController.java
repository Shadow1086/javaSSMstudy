package com.study.param;

import com.study.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Package: com.study.requestmapping.param
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/22 20:36
 */
@Controller
@RequestMapping("/param")
public class ParamController {
	// 直接接收
	// 形参列表填写对应名称的参数即可，要求：请求参数名= 形参参数名
	// 1. 名称相同  2. 可以不传递，不报错
	@RequestMapping("/data")
	@ResponseBody
	public String data(String name, int age) {
		if (name == null) {
			name = "";
		}
		String result = "name = " + name + ",age = " + age;
		System.out.println(result);
		return result;
	}

	// 2. 注解指定
	// /param/data1?account=root&page=1,其中account必须传递，而page不一定传递，默认为1
	// 指定任意的请求参数名，要求必须传递，要求不必须传递，给一个默认值

	@GetMapping("/data1")
	@ResponseBody
	public String data1(@RequestParam(value = "account") String username,
	                    @RequestParam(required = false, defaultValue = "1") int page) {
		String data1 = "account = " + username + ",page = " + page;
		System.out.println(data1);
		return data1;
	}

	// 3. 特殊情况：直接使用集合接值,必须添加@RequestParam注解，如果不佳的话，将hbs对应的一个字符串直接赋值给集合，类型异常
	// param/data2?hbs=吃&hbs=玩&hbs=学习
	@GetMapping("/data2")
	@ResponseBody
	public String data2(@RequestParam List<String> hb) {
		System.out.println("hbs = " + hb);
		return "ok";
	}

	// 使用实体对象接值
	// param/data3/username=二狗子&age=3
	@GetMapping("data3")
	@ResponseBody
	public String data3(User user){
		System.out.println("User = "+user);
		return user.toString();
	}
}
