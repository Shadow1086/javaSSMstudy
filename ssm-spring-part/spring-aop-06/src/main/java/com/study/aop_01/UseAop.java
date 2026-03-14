package com.study.aop_01;

import com.study.aop_01.dyn.ProxyFactory;
import com.study.aop_01.statics.StaticProxyCalculator;

import java.lang.reflect.Proxy;

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

		//jdk代理
		ProxyFactory factory = new ProxyFactory(target);
		//使用接口接值 = 代理对象
		Calculator proxy1 = (Calculator)factory.getProxy();
		proxy1.add(1,1);
	}

}
