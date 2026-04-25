package com.study.json;

import com.study.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.json
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/24 20:33
 */
@RequestMapping("json")
//@ResponseBody       // 返回json的注解
@RestController     // 等同于 @Controller + @ResponseBody
public class JsonController {
	@GetMapping("data")
	public User data(){
		User user = new User();
		user.setAge(19);
		user.setName("二狗子");
		return user;        //user 会在handlerAdapter中转换成json字符串,使用@ResponseBody，那么json直接就会返回，也就是前后端分离模式
	}

	@GetMapping("data1")
	public List<User> data1(){
		List<User> list  = new ArrayList<>();
		User user = new User();
		user.setAge(19);
		user.setName("二狗子");
		list.add(user);
		return list;
	}
}
