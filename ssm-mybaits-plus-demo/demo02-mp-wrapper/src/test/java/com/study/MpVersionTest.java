package com.study;

import com.study.mapper.ScheduleMapper;
import com.study.pojo.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/28 17:04
 */
@SpringBootTest
public class MpVersionTest {
	@Autowired
	private ScheduleMapper mapper;

	@Test
	public void test01() {
		Schedule schedule = mapper.selectById(4);
		Schedule schedule1 = mapper.selectById(4);

		schedule.setCompleted(0);
		schedule1.setCompleted(1);

		mapper.updateById(schedule);
		mapper.updateById(schedule1);

	}
}
