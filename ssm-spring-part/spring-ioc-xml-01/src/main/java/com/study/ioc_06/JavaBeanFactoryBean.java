package com.study.ioc_06;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.FactoryBean;

import java.util.Scanner;
import java.util.Arrays;

/**
 * ClassnameValue: Practice01
 * Package: java001.day05
 * Description:
 * 制造JavaBean的工厂bean对象
 *
 * 步骤：
 *  1. 实现FactoryBean接口 <返回值泛型>
 *    2.
 * {@code @Create} 2025/9/27 14:19
 */

public class JavaBeanFactoryBean implements FactoryBean<JavaBean> {
	private String nameValue;

	public void setNameValue(String nameValue) {
		this.nameValue = nameValue;
	}

	@Override
	public @Nullable JavaBean getObject() throws Exception {
		// 使用自己的方式实例化对象就可以了
		JavaBean javaBean = new JavaBean();
		javaBean.setName(nameValue);
		return javaBean;
	}

	@Override
	public @Nullable Class<?> getObjectType() {
		return JavaBean.class;
	}
	
}
