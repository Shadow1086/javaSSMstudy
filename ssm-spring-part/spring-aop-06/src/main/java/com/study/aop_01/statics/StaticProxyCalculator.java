package com.study.aop_01.statics;

import com.study.aop_01.Calculator;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * <p>
 * 代理类
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class StaticProxyCalculator implements Calculator {
	private Calculator calculator;

	//使用构造函数传入目标
	public StaticProxyCalculator(Calculator target) {
		this.calculator = target;
	}

	@Override
	public int add(int i, int j) {
		System.out.println("i = " + i + ",j = "+j);
		int result = calculator.add(i,j);
		System.out.println(result);
		return result;
	}

	@Override
	public int sub(int i, int j) {
		return 0;
	}

	@Override
	public int mul(int i, int j) {
		return 0;
	}

	@Override
	public int div(int i, int j) {
		return 0;
	}
}
