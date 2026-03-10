package com.study.practice.dao;

import com.study.practice.controller.StudentController;
import com.study.practice.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
@Repository
public class StudentDaoImpl implements StudentDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Student> queryAll() {
		String str = """
				select id,name,gender,age,class as classes from students
				""";
		return jdbcTemplate.query(str,new BeanPropertyRowMapper<>(Student.class));
	}
}
