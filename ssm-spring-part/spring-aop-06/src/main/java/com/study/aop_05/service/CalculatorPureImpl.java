package com.study.aop_05.service;

import com.study.aop_02.Calculator;
import org.springframework.stereotype.Component;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 *
 * aop只针对ioc容器的对象，创建代理对象
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Component
public class CalculatorPureImpl implements Calculator {
	@Override
	public int add(int i, int j) {
		int result = i + j;
		return result;
	}

	@Override
	public int sub(int i, int j) {
		int result = i - j;
		return result;
	}

	@Override
	public int mul(int i, int j) {
		int result = i * j;
		return result;
	}

	@Override
	public int div(int i, int j) {
		int result = i / j;
		return result;
	}
}
