package com.study.practice.controller;

import com.study.practice.models.Student;
import com.study.practice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
@Controller
public class StudentController {

	@Autowired
	private StudentService service;

	public void queryAll(){
		List<Student> list = service.queryAll();
		for(Student stu:list){
			System.out.println(stu);
		}
	}
}
