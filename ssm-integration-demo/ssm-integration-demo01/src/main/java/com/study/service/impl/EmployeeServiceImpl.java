package com.study.service.impl;

import com.study.mapper.EmployeeMapper;
import com.study.pojo.Employee;
import com.study.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.service.impl
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 08:41
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	private EmployeeMapper employeeMapper;
	@Override
	public List<Employee> queryList() {
		List<Employee> employees = employeeMapper.queryList();
		return employees;
	}
}
