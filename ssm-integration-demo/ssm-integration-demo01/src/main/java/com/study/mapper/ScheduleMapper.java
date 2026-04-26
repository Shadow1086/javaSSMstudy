package com.study.mapper;

import com.study.pojo.PageInfoVo;
import com.study.pojo.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Package: com.study.mapper
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 10:46
 */
@Mapper
public interface ScheduleMapper {
	/**
	 *  返回分页结果
	 *
	 * @param offset        偏移量
	 * @param pageSize      一页中展示的内容数量
	 * @return {@link PageInfoVo }<{@link Schedule }>
	 */
	List<Schedule> findByPage(@Param("offset") Integer offset,
	                          @Param("pageSize") Integer pageSize);

	/**
	 *  返回总数
	 *
	 * @return {@link Integer }
	 */
	Integer countTotal();

	Integer addSchedule(Schedule schedule);

	Integer deleteSchedule(@Param("id")Integer id);

	Integer updateSchedule(Schedule schedule);
}
