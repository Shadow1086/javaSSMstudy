package com.study.aop_05.advice;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * 使用普通方式进行事务的添加
 *
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Component
@Aspect
@Order(5)
public class TxAdvice {
	@Before("com.study.aop_05.pointCut.MyPointCut.pc()")
	public void begin(){
		System.out.println("开启事务");
	}
	@AfterReturning("com.study.aop_05.pointCut.MyPointCut.pc()")
	public void commit(){
		System.out.println("事务提交");
	}
	@AfterThrowing("com.study.aop_05.pointCut.MyPointCut.pc()")
	public void rollBack(){
		System.out.println("事务回滚");
	}
}
