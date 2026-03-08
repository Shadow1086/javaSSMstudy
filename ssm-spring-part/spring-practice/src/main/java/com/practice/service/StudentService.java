package com.practice.service;

import com.practice.model.Student;
import java.util.List;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
public interface StudentService {
	/**
	 * 插入学生信息
	 *
	 * @param stu 学生对象
	 */
	int insert(Student stu);

	/**
	 * 删除学生
	 *
	 * @param stu 学生对象
	 */

	int deleteById(Student stu);

	/**
	 * 更新学生信息
	 *
	 * @param stu    旧的学生信息
	 * @param stuNew 新的学生信息
	 */

	int updateById(Student stu, Student stuNew);

	/**
	 * 根据id查询学生信息
	 *
	 * @param id 学生id
	 */

	Student findById(int id);

	/**
	 * 查询所有学生信息
	 */
	List<Student> findAll();
}
