package com.study.aop_05.pointCut;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * 专门存储切点的类
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Component
public class MyPointCut {
	@Pointcut("execution(@ com.study.aop_03..*.*(..))")
	public void pc(){}

	@Pointcut("execution(* com..*.*(..)")
	public void myPc(){

	}
}
