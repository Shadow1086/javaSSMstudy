package com.study.service;

import com.study.pojo.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 18:22
 */
@SpringBootTest
public class ScheduleServiceTest {
	@Autowired
	private  ScheduleService service;

	@Test
	public void test_save() {
		List<Schedule> list = new ArrayList<>();
		Schedule schedule = new Schedule();
		schedule.setCompleted(0);
		schedule.setTitle("学习mp");
		list.add(schedule);

		boolean b = service.saveBatch(list);
	}

	@Test
	public void test_update() {
		// 如果id 有值，不为null,则为修改，如果为null,就插入
		Schedule schedule = new Schedule();
		schedule.setCompleted(0);
		schedule.setTitle("学习mp");
		service.saveOrUpdate(schedule);
	}

	@Test
	public void test_remove() {
		service.removeById(2);
	}
}
