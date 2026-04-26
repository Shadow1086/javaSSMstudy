package com.study.service;

import com.github.pagehelper.PageInfo;
import com.study.pojo.PageInfoVo;
import com.study.pojo.Schedule;

/**
 * Package: com.study.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 10:44
 */
public interface ScheduleService {

	/**
	 *  查找当前页
	 *
	 * @param pageSize          一页中存放多少内容
	 * @param currentPage       当前页数
	 * @return {@link PageInfoVo }<{@link Schedule }>
	 */
	public PageInfoVo<Schedule> findByPage(Integer pageSize,Integer currentPage);

	/**
	 *  添加日程
	 *
	 * @param schedule          前端传来的数据
	 * @return {@link Integer } 返回的id主键
	 */
	public Integer addSchedule(Schedule schedule);

	/**
	 *  根据日程id删除日程
	 *
	 * @param id        日程的id
	 * @return {@link Integer }
	 */
	public Integer deleteSchedule(Integer id);

	/**
	 * 根据日程id更新日程数据
	 *
	 * @param schedule 新的日程信息
	 * @return {@link Integer }
	 */
	public Integer updateSchedule(Schedule schedule);
}
