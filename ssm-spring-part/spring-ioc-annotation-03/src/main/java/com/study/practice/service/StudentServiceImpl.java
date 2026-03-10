package com.study.practice.service;

import com.study.practice.dao.StudentDao;
import com.study.practice.dao.StudentDaoImpl;
import com.study.practice.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
@Service
public class StudentServiceImpl implements StudentService{
	@Autowired
	private StudentDaoImpl dao;

	@Override
	public List<Student> queryAll() {
		return dao.queryAll();
	}
}
