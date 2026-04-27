package com.study.controller;

import com.study.pojo.Schedule;
import com.study.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.controller
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 12:19
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
	@Autowired
	private ScheduleService service;

	@GetMapping
	public List<Schedule> findAll(){
		return service.findAll();
	}
}
