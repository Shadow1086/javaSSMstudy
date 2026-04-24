package com.study.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.cookie
 * Description:
 * <p>
 *     接收Cookie
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/23 17:14
 */
@Controller
@RequestMapping("cookie")
@ResponseBody
public class CookieController {
	@RequestMapping("data")
	public String data(@CookieValue(value="cookieName")String value){
		System.out.println("value = "+value);
		return value;
	}

	@GetMapping("save")
	public String save(HttpServletResponse response){
		Cookie cookie = new Cookie("cookieName","root");
		response.addCookie(cookie);
		return "ok";
	}
}
