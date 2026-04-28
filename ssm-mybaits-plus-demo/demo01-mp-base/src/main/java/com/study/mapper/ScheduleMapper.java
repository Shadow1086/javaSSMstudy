package com.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.study.pojo.Schedule;
import org.apache.ibatis.annotations.Param;

import java.net.InterfaceAddress;

/**
 * Package: com.study.mapper
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 16:47
 */

public interface ScheduleMapper extends BaseMapper<Schedule> {


	// 定义一个根据completed参数查询，并且分页的方法
	IPage<Schedule> queryByCompleted(IPage<Schedule> page,@Param("completed") Integer completed);
}
