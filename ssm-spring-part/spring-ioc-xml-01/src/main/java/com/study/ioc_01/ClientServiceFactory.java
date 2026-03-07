package com.study.ioc_01;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class ClientServiceFactory {
	private static ClientServiceFactory clientServiceFactory = new ClientServiceFactory();

	private ClientServiceFactory() {
	}

	public static ClientServiceFactory createInstance() {
		return clientServiceFactory;
	}
}
