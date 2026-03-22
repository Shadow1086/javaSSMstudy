package com.study.mapper;

import com.study.pojo.Order;

import java.util.Scanner;
import java.util.Arrays;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * 查询订单信息接口方法
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public interface OrderMapper {
	Order queryOrderById(Integer id);
}
