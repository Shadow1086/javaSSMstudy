package com.study.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Package: com.study.pojo
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 21:00
 */
@Data
@Component
@ConfigurationProperties(prefix = "zef.user")
public class User {
//	@Value("${zef.root.username}")
	private String username;
//	@Value("${zef.root.password}")
	private String password;
//	@Value("${zef.gfs}")
	private List<String> gfs;
}
