package com.study.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.error
 * Description:
 * <p>
 *      全局异常发生，就会走此类中写的handler
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 14:05
 */
//@ControllerAdvice       // 可以返回逻辑视图，比如转发和重定向
@RestControllerAdvice       // @ResponseBody：直接返回json字符串
public class GlobalExceptionHandler {

	@ExceptionHandler(ArithmeticException.class)
	public Object ArithmeticExceptionHandler(ArithmeticException e){
		// 自定义处理异常即可
		return null;
	}
	@ExceptionHandler(NullPointerException.class)
	public Object NullPointerExceptionHandler(NullPointerException e){
		// 自定义处理异常即可
		return null;
	}

	@ExceptionHandler(Exception.class)
	public Object ExceptionHandler(Exception e){
		// 自定义处理异常即可
		String message = e.getMessage();
		System.out.println("message = " + message);
		return message;
	}
}
