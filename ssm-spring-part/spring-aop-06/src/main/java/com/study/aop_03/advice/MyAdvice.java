package com.study.aop_03.advice;

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
 * <p>
 * 定义四个增强方法，获取目标方法的信息和返回值
 * <p>
 * 步骤一：定义方法：增强代码
 * 步骤二：使用注解指定对应位置
 * 步骤三：配置切点表达式选中方法
 * 步骤四：切面和ioc的配置
 * 步骤五：开启aspectj注解的支持
 * TODO:增强方法中获取目标方法信息
 *      1.全部增强方法中，获取目标方法的信息(方法名，参数，访问修饰符，所属的类的信息....)
 *          (JoinPoint joinPoint) - joinPoint包含目标方法的信息
 *      2.返回的结果 - @AfterReturning
 *          (Object result) result接收返回结果
 *          @AfterReturning(value = "execution()",returning = "形参名即可")
 *      3.获取异常的信息 - @AfterThrowing
 *          (throwable throwable)
 *          @AfterThrowing(value="execution()",throwing = "形参名")
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
