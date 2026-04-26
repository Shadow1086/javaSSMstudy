package com.study.exceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Package: com.study.exceptionHandler
 * Description:
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2026-2026/4/26 11:29
 */

@Slf4j
public class MyInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
		System.out.println("request = " + request);
		return true;
	}

	@Override
	public void postHandle(@NonNull HttpServletRequest request,
	                       @NonNull HttpServletResponse response,
	                       @NonNull Object handler,
	                       @Nullable ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(@NonNull HttpServletRequest request,
	                            @NonNull HttpServletResponse response,
	                            @NonNull Object handler,
	                            @Nullable Exception ex) throws Exception {
		System.out.println("ex = " + ex);
		log.error("请求处理异常，路径：{}",request.getRequestURI(),ex);
	}
}
