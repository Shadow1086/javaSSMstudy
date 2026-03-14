package com.study.aop_01.dyn;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class ProxyFactory {
	private Object target;

	public ProxyFactory(Object target) {
		this.target = target;
	}

	/**
	 * newProxyinstance():创建一个代理实例：
	 * 其中有三个参数：
	 * 1. classLoader: 加载动态生成的代理类的类加载器
	 * 2. interfaces： 目标对象实现的所有接口的class对象所组成的数组
	 * 3. invocationHandler: 设置代理对象实现目标对象方法的过程，即代理类中如何重写接口中的抽象方法
	 */
	public Object getProxy() {
		ClassLoader classLoader = target.getClass().getClassLoader();
		Class<?>[] interfaces = target.getClass().getInterfaces();
		InvocationHandler invocationHandler = new InvocationHandler() {
			/**
			 *  invoke:调用代理的方法都会执行此方法，非核心业务+目标调用就行了
			 * 	proxy:代理对象
			 * 	method:道理对象需要实现的方法，即其中需要重写的方法
			 * 	args: method所对应方法的参数
			 */
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Object result = null;
				try {
					System.out.println("[动态代理]日志：" + method.getName() + ",参数：" + Arrays.toString(args));
					// 嗲用目标方法进行核心业务
					result = method.invoke(target, args);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("[动态代理]日志：" + method.getName() + ",异常:" + e.getMessage());
				} finally {
					System.out.println("[动态代理]日志：" + method.getName() + ",方法执行完毕");
				}
				return result;
			}
		};

		/**
		 * jdk生成代理对象：
		 * 参数一：类加载器
		 * 参数二：目标类的接口们
		 * 参数三：具体要进行的代理动作[非核心动作 - 调用目标函数]
		 */

		return Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
	}

}

