package com.study.ioc_04;

import org.springframework.beans.factory.annotation.Value;

import java.util.Scanner;
import java.util.Arrays;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * 通过注解引入基本数据类型
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class JavaBean {
	// 方案一：直接赋值，private String name = "二狗子"
	private String name;
	// 方案二：使用注解进行赋值,使用@value,不用来直接赋值，而是读取外部配置
	//      默认值语法：@Value(${key:value})其中value为默认值
	@Value("19")
	private int age;
	// 前提：使用<context:component-scan base-package=""引入包，
	// 再通过<context:property-placeholder location=""引入配置文件
	@Value("${jdbc.userName}")
	private String userName;
	@Value("${jdbc.password:123456}")
	private String password;
}
