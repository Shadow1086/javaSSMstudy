package com.study.service;

import com.study.dao.ScheduleDao;
import com.study.pojo.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Package: com.study.service
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 11:43
 */
@Service
public class ScheduleService {
	@Autowired
	private ScheduleDao dao;


	public List<Schedule> find(){
		return dao.find();
	}
}
