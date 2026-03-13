package com.study.ioc02;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

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
@Import(value={JavaConfigurationB.class})
@Configuration
public class JavaConfigurationA {

}
