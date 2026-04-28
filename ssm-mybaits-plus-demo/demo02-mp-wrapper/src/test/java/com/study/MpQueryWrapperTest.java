package com.study;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.mapper.ScheduleMapper;
import com.study.pojo.Schedule;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/28 14:56
 */
@SpringBootTest
public class MpQueryWrapperTest {

	@Autowired
	private ScheduleMapper mapper;

	@Test
	public void test01() {
		// 查询title中包含学习的，completed为1的，id不为空的
		QueryWrapper<Schedule> wrapper = new QueryWrapper<>();
		// 条件动态调用Wrapper的方法完成拼接
//		wrapper.like("title","学习");
//		wrapper.between("completed",1,1);
//		wrapper.isNotNull("id");
		/// 链式调用
		wrapper.like("title", "学习").between("completed", 1, 1).isNotNull("id");
		/// SQL ： select * from schedule where title like %学习% and completed = 1 and id is not null
		List<Schedule> schedules = mapper.selectList(wrapper);
	}

	@Test
	public void test02() {
		/// 按照id降序查询用户
		QueryWrapper<Schedule> wrapper = new QueryWrapper<>();
		/// desc是降序，asc是升序
		wrapper.orderByDesc("id").orderByAsc("completed");
		/// SQL : order by age desc,id asc
		List<Schedule> schedules = mapper.selectList(wrapper);
	}

	@Test
	public void test03() {
		/// 删除 completed是0的用户
		QueryWrapper<Schedule> wrapper = new QueryWrapper<>();
		wrapper.isNull("completed");
		mapper.delete(wrapper);
	}

	@Test
	public void test04() {
		/// 将 completed 为 1 并且 title中包含 vue 或者 java 的用户信息修改
		QueryWrapper<Schedule> wrapper = new QueryWrapper<>();
		wrapper.gt("completed", 0)
				.like("title", "vue")
				.or().like("title", "java") /// 第一个条件直接是or,以后的拼接条件还是and
				.isNull("id");

		Schedule schedule = new Schedule();
		schedule.setTitle("学习wrapper");
		schedule.setCompleted(0);

		/// SQL :
		mapper.update(schedule, wrapper);
	}

	@Test
	public void test05() {
		QueryWrapper<Schedule> wrapper = new QueryWrapper<>();

		wrapper.gt("completed",0).select("title");
		mapper.selectList(wrapper);
	}

	@Test
	public void test06() {
		String title = "学习";
		Integer completed = 1;
		QueryWrapper<Schedule> wrapper= new QueryWrapper<>();
		if(StringUtils.isNotBlank(title)){
			wrapper.eq("title",title);
		}
		if(completed != null){
			wrapper.eq("completed",completed);
		}

		mapper.selectList(wrapper);
	}
}
