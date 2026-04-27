package com.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 19:25
 */
@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class,args);        //自动创建ioc容器，启动tomcat服务器软件
	}
}
