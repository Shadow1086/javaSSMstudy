package com.study;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/28 14:47
 */
@SpringBootApplication
@MapperScan("com.study.mapper")
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public MybatisPlusInterceptor interceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

		// 分页插件
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);

		interceptor.addInnerInterceptor(paginationInnerInterceptor);
		return interceptor;
	}
}
