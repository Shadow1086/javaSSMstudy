package com.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 12:19
 */
/// mapper接口所在的具体位置
@MapperScan("com.study.mapper")
@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class,args);
	}
}
