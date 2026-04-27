package com.study.service;

import com.study.mapper.ScheduleMapper;
import com.study.pojo.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 12:20
 */
@Service
public class ScheduleService {
	@Autowired
	private ScheduleMapper mapper;

	public List<Schedule> findAll(){
		return mapper.findAll();
	}
}
