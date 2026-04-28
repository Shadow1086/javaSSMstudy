package com.study.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Package: com.study.pojo
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 16:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
	@TableId(value = "id",type = IdType.AUTO)
	private Integer id;
	private String title;
	private Integer completed;
}
