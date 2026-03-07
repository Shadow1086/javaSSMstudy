package com.study.ioc_05;

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

public class JavaBean {
	/**
	*必须是Public ,必须是void返回值，必须是无参数的
	 * 命名随意
	 * 初始化方法 ->初始化业务逻辑即可
	*/

	public void init(){
		System.out.println("JavaBean.init");
	}


	public void clear(){
		System.out.println("JavaBean.clear");
	}
}
