package com.study;

import com.study.mapper.ScheduleMapper;
import com.study.pojo.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/28 16:51
 */
@SpringBootTest
public class MpTableLogicTest {
	@Autowired
	private ScheduleMapper mapper;

	@Test
	public void test01() {
		// update schedule set deleted  = 1 and id = #{id}
		mapper.deleteById(2);

		List<Schedule> schedules = mapper.selectList(null);
		System.out.println("schedules = " + schedules);
	}
}
