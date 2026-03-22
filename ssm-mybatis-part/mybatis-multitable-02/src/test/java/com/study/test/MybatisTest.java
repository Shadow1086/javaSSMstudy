package com.study.test;

import com.study.mapper.CustomerMapper;
import com.study.mapper.OrderMapper;
import com.study.pojo.Customer;
import com.study.pojo.Order;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class MybatisTest {
	private SqlSession session;

	// junit会在每一个@Test方法前执行@BeforeEach方法
	@BeforeEach
	public void init() throws IOException {
		session = new SqlSessionFactoryBuilder().build(
				Resources.getResourceAsStream("mybatis-config.xml")
		).openSession();
	}

	@Test
	public void test01() {
		// 查询订单和对应的客户
		OrderMapper mapper = session.getMapper(OrderMapper.class);
		Order order = mapper.queryOrderById(1);
		System.out.println(order);
		System.out.println(order.getCustomer());
	}

	@Test
	public void test02() {
		CustomerMapper customerMapper = session.getMapper(CustomerMapper.class);
		Customer customer = customerMapper.queryOrderById(1);
		List<Order> orders = customer.getOrderList();
		for(Order order : orders){
			System.out.println(order);
		}
	}
	@AfterEach
	public void clean() {
		session.close();
	}
}
