package com.study.ioc_02;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON) //单例，默认值
//@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 多例
public class JavaBean {
	@PostConstruct
	public void init(){
		System.out.println("JavaBean.init");
	}

	@PreDestroy
	public void destroy(){
		System.out.println("JavaBean.destroy");
	}
}
