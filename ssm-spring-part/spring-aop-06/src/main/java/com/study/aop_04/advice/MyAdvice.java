package com.study.aop_04.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Create} 2025/9/27 14:19
 */
@EnableAspectJAutoProxy
@Component
public class MyAdvice {
	@Before("execution(* com..aop_03..*.*(..)")
	public void before(JoinPoint joinPoint) {
		// 1.获取方法所属的累的信息
		String simpleName = joinPoint.getTarget().getClass().getSimpleName();
		// 2.获取方法访问权限符和名称
		int modifiers = joinPoint.getSignature().getModifiers();
		String s = Modifier.toString(modifiers);
		String name = joinPoint.getSignature().getName();
		// 3. 获取参数列表
		Object[] args = joinPoint.getArgs();    // 获取目标方法的参数
	}

	@After("execution(* com..aop_03..*.*(..)")
	public void after() {

	}

	@AfterReturning(value = "execution(* com..aop_03..*.*(..)",returning = "result")
	public void afterReturning(JoinPoint joinPoint,Object result) {

	}

	@AfterThrowing(value = "execution(* com..aop_03..*.*(..)",throwing = "throwable")
	public void afterThrowing(JoinPoint joinPoint,Throwable throwable) {

	}
}
