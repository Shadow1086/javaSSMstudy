package com.study.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.mapper
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/27 13:19
 */

public interface ScheduleMapper {

	int delete(@Param("id") int i);
}
