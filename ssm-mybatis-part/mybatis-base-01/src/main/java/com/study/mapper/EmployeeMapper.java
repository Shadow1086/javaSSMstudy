package com.study.mapper;

import com.study.pojo.Employee;

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

public interface EmployeeMapper {
	// 根据id查询员工信息
	Employee queryById(int id);
}
