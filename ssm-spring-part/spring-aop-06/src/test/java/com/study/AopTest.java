package com.study;

import com.study.aop_02.Calculator;
import com.study.aop_02.config.JavaConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@SpringJUnitConfig(value = JavaConfiguration.class)
public class AopTest {
	// aop 代理 - jdk -接口 - 代理类 - 代理对象和目标对象是兄弟关系。只能使用接口接值
	// 注意：使用aop 时，ioc获取的都是代理对象，而不是真实对象
	@Autowired
	private Calculator calculatorPure;

	@Test
	public void test() {
		int result = calculatorPure.add(1, 1);
		System.out.println("result = " + result);
	}
}
