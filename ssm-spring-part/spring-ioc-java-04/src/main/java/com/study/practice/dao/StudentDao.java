package com.study.practice.dao;

import com.study.practice.models.Student;
import jakarta.annotation.Priority;
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
public class StudentDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Student> queryAll(){
		String sql = """
				select id,name,gender,age,class as classes from students
				""";
		return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Student.class));
	}
}
