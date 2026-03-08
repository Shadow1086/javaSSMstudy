package com.practice.service;

import com.practice.dao.StudentDao;
import com.practice.dao.StudentDaoImpl;
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

public class StudentServiceImpl implements StudentService{
	private StudentDao dao;

	public void setDao(StudentDao dao) {
		this.dao = dao;
	}

	/**
	 * 插入学生信息
	 *
	 * @param stu 学生对象
	 */
	@Override
	public int insert(Student stu) {
		return dao.insert(stu);
	}

	/**
	 * 删除学生
	 *
	 * @param stu 学生对象
	 */
	@Override
	public int deleteById(Student stu) {
		return dao.deleteById(stu);
	}

	/**
	 * 更新学生信息
	 *
	 * @param stu    旧的学生信息
	 * @param stuNew 新的学生信息
	 */
	@Override
	public int updateById(Student stu, Student stuNew) {
		return dao.updateById(stu,stuNew);
	}

	/**
	 * 根据id查询学生信息
	 *
	 * @param id 学生id
	 */
	@Override
	public Student findById(int id) {
		return dao.findById(id);
	}

	/**
	 * 查询所有学生信息
	 */
	@Override
	public List<Student> findAll() {
		return dao.findAll();
	}
}
