package com.study.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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

	/// 当前属性对应的列就是逻辑删除的对应状态字段
	/// 当你删除数据的时候，自动变成修改此列的属性值，默认 0 未删除，1 ： 已删除
	/// 当查询的时候，默认只查询 该属性为0的
	@TableLogic
	private Integer deleted;
}
