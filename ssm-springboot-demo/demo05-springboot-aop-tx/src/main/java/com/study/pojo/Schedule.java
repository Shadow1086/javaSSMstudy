package com.study.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Package: com.study.pojo
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 13:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
	private Integer id;
	private String title;
	private Integer completed;
}
