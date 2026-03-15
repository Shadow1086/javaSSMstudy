package com.study;

import com.study.tx_01.config.JavaConfig;
import com.study.tx_01.service.StudentService;
import com.study.tx_01.service.TopService;
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
@SpringJUnitConfig(JavaConfig.class)
public class Test01 {
	@Autowired
	private TopService topService;
	
	@Autowired
	private StudentService service;

	@Test
	public void test01() {
		service.changeInfo();
	}

	@Test
	public void test02() {
		topService.topService();
	}
}
