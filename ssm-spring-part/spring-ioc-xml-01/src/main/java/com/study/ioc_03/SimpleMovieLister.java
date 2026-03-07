package com.study.ioc_03;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class SimpleMovieLister {
	private MovieFinder movieFinder;
	private String movieName;

	public void setMovieFinder(MovieFinder movieFinder) {
		this.movieFinder = movieFinder;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
}
