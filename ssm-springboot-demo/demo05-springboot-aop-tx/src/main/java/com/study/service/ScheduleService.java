package com.study.service;

import com.study.mapper.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Package: com.study.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 13:20
 */
@Service
public class ScheduleService {
	@Autowired
	private ScheduleMapper mapper;

	@Transactional
	public void delete() {
		int rows = mapper.delete(1);
		System.out.println("rows = " + rows);
//		int i = 1 / 0;
	}
}
