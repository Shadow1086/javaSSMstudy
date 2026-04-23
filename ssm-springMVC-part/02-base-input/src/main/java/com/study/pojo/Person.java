package com.study.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.pojo
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/22 21:28
 */
@Data
@AllArgsConstructor
public class Person {
	private String name;
	private Integer age;
	private String gender;
}
