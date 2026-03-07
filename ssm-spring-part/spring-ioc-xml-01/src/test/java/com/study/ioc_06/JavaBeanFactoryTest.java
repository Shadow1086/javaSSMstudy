package com.study.ioc_06;

import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class JavaBeanFactoryTest {

	/**
	 * 读取使用factoryBean工厂配置的组件对象
	 */
	@Test
	public void test() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-06.xml");
		//2. 读取组件
		JavaBean javaBean = applicationContext.getBean("javaBean", JavaBean.class);

		System.out.println("JavaBean = " + javaBean);

		//TODO : FactoryBean工厂也会加入到ioc容器中，名字叫： &id值
		Object bean = applicationContext.getBean("&javaBean");
		System.out.println("bean = " + bean);
		applicationContext.close();
	}
}
