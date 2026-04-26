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
 * {@code @Create} 2026-2026/4/25 20:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	private Integer empId;
	private String empName;
	private double empSalary;
}
