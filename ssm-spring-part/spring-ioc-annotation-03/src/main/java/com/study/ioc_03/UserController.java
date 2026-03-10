package com.study.ioc_03;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.Scanner;
import java.util.Arrays;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Controller
public class UserController {
//	<property userService ->对应类型的bean装配
	// 自动装配注解：1.ioc容器中查找符合类型的组件对戏那个，2.设置给当前属性
	@Autowired
	@Qualifier(value="userServiceImpl")  //使用UserServiceImpl,而不是NewUserServiceImpl
	private UserServiceImpl userService;

	@Resource(name = "userServiceImpl")
	private UserService userService01;

	public void setUserService(UserServiceImpl userService) {
		this.userService = userService;
	}
	public void show(){
		String show = userService.show();
		System.out.println(show);
	}
}
