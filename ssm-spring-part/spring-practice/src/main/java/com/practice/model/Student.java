package com.practice.model;

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

public class Student {
	private int id;
	private String name;
	private String gender;
	private int age;
	private String classes;

	public Student() {
	}

	public Student(int id, String name, String gender, int age, String classes) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.classes = classes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getclasses() {
		return classes;
	}

	public void setclasses(String classes) {
		this.classes = classes;
	}

	@Override
	public String toString() {
		return "Student{" +
				"id=" + id +
				", name='" + name + '\'' +
				", gender='" + gender + '\'' +
				", age=" + age +
				", classes='" + classes + '\'' +
				'}';
	}
}
