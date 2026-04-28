package com.study;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 16:46
 */
@SpringBootApplication
@MapperScan("com.study.mapper")
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class,args);
	}

	// mybaits plus 插件加入ioc容器
	@Bean
	public MybatisPlusInterceptor plusInterceptor(){
		// mybatis-plus的插件集合，想用的插件加入到这个集合中，比如分页插件，乐观锁插件
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

		/// 分页插件
		mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return mybatisPlusInterceptor;
	}

}
