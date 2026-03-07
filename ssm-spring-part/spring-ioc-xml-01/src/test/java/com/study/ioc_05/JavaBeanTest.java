package com.study.ioc_05;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;
import java.util.Arrays;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class JavaBeanTest {
	/**
	* 测试ioc配置的初始化和销毁方法的触发
	*/

	@Test
	public void initTest(){
		//1.创建ioc容器，就会自动进行组件对象的实例化 -> init()方法
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("spring-05.xml");

		//ioc -> 容器去调用destroy
		// ioc 会立即释放，死了

		// 2. 正常结束ioc容器
		classPathXmlApplicationContext.close();

	}

	@Test
	public void javaBean2Test(){
		//1. 创建ioc容器
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("spring-05.xml");
		JavaBean2 bean = (JavaBean2) classPathXmlApplicationContext.getBean("javaBean2", JavaBean2.class);
		JavaBean2 bean1 = (JavaBean2) classPathXmlApplicationContext.getBean(JavaBean2.class);

		System.out.println(bean);
		System.out.println(bean1);
		classPathXmlApplicationContext.close();
	}
}
