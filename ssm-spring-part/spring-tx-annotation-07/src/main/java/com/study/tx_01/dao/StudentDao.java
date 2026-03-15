package com.study.tx_01.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

	public void updateNameById(String name, int id) {
		String sql = """
				UPDATE students SET name = ? WHERE id = ?
				""";
		int update = jdbcTemplate.update(sql, name, id);
	}

	public void updateAgeById(int age, int id) {
		String sql = """
				UPDATE students SET age = ? WHERE id = ?
				""";
		int result = jdbcTemplate.update(sql, age, id);
	}
}
