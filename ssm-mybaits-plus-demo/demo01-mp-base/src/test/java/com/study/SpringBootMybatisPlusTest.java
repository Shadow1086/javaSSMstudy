package com.study;

import com.study.mapper.ScheduleMapper;
import com.study.pojo.Schedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 16:49
 */
@SpringBootTest
public class SpringBootMybatisPlusTest {
	@Autowired
	private ScheduleMapper mapper;

	@Test
	public void test() {
		List<Schedule> list = mapper.selectList(null);
		System.out.println("list = " + list);
	}

	@Test
	public void test01() {
		mapper.insert(new Schedule());
	}

	@Test
	public void test02() {
		// 根据id删除
		mapper.deleteById(12);
		// 根据completed = 1删除
		Map<String,Object> map = new HashMap<>();
		map.put("compeleted", 1);
		int i = mapper.deleteByMap(map);
		System.out.println("i = " + i);
		// wrapper 条件封装对象
	}

	@Test
	public void test03() {
		// TODO : 相同点：update当属性名为null时，就不修改
		//  updatebyId()：实体类id必须有值，而updaet实体类可以没有id值
		Schedule schedule = new Schedule();
		schedule.setId(1);
		schedule.setCompleted(0);
		mapper.updateById(schedule);

		// 将所有schedule的completed设置为0
		Schedule schedule1 = new Schedule();
		schedule1.setCompleted(0);
		// null 代表没有条件，也就是修改所有人
		mapper.update(schedule1,null);
	}

	@Test
	public void test04() {
		System.out.println(mapper.selectById(2));
		List<Long> ids = new ArrayList<>();
		ids.add(4L);
		ids.add(2L);
		System.out.println(mapper.selectByIds(ids));
	}
}
