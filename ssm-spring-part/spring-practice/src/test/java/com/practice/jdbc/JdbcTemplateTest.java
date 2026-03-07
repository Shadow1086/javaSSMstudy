package com.practice.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.practice.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * 测试javaTemplate如何使用
 * <p>
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class JdbcTemplateTest {
	/**
	 * JdbcTemplate即爱华数据库的crud操作，但是不提供连接池
	 * <p>
	 * druidDataSource 负责连接的创建和数据库驱动的注册等等
	 */

	@Test
	public void testForjava() {
		// 0.创建一个连接池对象
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl("jdbc:mysql:///studb");
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");  // jdbc驱动
		dataSource.setUsername("root");
		dataSource.setPassword("LHTl527218713");

		// 1. 实例化对象即可
		JdbcTemplate jdbcTemplate = new JdbcTemplate();

		jdbcTemplate.setDataSource(dataSource);

		// 2. 调用方法即可
//		jdbcTemplate.update();
//		jdbcTemplate.queryForObject();      单个查询
//		jdbcTemplate.query();
	}

	/**
	 * 通过ioc容器读取配置的JdbcTemplatJdbcTemplate组件
	 */

	@Test
	public void testForIoC() {
		//1.创建ioc容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("practice.xml");

		//2. 获取jdbcTemplate组件
		JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
		//3. 进行数据库的crud操作

		// 3.1 插入，删除，修改操作----->DML
		String sql = """
				insert into students (name,gender,age,class)
				VALUES
				(?,?,?,?);
				""";
		/**
		 * 参数一：String 类型的sql语句 可以带占位符，？只能替代值，不能替代关键字和容器名
		 * 参数二：Object ... param 传入占位符的值，顺序是从左到右
		 * 返回值：int 影响行数
		 */

		int rows = jdbcTemplate.update(sql, "二狗", "男", 22, "计算机科学2班");
		System.out.println("插入影响行数：" + rows);

		// 3.2 查询单条数据
		// 根据id查询学生数据，返回一个对应的实体对象
		String sqlQuery = """
				select * from students
				where id = ?
				""";
		/*
		  参数一：sql语句
		  参数二：RowMapper 列名和属性名的映射器接口
		  参数三：Object...param 同上
		  返回值：rowMapper指定的对象
		 */
		Student student = jdbcTemplate.queryForObject(sqlQuery, new RowMapper<Student>() {
			@Override
			public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new Student(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("gender"),
						rs.getInt("age"),
						rs.getString("class"));
			}
		}, 5);
		System.out.println("学号为5的学生信息：" + student);
		// 3.3查询所有学生数据
		String sqlAll = """
				SELECT id,name,gender,age,class AS classes FROM students
				""";
		//TODO:BeanPropertyRowMapper帮助我们自动映射列和属性值,要求：列名和属性名一致，如果不一致就起别名
		List<Student> stuList = jdbcTemplate.query(sqlAll,new BeanPropertyRowMapper<>(Student.class));
		System.out.println("studentList = "+stuList);
	}
}
