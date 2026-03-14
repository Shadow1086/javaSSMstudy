package com.study.aop_03.advice;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * TODO : 切点表达式
 *      固定语法：execution(1 2 3.4.5(6))
 *          1. 访问修饰符：public / private
 *          2. 方法的返回参数类型
 *              String int void
 *              如果不考虑访问修饰符和返回值，这两个整合成一起为：*
 *              如果要是不考虑，必须两个都不考虑，不能出现 * String
 *          3. 包的位置
 *              具体包：
 *              单层模糊：com.study.service.*  * 是单层模糊
 *              多层模糊：com..service.*         ..是任意层的模糊。但是..不能开头
 *          4. 类的名称：
 *              具体：CalculatorPureImpl
 *              模糊： *
 *              不分模糊：*Impl
 *          5. 方法名：语法和雷鸣一致
 *          6. 参数列表：
 *              没有参数：()
 *              有具体参数：(String) (String , int)
 *              模糊参数：(..) 有没有参数都行，有多个也行
 *              不分模糊：(String ..) String后面有没有参数无所谓，第一个是String就行
 *                      (..int)最后一个参数是int
 *                      (String .. int) 第一个是String,最后一个是int类型即可
 *
 *  TODO 实战
 *      1. 查询某包某类下，访问修饰符时公有，返回值是int的全部方法 "execution(public int com.study..*Impl.*(..))"
 *      2. 查询某包某类下，第一个参数是String的方法： "execution(* com.study.aop01.*(String..))"
 *      3. 查询全部包下，无参数的方法：   "execution(* *..*.*())"
 *      4. 查询com包下，以int参数类型结尾的方法：   "execution(* com..*.*(..int))"
 *      5. 查询指定包下，以Service开头类的私有返回值int的无参数方法：   "execution(private int *..Service*.*())"
 * <p>
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
	public void error() {
		System.out.println("方法报错了");
	}
}
