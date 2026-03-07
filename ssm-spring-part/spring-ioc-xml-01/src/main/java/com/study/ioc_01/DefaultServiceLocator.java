package com.study.ioc_01;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class DefaultServiceLocator {
	private static ClientServiceImpl clientService = new ClientServiceImpl();
	public ClientServiceImpl createClientServiceInstance(){
		return clientService;
	}
}
