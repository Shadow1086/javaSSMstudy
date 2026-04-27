package com.study.advicef;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.advicef
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 13:30
 */
@Component
@Aspect
public class LogAdvice {
	@Before("execution(* com..service.*.*(..))")
	public void before(JoinPoint point){
		String className = point.getTarget().getClass().getSimpleName();
		String methodName = point.getSignature().getName();
		System.out.println("methodName = " + methodName);
	}
}
