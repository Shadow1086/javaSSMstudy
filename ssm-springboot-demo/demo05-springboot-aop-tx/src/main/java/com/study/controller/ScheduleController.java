package com.study.controller;

import com.study.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package: com.study.controller
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 13:18
 */
@RestController
@RequestMapping("schedule")
public class ScheduleController {
	@Autowired
	private ScheduleService service;
	@GetMapping
	public void delete() {
		service.delete();
	}
}
