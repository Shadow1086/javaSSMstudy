package com.study.dao;

import com.study.pojo.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Package: com.study.dao
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 11:43
 */
@Repository
public class ScheduleDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Schedule> find() {
		String sql = """
				select * from schedule;
				""";
		return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Schedule.class));
	}
}
