package com.study.json;

import com.study.pojo.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.json
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/22 21:28
 */
@RequestMapping("/json")
@ResponseBody
@Controller
public class JsonController {
	@PostMapping("/data")
	public String data(@RequestBody Person person){
		System.out.println("person = "+person);
		return "ok";
	}
}
