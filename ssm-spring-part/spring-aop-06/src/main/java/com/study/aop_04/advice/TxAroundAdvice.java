package com.study.aop_04.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * <p>
 * 环绕通知提交事务
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class TxAroundAdvice {
	/**
	 * 环绕通知：需要你在通知中，定义目标方法的执行
	 *
	 * @param joinPoint 目标方法（获取目标方法信息，多了一个执行方法）
	 * @return 目标方法的返回值
	 */
	@Around("com.study.aop_04.pointCut.MyPointCut.pc()")
	public Object transaction(ProceedingJoinPoint joinPoint) {
		// 保证目标方法被执行
		Object[] args = joinPoint.getArgs();
		Object result;
		try {
			// 增强代码
			System.out.println("开启事务");
			result = joinPoint.proceed(args);
			System.out.println("结束事务");
		} catch (Throwable e) {
			// 必须再抛出异常
			System.out.println("事务回滚");
			throw new RuntimeException(e);
		}
		return result;
	}
}
