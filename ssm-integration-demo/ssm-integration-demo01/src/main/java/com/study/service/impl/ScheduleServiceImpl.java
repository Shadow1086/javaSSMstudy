package com.study.service.impl;

import com.study.mapper.ScheduleMapper;
import com.study.pojo.PageInfoVo;
import com.study.pojo.Schedule;
import com.study.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Package: com.study.service.impl
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 10:46
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleMapper mapper;

	/**
	 * 返回分页的内容
	 *
	 * @param pageSize    一页存放多少内容
	 * @param currentPage 当前页码数
	 * @return {@link PageInfoVo }<{@link Schedule }>
	 */
	@Override
	public PageInfoVo<Schedule> findByPage(Integer pageSize, Integer currentPage) {
		Integer offset = (currentPage - 1) * pageSize;
		/// 返回查找的分页结果
		List<Schedule> mapperResult = mapper.findByPage(offset, pageSize);

		PageInfoVo<Schedule> byPage = new PageInfoVo<>();

		byPage.setData(mapperResult);
		byPage.setCurrentPage(currentPage);
		byPage.setPageSize(pageSize);
		/// 设置总共的数据数量
		byPage.setTotal(mapper.countTotal());
		return byPage;
	}

	/**
	 * 添加日程
	 *
	 * @param schedule 前端传来的数据
	 * @return {@link Integer } 返回的id主键
	 */
	@Override
	public Integer addSchedule(Schedule schedule) {
		Integer id = mapper.addSchedule(schedule);
		return id;
	}

	/**
	 * 根据日程id删除日程
	 *
	 * @param id 日程的id
	 * @return {@link Integer }
	 */
	@Override
	public Integer deleteSchedule(Integer id) {
		return mapper.deleteSchedule(id);
	}

	/**
	 * 根据日程id更新日程数据
	 *
	 * @param schedule 新的日程信息
	 * @return {@link Integer }
	 */
	@Override
	public Integer updateSchedule(Schedule schedule) {
		return mapper.updateSchedule(schedule);
	}
}
