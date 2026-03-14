package com.study.aop_01;

import com.study.aop_01.statics.StaticProxyCalculator;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class UseAop {
	public static void main(String[] args) {
		// 目标
		Calculator target = new CalculatorPureImpl();

		// 代理
		Calculator proxy = new StaticProxyCalculator(target);

		//调用
		int add = proxy.add(1, 1);
		System.out.println("add = " + add);
	}

}
