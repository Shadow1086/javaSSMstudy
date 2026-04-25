package com.study.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * Package: com.study.pojo
 * Description:
 * <p>
 * 要求：
 * 1. name 不为空/null
 *      字符串@NotBlank  集合@NotEmpty   包装@NotNull
 * 2. password 长度大于6
 *
 * 3. age 必须>=1
 * 4. email    邮箱格式的字符串
 * 5. birthday     过去时间
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/25 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@NotBlank
	private String name;
	@Length(min = 6)
	private String password;
	@Min(1)
	private int age;
	@Email
	private String email;
	@Past
	private Date birthday;
}
