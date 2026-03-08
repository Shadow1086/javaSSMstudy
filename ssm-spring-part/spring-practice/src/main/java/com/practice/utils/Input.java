package com.practice.utils;

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

public class Input {
	Scanner input = new Scanner(System.in);
	/**
	* 获取int类型整数
	*/
	public int getInt(){
		return input.nextInt();
	}

	/**
	* 获取字符串
	*/
	public String getString(){
		return input.next();
	}
}
