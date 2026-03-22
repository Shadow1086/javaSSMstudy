package com.study.pojo;

import lombok.Data;

import java.util.List;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Data
public class Customer {
	private Integer customerId;
	private String customerName;
	private List<Order> orderList;

}
