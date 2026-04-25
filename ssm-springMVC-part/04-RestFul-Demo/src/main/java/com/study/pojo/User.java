package com.study.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.pojo
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 13:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private String name;
	private Integer id;
	private Integer age;
}
