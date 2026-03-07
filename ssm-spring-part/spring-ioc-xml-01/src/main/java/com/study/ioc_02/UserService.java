package com.study.ioc_02;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class UserService {
	private UserDao userDao;

	private int age;
	private String name;
	public UserService(int age,String name,UserDao userDao){
		this.userDao = userDao;
		this.age = age;
		this.name = name;
	}

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}
}
