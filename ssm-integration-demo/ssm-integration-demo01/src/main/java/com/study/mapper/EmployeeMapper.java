package com.study.mapper;

import com.study.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Package: com.study.mapper
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 21:58
 */
@Mapper
public interface EmployeeMapper {

	List<Employee> queryList();
}
