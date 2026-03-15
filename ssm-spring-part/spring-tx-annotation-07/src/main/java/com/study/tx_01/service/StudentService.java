package com.study.tx_01.service;

import com.study.tx_01.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;

/**
 * ClassName: Practice01
 * Package: java001.day05
 * Description:
 * {@code @Author} Liang-ht
 * {@code @Create} 2025/9/27 14:19
 * {@code @Version} 1.0
 */
@Service
public class StudentService {
	@Autowired
	private StudentDao dao;

	/**
	 * 添加事务：
	 *
	 * @Transactional注解 位置：方法 / 类上
	 * 方法： 当前方法有事务
	 * 类上： 类下的所有方法都有事务
	 * <p>
	 * 1. 只读模式：
	 * 只读模式可以提升查询事务的效率，如果事务中只有查询代码，使用只读模式
	 * 默认：boolean readOnly() default false;   需要加(readOnly = true)
	 * 在只读模式下修改，会报错：Connection is read only
	 * 解释：一般情况下都是通过类注解添加事务，此时类下的所有方法都有事务
	 * 那么查询方法可以通过再次添加注解，设置为只读模式，提高效率
	 * 2. 超时时间：
	 * 默认：永远不超时 -1
	 * 设置timeout = 时间(秒数)，超过时间，就会回滚事务和释放异常:transactionTimeOutException
	 * 注意点：
	 * 如果类上设置事务属性，方法也设置了事务注解，方法不会生效！！！
	 * 原因：方法上的注解覆盖掉类上的注解(默认值为-1，覆盖类上的超时时间)
	 * <p>
	 * 3. 指定异常回滚和指定异常不回滚
	 * 默认情况下：指定发生 运行时异常 时，事务才会回滚
	 * 我们可以指定Exception异常来控制所有异常都回滚 : rollbackFor = Exception.class
	 * 在回滚异常范围内，控制某个异常不回滚：noRollBack =
	 * 4. 隔离级别设置：
	 * 推荐设置第二个隔离级别
	 * isolation = Isolation.READ_COMITTED
	 * <p>
	 * 脏读：一个事物读取了另一个事务未提交的数据，会造成数据的错误
	 * 不可重复读：一个事务读取了另一个事务提交的修改数据
	 * 幻读：一个事务读取了另一个事务提交的插入数据
	 * <p>
	 * 不可重复读和幻读 不回造成数据的错误，但是不符合事务的一致性原则
	 * <p>
	 * 5. 事务的传播行为：
	 *
	 */

	@Transactional(readOnly = false, timeout = 3)
	public void changeInfo() {
		dao.updateAgeById(88, 1);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("-----------");
		dao.updateNameById("test1", 1);
	}

	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public void changeInfo01() {
		dao.updateAgeById(99, 1);
//		FileInputStream xxx = new FileInputStream("xxx");
		;
		dao.updateNameById("test02", 1);
	}

	/**
	 * 测试事务的传播行为
	 * 声明两个独立修改数据库的事务业务方法
	 */
	// 父方法如果有事务，就加入到父方法的事务中，最终是一个事务
	@Transactional(propagation = Propagation.REQUIRED)
//	@Transactional
	public void changeAge() {
		dao.updateAgeById(999, 1);
	}
	// 不管父方法是否有事务，我都是独立的事务，两个事务或者三个事务
	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	@Transactional
	public void changeName() {
		dao.updateNameById("事务传播测试", 1);
		int i = 1 / 0;
	}

	public void updateNameById(String name, int id) {
		dao.updateNameById(name, id);
	}

	public void updateAgeById(int age, int id) {
		dao.updateAgeById(age, id);
	}

}
