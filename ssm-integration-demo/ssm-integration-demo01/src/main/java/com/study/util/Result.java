package com.study.util;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Package: com.study
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 09:02
 */
@Data
public class Result<T> {
	private Integer code;
	private String message;
	private T data;

	public Result() {

	}

	public static <T> Result<T> build(T data) {
		Result<T> result = new Result<T>();
		result.data = data;
		return result;
	}

	public static <T> Result<T> build(Integer code, String message, T data) {
		Result<T> result = build(data);
		result.setCode(code);
		result.setMessage(message);
		return result;
	}

	public static <T> Result<T> build(T data, ResultCodeEnum codeEnum) {
		Result<T> result = build(data);
		result.setMessage(codeEnum.getMessage());
		result.setCode(codeEnum.getCode());
		return result;
	}

	public static <T> Result<T> ok(T data){
		Result<T> result = build(data);
		return build(data,ResultCodeEnum.SUCCESS);
	}
	public static <T> Result<T> fail(T data){
		Result<T> result = new Result<>();
		return build(data,ResultCodeEnum.MAPPERERROR);
	}
	public Result<T> message(String msg){
		this.setMessage(msg);
		return this;
	}
	public Result<T> code(Integer code){
		this.setCode(code);
		return this;
	}

}
