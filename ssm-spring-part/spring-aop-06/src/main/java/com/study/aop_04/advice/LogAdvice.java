package com.study.aop_04.advice;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Create} 2025/9/27 14:19
 */
@Aspect
@Component
public class LogAdvice {

	@Pointcut("execution(* com.study.aop_03.*.*(..))")
	public void pc() {
	}
	//方法一：
	@Before("pc()")
	public void start() {
		System.out.println("方法开始了");
	}

	//  方法二：
	@After("com.study.aop_03.pointCut.MyPointCut.pc()")
	public void after() {
		System.out.println("方法结束了");
	}

	@AfterThrowing("pc()")
	public void error() {
		System.out.println("方法报错了");
	}
}
