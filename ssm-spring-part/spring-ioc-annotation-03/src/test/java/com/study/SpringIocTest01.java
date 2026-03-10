package com.study;

import com.study.ioc_01.CommonComponent;
import com.study.ioc_01.XxxController;
import com.study.ioc_01.XxxDao;
import com.study.ioc_01.XxxService;
import com.study.ioc_02.JavaBean;
import com.study.ioc_03.UserController;
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
	/**
	* 关于生命周期(单例多例模式)
	*/
	@Test
	public void testIoc02(){
		ClassPathXmlApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring-02.xml");
		JavaBean javaBean = applicationContext.getBean("javaBean", JavaBean.class);
		JavaBean javaBean1 = applicationContext.getBean("javaBean", JavaBean.class);
		System.out.println(javaBean1 == javaBean);
		applicationContext.close();
	}

	@Test
	public void testIoc03(){
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-03.xml");
		UserController userController = applicationContext.getBean(UserController.class);
		// 场景1 ： ioc容器中有一个UserService接口对应的实现类对象
		userController.show();
		// 场景2: ioc容器中没有默认的类型应该如何处理
		// @Autowired 使用它进行装配时，默认情况下至少要求有一个bean，否则报错，默认值为true
		// 佛系装配：可以没有，值==null boolean required() default false;就是开启佛系了。不推荐使用佛系装配，装配的数据后期都会有调用，后面会出现空指针
		// @Autowired(required=false)


		// 场景三：同一类型有多个对应组件，@Autowired也会报错，无法选择。比如Service接口有两个实现类
			// 解决一：成员属性名指定 @Autowired默认会根据成员的属性名进行查找
			// 解决二：使用@Qualifier(value="...") ,只能和@Autowired搭配使用

		//优化点：@Autowired(required=true) + @Qualifier(value="") = @Resource(name="")

		applicationContext.close();
	}
}
