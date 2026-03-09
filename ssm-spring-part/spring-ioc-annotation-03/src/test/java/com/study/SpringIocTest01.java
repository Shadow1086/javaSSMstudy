package com.study;

import com.study.ioc_01.CommonComponent;
import com.study.ioc_01.XxxController;
import com.study.ioc_01.XxxDao;
import com.study.ioc_01.XxxService;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description: spring 的ioc注解测试
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class SpringIocTest01 {
	@Test
	public void testIoc01(){
		//1.创建ioc容器
		ClassPathXmlApplicationContext  applicationContext = new ClassPathXmlApplicationContext("spring-01.xml");
		//2.获取组件
		CommonComponent commonComponent = applicationContext.getBean("commonComponent", CommonComponent.class);
		XxxDao dao = applicationContext.getBean(XxxDao.class);
		XxxService service = applicationContext.getBean(XxxService.class);
		XxxController controller = applicationContext.getBean(XxxController.class);
		//3.调用方法
		commonComponent.printInfo();
		dao.printInfo();
		service.printInfo();
		controller.printInfo();
		//4.关闭容器
		applicationContext.close();
	}

}
