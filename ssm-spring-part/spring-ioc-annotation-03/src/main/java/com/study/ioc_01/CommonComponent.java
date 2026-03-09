package com.study.ioc_01;

import org.springframework.stereotype.Component;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * <p>
 * 1. 标记注解：@Component,添加ioc注解，默认的组件名为：类的首字母小写
 * 2. 配置指定包
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

/*
*   @Component = <bean id="commonComponent" class="..../ComminComponent"/>
*   注意：默认ID值为首字母小写，如果想指定id值的话就使用：
*       @Component(value="xxx") / @Component("xxx")
*/

@Component
public class CommonComponent {
	public void printInfo() {
		System.out.println("CommonComponent.class running...");
	}
}
