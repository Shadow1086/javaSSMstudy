package com.study;

import com.study.mapper.ScheduleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/28 15:43
 */
@SpringBootTest
public class MpUpdateWrapperTest {
	@Autowired
	private ScheduleMapper mapper;

	/*
	* QueryWrapper 需要自己准备要修改的实体类数据，并且不能修改为null
	* 而updateWrapper 直接携带修改数据，有set("列名"，"值")方法，并且指定任意修改值，包括null
	* */
	@Test
	public void test02() {

	}
}
