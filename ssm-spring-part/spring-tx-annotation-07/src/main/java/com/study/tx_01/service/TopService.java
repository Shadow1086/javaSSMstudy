package com.study.tx_01.service;

import com.study.tx_01.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Scanner;
import java.util.Arrays;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Service
public class TopService {
	@Autowired
	public StudentService service;

	@Transactional
	public void topService(){
		service.changeAge();
		service.changeName();
	}
}
