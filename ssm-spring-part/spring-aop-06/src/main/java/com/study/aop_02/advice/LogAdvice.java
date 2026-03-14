package com.study.aop_02.advice;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * 增强类的内部：存储增强代码
 * 步骤一：定义方法存储增强代码
 *      具体定义几个方法，根据插入的位置决定
 *  步骤二：使用注解配置：指定插入目标方法的位置
 *      前置  @Before
 *      后置  @AfterReturning
 *      异常  @AfterThrowing
 *      最后  @After
 *      环绕  @Around
 *  步骤三：配置切点表达式
 *  步骤四：补全注解：
 *      加入ioc容器：@Component
 *      配置切面：@Aspect = 切点+增强
 *
 *  spring aop 重点是配置 -> jdk / cglib
 *
 *  步骤五：开启aspect注解的支持
 * {@code @Create} 2025/9/27 14:19
 */
@Aspect
@Component
public class LogAdvice {
	@Before("execution(* com.study.aop_02..*.*(..))")
	public void start() {
		System.out.println("方法开始了");
	}
	@After("execution(* com.study.aop_02..*.*(..))")
	public void after() {
		System.out.println("方法结束了");
	}
	@AfterThrowing("execution(* com.study.aop_02..*.*(..))")
	public void error(){
		System.out.println("方法报错了");
	}
}
