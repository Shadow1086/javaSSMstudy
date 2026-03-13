package com.study.ioc01;

import com.study.ioc01.config.JavaConfiguration;
import com.study.practice.configuration.StudentConfiguration;
import com.study.practice.controller.StudentController;
import com.study.practice.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class SpringIocTest {
	@Test
	public void test() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(JavaConfiguration.class);

		/*
		 * 也可以写为：
		 * AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		 * applicationContext.register(JavaConfiguration.class);
		 * applicationContext.refresh();    //进行ioc,di的操作
		 */

		applicationContext.close();
	}


	@Test
	public void test01(){
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("JavaConfigurationA.class");
		applicationContext.close();
	}

	@Test
	public void testForPractice(){
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(StudentConfiguration.class);
		StudentController controller = context.getBean( StudentController.class);
		controller.queryAll();
		context.close();
	}
}
