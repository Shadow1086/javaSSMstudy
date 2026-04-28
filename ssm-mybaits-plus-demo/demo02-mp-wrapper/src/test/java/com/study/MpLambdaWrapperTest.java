package com.study;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.study.mapper.ScheduleMapper;
import com.study.pojo.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/28 15:50
 */
@SpringBootTest
public class MpLambdaWrapperTest {
	@Autowired
	private ScheduleMapper mapper;

	@Test
	public void test01() {
		LambdaQueryWrapper<Schedule> wrapper = new LambdaQueryWrapper<>();
		wrapper.like(Schedule::getTitle, "学习")
				.between(Schedule::getCompleted, 1, 1);
		mapper.selectList(wrapper);
	}

	@Test
	public void test02() {
		LambdaUpdateWrapper<Schedule> wrapper = new LambdaUpdateWrapper<>();
		wrapper.gt(Schedule::getCompleted, 0)
				.like(Schedule::getTitle, "java")
				.or().like(Schedule::getTitle, "vue")
				.set(Schedule::getTitle, "学习lambdaWrapper");

		mapper.update(null,wrapper);
	}
}
