package com.study.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Package: com.study.pojo
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 10:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageInfoVo<T> {
	private Integer pageSize;
	private Integer currentPage;
	private long total;
	private List<T> data;
}
