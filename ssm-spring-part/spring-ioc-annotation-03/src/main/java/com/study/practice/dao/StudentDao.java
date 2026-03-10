package com.study.practice.dao;

import com.study.practice.models.Student;

import java.util.List;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
public interface StudentDao {
	List<Student> queryAll();
}
