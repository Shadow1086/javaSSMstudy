package com.study.exception;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Package: com.study.exception
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 14:57
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	/**
	 *  可能情况：
	 *      1. 前端未填写completed参数
	 * @param e 错误
	 * @return  错误信息描述
	 */
	@ExceptionHandler(ServletException.class)
	public Object servletExceptionHandler(Exception e) {
		String message = e.getMessage();
		log.error("传入值错误：", e);
		return message;
	}
}
