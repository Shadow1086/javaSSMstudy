package com.study.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Package: com.study.util
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 09:12
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum {
	SUCCESS(200,"success"),
	MAPPERERROR(400,"mybatis go wrong");

	private final Integer code;
	private final String message;

}
