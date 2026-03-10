package com.study.practice.controller;

import com.study.practice.models.Student;
import com.study.practice.service.StudentService;
import com.study.practice.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Component
public class StudentController{
	@Autowired
	private StudentServiceImpl service;

	public void printinfo() {
		List<Student> stuList = service.queryAll();
		for(Student stu:stuList){
			System.out.println(stu);
		}
	}
}
