package com.study.controller;

import com.study.pojo.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Package: com.study.controller
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 13:36
 */
@RestController
@RequestMapping("/user")
public class UserController {
	/**
	 * 使用param形式接收数据
	 */
	@GetMapping
	public List<User> page(@RequestParam(required = false, defaultValue = "1") Integer page,
	                       @RequestParam(required = false, defaultValue = "10") Integer size) {
		List<User> list = new ArrayList<>();
		System.out.println("page = "+page+",size = "+size);
		return list;
	}

	/**
	 * json数据传递对象参数
	 */
	@PostMapping
	public User save(@RequestBody User user){
		System.out.println("save()方法");
		return user;
	}

	/**
	 * 路径传参
	 */
	@GetMapping("{id}")
	public User detail(@PathVariable("id") Integer id){
		System.out.println("detail()方法");
		return null;
	}

	/**
	 * Json数据传递对象
	 */
	@PutMapping
	public User update(@RequestBody User user){
		System.out.println("update()方法");
		return user;
	}
	@DeleteMapping("{id}")
	public User delete(@PathVariable("id") Integer id){
		System.out.println("delete()方法");
		return null;
	}
	@GetMapping("search")
	public List<User> search(
			@RequestParam(required = false,defaultValue = "1") Integer page,
			@RequestParam(required = false,defaultValue = "10") Integer size,
			@RequestParam(required = false) String keyword
	){
		return null;
	}
}
