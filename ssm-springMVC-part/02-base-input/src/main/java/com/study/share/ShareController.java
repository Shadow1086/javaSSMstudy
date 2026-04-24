package com.study.share;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.share
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/24 15:25
 */
@Controller
@EnableWebMvc
@ResponseBody
public class ShareController {
	@Autowired
	private ServletContext context;
	// 原生api
	public void data(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		request.setAttribute("key","value");
	}

	// spring mvc 提供的方法： request提供了几种，了解即可
	// model modelMap map modelAndView
	public void data1(Model model){
		model.addAttribute("key","value");  // 存到request共享域
	}
	public void data2(ModelMap modelMap){
		modelMap.addAttribute("key","value");  // 存到request共享域
	}
	public void data3(Map map){
		map.put("key","value");  // 存到request共享域
	}
	public ModelAndView data4( ){
		ModelAndView mav = new ModelAndView();
		mav.addObject("key","value");  // 存到request共享域
		mav.setViewName("视图名，页面的名称");
		return mav;
	}
}
