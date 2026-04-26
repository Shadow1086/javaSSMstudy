package com.study.controller;

import com.study.pojo.PageInfoVo;
import com.study.pojo.Schedule;
import com.study.service.ScheduleService;
import com.study.util.Result;
import com.study.util.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Package: com.study.controller
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 10:43
 */
@CrossOrigin
@RequestMapping("/schedule")
@RestController
public class ScheduleController {
	@Autowired
	private ScheduleService service;

	/**
	 * 返回前端给json数据
	 * GET /schedule?pageSize=5&currentPage=1
	 *
	 * @param pageSize    一页中展示的数据数量
	 * @param currentPage 当前页码数
	 * @return {@link Result }<{@link PageInfoVo }<{@link Schedule }>>
	 */
	@GetMapping
	public Result<PageInfoVo<Schedule>> findByPage(@RequestParam("pageSize") Integer pageSize, @RequestParam("currentPage") Integer currentPage) {
		PageInfoVo<Schedule> byPage = service.findByPage(pageSize, currentPage);
		if (byPage != null) {
			return Result.build(byPage, ResultCodeEnum.SUCCESS);
		} else {
			return Result.build(null, ResultCodeEnum.MAPPERERROR);
		}
	}

	/**
	 * 新增日程
	 * POST /schedule
	 */
	@PostMapping
	public Result<Integer> addSchedule(@RequestBody Schedule schedule) {
		Integer id = service.addSchedule(schedule);
		if (id >= 0) {
			return Result.ok(id);
		}
		return Result.fail(-1);
	}

	/**
	 * 修改日程
	 * PUT /schedule/1
	 */
	@DeleteMapping("/{id}")
	public Result<Integer> deleteSchedule(@PathVariable("id") Integer id) {
		Integer rows = service.deleteSchedule(id);
		if (rows == 1) {
			return Result.ok(rows);
		} else {
			return Result.build(400,"删除日程失败",-1);
		}
	}

	/**
	 * 删除日程
	 * DELETE /schedule/1
	 */
	@PutMapping("/{id}")
	public Result<Integer> updateSchedule(@PathVariable("id")Integer id, @RequestBody Schedule scheduleNew) {
		scheduleNew.setId(id);
		Integer rows = service.updateSchedule(scheduleNew);
		if (rows == 1) {
			return Result.ok(rows);
		}
		return Result.build(400, "更新日程信息失败", -1);
	}
}
