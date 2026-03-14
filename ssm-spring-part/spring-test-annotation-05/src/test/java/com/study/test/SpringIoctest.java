package com.study.test;

import com.study.ioc01.A;
import com.study.ioc01.B;
import com.study.ioc01.config.JavaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * <p>
 * 整合测试类注解
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

//@SpringJUnitConfig(locations = 指定配置文件)
@SpringJUnitConfig(value = JavaConfig.class)
public class SpringIoctest {
	@Autowired
	private A a;
	@Autowired
	private B b;
	@Test
	public void test() {

	}
}
