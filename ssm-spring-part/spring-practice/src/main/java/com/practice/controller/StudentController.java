package com.practice.controller;

import com.practice.model.Student;
import com.practice.service.StudentService;

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

public class StudentController {
	private StudentService service;

	public void setService(StudentService service) {
		this.service = service;
	}

	public void findAll(){
		List<Student> stuList = service.findAll();
		for(Student stu : stuList){
			System.out.println(stu);
		}
	}

	public static void main(String[] args) {

	}

}
