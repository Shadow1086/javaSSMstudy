package com.study.ioc_use_04;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * ioc容器创建和读取组件的测试类
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class HappyComponentTest {
	public static void main(String[] args) {

	}

	/**
	 * 讲解如何创建ioic容器并且读取配置文件
	 */

	public void createIoc() {
		// 创建容器，选择合适的容器实现即可
		/**
		 * 接口
		 * BeanFactory
		 *
		 * ApplicationContext
		 *
		 * 实现类：
		 *  可以直接通过构造函数实例化：
		 *  ClassPathXmlApplicationContext          读取类路径下的的xml配置方式         编译后的class文件
		 *  FileSystemXmlApplicationContext         读取指定文件位置的xml配置方式
		 *  AnnotationConfigApplicationContext      读取配置类方式的ioc容器
		 *  WebApplicationContext                   web项目专属的配置的ioc容器
		 */
		// 方式一：直接创建容器并且制定配置文件
		// 构造函数(String ... 配置文件)可以填写一个或者多个参数
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-04.xml");

		// 方式二：先创建ioc容器对象，在制定配置文件，再刷新
		// 源码的配置过程：创建容器[spring]和配置文件制定分开[自己指定]
		ClassPathXmlApplicationContext applicationContext1 = new ClassPathXmlApplicationContext();

		applicationContext1.setConfigLocations("spring-04.xml");    //外部配置文件的设置

		applicationContext1.refresh();  //提供ioc和di的流程
	}

	/**
	 * 讲解如何在Ioc容器中获取组件
	 */
	@Test
	public void getBeanFromIoc() {
		// 1.创建ioc容器对象
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext();
		applicationContext.setConfigLocations("spring-04.xml");
		applicationContext.refresh();

		//2. 读取ioc容器的组件
		System.out.println("// 方案一： 直接根据beanId获取即可,返回值类型是Object,需要强转[不推荐]");
		HappyComponent happyComponent = (HappyComponent) applicationContext.getBean("happyComponent");
		happyComponent.doWork();

		System.out.println("// 方案二：根据beanId，同时指定bean的类型 class");
		HappyComponent happyComponent1 = applicationContext.getBean("happyComponent",HappyComponent.class);
		happyComponent1.doWork();

		System.out.println("// 方案三：直接根据类型获取");
		// TODO : 根据bean的类型获取，同一个类型在ioc容器中只能有一个bean
		// TODO : 如果ioc容器存在多个同类型的Bean,会出现：NoUniqueBeanDefinitionException
		// TODO : ioc的配置一定是实现类，但是可以根据接口类型获取值
		HappyComponent happyComponent2 = applicationContext.getBean(HappyComponent.class);
		happyComponent2.doWork();

		System.out.println("测试对象是否一致");
		System.out.println(happyComponent1 == happyComponent);
		System.out.println(happyComponent1 == happyComponent2);
	}
}
