package com.study.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Scanner;
import java.util.Arrays;

/**
 * Package: com.study.pojo
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 10:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
//	@NotNull
	private Integer id;
	@NotBlank
	private String title;
	@NotNull
	private Integer completed;
}
