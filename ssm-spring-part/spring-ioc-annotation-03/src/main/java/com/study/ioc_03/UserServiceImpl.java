package com.study.ioc_03;

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
public class UserServiceImpl implements UserService {
	@Override
	public String show() {
		return "UserService.show()";
	}
}
