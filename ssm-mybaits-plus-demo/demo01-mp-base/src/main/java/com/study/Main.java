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
 * {@code @Create} 2026-2026/4/27 16:46
 */
@SpringBootApplication
@MapperScan("com.study.mapper")
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class,args);
	}
}
