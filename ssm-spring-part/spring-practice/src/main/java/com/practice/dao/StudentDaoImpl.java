package com.practice.dao;

import com.practice.model.Student;
import org.jspecify.annotations.NonNull;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class StudentDaoImpl implements StudentDao{
	private static JdbcTemplate jdbcTemplate;

	/**
	* 注入JdbcTemplate对象
	*/
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		StudentDaoImpl.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 插入学生信息
	 *
	 * @param stu 学生对象
	 */
	@Override
	public int insert(Student stu) {
		String sqlInsert = """
				insert into students (name, gender, age, class)
				values (?,?,?,?);
				""";
		return jdbcTemplate.update(sqlInsert,stu.getName(),stu.getGender(),stu.getAge(),stu.getclasses());
	}

	/**
	 * 删除学生
	 *
	 * @param stu 学生对象
	 */
	@Override
	public int deleteById(Student stu) {
		String sqlDelete = """
				delete from students where id = ?
				""";
		return jdbcTemplate.update(sqlDelete,stu.getId());
	}

	/**
	 * 更新学生信息
	 *
	 * @param stu    旧的学生信息
	 * @param stuNew 新的学生信息
	 */
	@Override
	public int updateById(Student stu, Student stuNew) {
		String sqlUpdate = """
				update students 
				set name=?,
					gender =?,
					age = ?,
					class = ?
				where id = ?
				""";
		return jdbcTemplate.update(sqlUpdate,stuNew.getName(),stuNew.getGender(),stuNew.getAge(),stuNew.getclasses(),stu.getId());
	}

	/**
	 * 根据id查询学生信息
	 *
	 * @param id 学生id
	 */
	@Override
	public Student findById(int id) {
		String sqlFind = """
				select * from students
				where id = ?
				""";
		return jdbcTemplate.queryForObject(sqlFind, new RowMapper<Student>(){
			@Override
			public Student mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
				return  new Student(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("gender"),
						rs.getInt("age"),
						rs.getString("class")
				);
			}
		},id);

	}

	/**
	 * 查询所有学生信息
	 */
	@Override
	public List<Student> findAll() {
		String sqlFindAll = """
				select id,name,gender,age,class as classes from students
				""";
		return jdbcTemplate.query(sqlFindAll, new BeanPropertyRowMapper<>(Student.class));
	}
}
