package com.study.pojo;

import lombok.Data;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Data
public class Order {
	private Integer orderId;
	private String orderName;
	private Integer customerId;
	// 一个订单对应着一个客户，一对一的对应关系
	// 使用customer属性装载这个客户信息
	private Customer customer;
}
