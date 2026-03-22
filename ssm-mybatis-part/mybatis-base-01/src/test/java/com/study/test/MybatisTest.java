package com.study.test;

import com.study.mapper.EmployeeMapper;
import com.study.pojo.Employee;
import com.study.pojo.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */

public class MybatisTest {
	/**
	 * mybatis提供的api进行方法的调用
	 */

	@Test
	public void test01() throws IOException {
		//1，读取外部配置文件
		InputStream ips = Resources.getResourceAsStream("mybatis-config.xml");
		//2。创建sqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(ips);
		//3.根据sqlSessionFactory创建sqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();
		//4.获取接口的代理对象，调用代理对象的方法，就会查找mapper接口的方法
		// jdk动态代理技术生成的mapper代理对象
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		// 内部拼接接口的全限定符号.方法名，去查找sql语句标签
		//1. 拼接累的全限定符.方法名 整合参数 ====> ibatis对应的方法传入参数
		//2. mybatis底层依然调用ibatis，只不过有固定的模式
		Employee employee = mapper.queryById(1);
		System.out.println("employee = " + employee);

		//5.提交事务(非DQL)和释放资源
		sqlSession.close();
	}

	/**
	 * 测试ibatis方式进行数据库操作
	 *
	 * @throws IOException
	 */
	@Test
	public void test02() throws IOException {
		//1，读取外部配置文件
		InputStream ips = Resources.getResourceAsStream("mybatis-config.xml");
		//2。创建sqlSessionFactory
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(ips);
		//3.根据sqlSessionFactory创建sqlSession
		SqlSession sqlSession = sqlSessionFactory.openSession();
		//4.获取接口的代理对象，调用代理对象的方法，就会查找mapper接口的方法
//		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
//		Employee employee = mapper.queryById(1);
//		System.out.println("employee = " + employee);


		// 4.sqlSession提供的crud方法进行数据库查询即可
		// selectOne selectList / insert / delete / update ====> 查找对应的sql语句标签，ibatis再执行即可
		// 参数1 ： 字符串 - > sql标签对应的标识id / namespace.id，参数2 ： Object 执行sql语句传入的参数
		Student student = sqlSession.selectOne("xx.kkk", 1);
		// 缺点：1. sql语句标签对应的字符串表示容易出错 2. 参数需要进行整合，只能传递一个参数 3.返回值问题
		System.out.println("Student = " + student);
		//5.提交事务(非DQL)和释放资源
		sqlSession.close();
	}
}
