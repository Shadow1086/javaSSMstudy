# MyBatis 核心知识

> 本文聚焦 MyBatis 的核心使用链路：从快速入门、Mapper 映射、参数绑定，到动态 SQL、缓存机制和整合实践。
>
> 目标是让你在复习时能快速定位：**SQL 写在哪里、参数怎么传、结果如何映射、问题如何排查**。

## 一、Mybatis简介

### 1.1 简介

https://mybatis.org/mybatis-3/zh/index.html

**MyBatis** 是一款优秀的**持久层框架**，前身为 Apache iBatis，2010 年更名并迁移至 GitHub。它支持自定义 SQL、存储过程和高级映射，通过 XML 或注解将接口和 Java POJO 映射为数据库记录，几乎免除了所有 JDBC 样板代码。

### 1.2 持久层框架对比

| 框架 | 优点 | 缺点 |
| --- | --- | --- |
| **JDBC** | 性能最高，直接操作数据库 | SQL 与 Java 代码耦合，维护困难，代码冗长 |
| **Hibernate / JPA** | 操作简便，开发效率高，全自动 ORM | 复杂 SQL 难以优化，反射多导致性能下降 |
| **MyBatis** | 轻量级，SQL 与 Java 分离，灵活可控 | 开发效率略低于 Hibernate，需手写 SQL |

> **效率对比**：开发效率 `Hibernate > MyBatis > JDBC`；运行效率 `JDBC > MyBatis > Hibernate`。
>
> **MyBatis 定位**：半自动 ORM，适合对 SQL 有精细控制需求的场景，是目前企业开发**首选持久层框架**。

### 1.3 快速入门（基于 MyBatis3 方式）

**步骤1：准备数据模型**

```sql
CREATE DATABASE `mybatis-example`;

USE `mybatis-example`;

CREATE TABLE `t_emp`(
  emp_id INT AUTO_INCREMENT,
  emp_name CHAR(100),
  emp_salary DOUBLE(10,5),
  PRIMARY KEY(emp_id)
);

INSERT INTO `t_emp`(emp_name,emp_salary) VALUES("tom",200.33);
INSERT INTO `t_emp`(emp_name,emp_salary) VALUES("jerry",666.66);
INSERT INTO `t_emp`(emp_name,emp_salary) VALUES("andy",777.77);
```
**步骤2：项目搭建和准备**

依赖导入（`pom.xml`）：

```xml
<dependencies>
  <!-- mybatis依赖 -->
  <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.11</version>
  </dependency>

  <!-- MySQL驱动 mybatis底层依赖jdbc驱动实现,本次不需要导入连接池,mybatis自带! -->
  <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.25</version>
  </dependency>

  <!--junit5测试-->
  <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.3.1</version>
  </dependency>
</dependencies>
```
**步骤3：实体类准备**

```java
public class Employee {

    private Integer empId;

    private String empName;

    private Double empSalary;
    
    //getter | setter
}
```
**步骤4：准备 Mapper 接口和 Mapper XML 文件**

MyBatis 框架下，SQL 语句写在 **XML 文件**（推荐）或注解中，而非 Java 代码里。Mapper 接口只需定义方法签名，具体 SQL 在对应的 `XxxMapper.xml` 中实现，MyBatis 通过动态代理自动关联两者。

> ⚠️ **注意**：方法名对应 XML 中的 `id`，方法返回类型对应 `resultType`，接口全类名对应 XML 的 `namespace`。

1. 定义mapper接口

包：com.atguigu.mapper

```java
package com.atguigu.mapper;

import com.atguigu.pojo.Employee;

/**
 * t_emp表对应数据库SQL语句映射接口!
 *    接口只规定方法,参数和返回值!
 *    mapper.xml中编写具体SQL语句!
 */
public interface EmployeeMapper {

    /**
     * 根据员工id查询员工数据方法
     * @param empId  员工id
     * @return 员工实体对象
     */
    Employee selectEmployee(Integer empId);
    
}
```
2. 定义mapper xml

位置： resources/mappers/EmployeeMapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace等于mapper接口类的全限定名,这样实现对应 -->
<mapper namespace="com.atguigu.mapper.EmployeeMapper">
    
    <!-- 查询使用 select标签
            id = 方法名
            resultType = 返回值类型
            标签内编写SQL语句
     -->
    <select id="selectEmployee" resultType="com.atguigu.pojo.Employee">
        <!-- #{empId}代表动态传入的参数,并且进行赋值!后面详细讲解 -->
        select emp_id empId,emp_name empName, emp_salary empSalary from 
           t_emp where emp_id = #{empId}
    </select>
</mapper>
```

**注意：**
- 方法名和 SQL 的 **`id`** 一致
- 方法返回值和 **`resultType`** 一致
- 方法参数和 SQL 参数一致
- 接口全类名和映射配置文件的 **`namespace`** 一致

**步骤5：准备 MyBatis 配置文件**（`mybatis-config.xml`，整合 Spring 后可省略）

> **示例**：以下是 MyBatis 全局配置文件的完整写法。

```xml
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

  <!-- environments表示配置Mybatis的开发环境，可以配置多个环境，在众多具体环境中，使用default属性指定实际运行时使用的环境。default属性的取值是environment标签的id属性的值。 -->
  <environments default="development">
    <!-- environment表示配置Mybatis的一个具体的环境 -->
    <environment id="development">
      <!-- Mybatis的内置的事务管理器 -->
      <transactionManager type="JDBC"/>
      <!-- 配置数据源 -->
      <dataSource type="POOLED">
        <!-- 建立数据库连接的具体信息 -->
        <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/mybatis-example"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
      </dataSource>
    </environment>
  </environments>

  <mappers>
    <!-- Mapper注册：指定Mybatis映射文件的具体位置 -->
    <!-- mapper标签：配置一个具体的Mapper映射文件 -->
    <!-- resource属性：指定Mapper映射文件的实际存储位置，这里需要使用一个以类路径根目录为基准的相对路径 -->
    <!--    对Maven工程的目录结构来说，resources目录下的内容会直接放入类路径，所以这里我们可以以resources目录为基准 -->
    <mapper resource="mappers/EmployeeMapper.xml"/>
  </mappers>

</configuration>
```
**步骤6：运行和测试**

```java
/**
 * projectName: com.atguigu.test
 *
 * description: 测试类
 */
public class MyBatisTest {

    @Test
    public void testSelectEmployee() throws IOException {

        // 1.创建SqlSessionFactory对象
        // ①声明Mybatis全局配置文件的路径
        String mybatisConfigFilePath = "mybatis-config.xml";

        // ②以输入流的形式加载Mybatis配置文件
        InputStream inputStream = Resources.getResourceAsStream(mybatisConfigFilePath);

        // ③基于读取Mybatis配置文件的输入流创建SqlSessionFactory对象
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 2.使用SqlSessionFactory对象开启一个会话
        SqlSession session = sessionFactory.openSession();

        // 3.根据EmployeeMapper接口的Class对象获取Mapper接口类型的对象(动态代理技术)
        EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);

        // 4. 调用代理类方法既可以触发对应的SQL语句
        Employee employee = employeeMapper.selectEmployee(1);

        System.out.println("employee = " + employee);

        // 4.关闭SqlSession
        session.commit(); //提交事务 [DQL不需要,其他需要]
        session.close(); //关闭会话

    }
}
```

说明：

- SqlSession：代表Java程序和数据库之间的会话。（HttpSession是Java程序和浏览器之间的会话）
- SqlSessionFactory：是“生产”SqlSession的“工厂”。
- 工厂模式：如果创建某一个对象，使用的过程基本固定，那么我们就可以把创建这个对象的相关代码封装到一个“工厂类”中，以后都使用这个工厂类来“生产”我们需要的对象。
**SqlSession 和 HttpSession 区别**：
- **HttpSession**：工作在 Web 服务器上，属于表述层，代表浏览器和 Web 服务器之间的会话。
- **SqlSession**：不依赖 Web 服务器，属于持久化层，代表 Java 程序和数据库之间的会话。

## 二、MyBatis基本使用

### 2.1 向SQL语句传参

#### 2.1.1 **mybatis日志输出配置**

mybatis配置文件设计标签和顶层结构如下：

- configuration（配置）
- [properties（属性）](https://mybatis.org/mybatis-3/zh/configuration.html#properties)
- [settings（设置）](https://mybatis.org/mybatis-3/zh/configuration.html#settings)
- [typeAliases（类型别名）](https://mybatis.org/mybatis-3/zh/configuration.html#typeAliases)
- [typeHandlers（类型处理器）](https://mybatis.org/mybatis-3/zh/configuration.html#typeHandlers)
- [objectFactory（对象工厂）](https://mybatis.org/mybatis-3/zh/configuration.html#objectFactory)
- [plugins（插件）](https://mybatis.org/mybatis-3/zh/configuration.html#plugins)
- [environments（环境配置）](https://mybatis.org/mybatis-3/zh/configuration.html#environments)
- environment（环境变量）
- transactionManager（事务管理器）
- dataSource（数据源）
- [databaseIdProvider（数据库厂商标识）](https://mybatis.org/mybatis-3/zh/configuration.html#databaseIdProvider)
- [mappers（映射器）](https://mybatis.org/mybatis-3/zh/configuration.html#mappers)

我们可以在mybatis的配置文件使用**settings标签**设置，输出运过程SQL日志！

通过查看日志，我们可以判定#{} 和 ${}的输出效果！

settings设置项：

| 配置项 | 说明 | 可选值 | 默认值 |
| --- | --- | --- | --- |
| `logImpl` | 指定 MyBatis 使用的日志实现 | `SLF4J`、`LOG4J`（3.5.9 起废弃）、`LOG4J2`、`JDK_LOGGING`、`COMMONS_LOGGING`、`STDOUT_LOGGING`、`NO_LOGGING` | 未设置（自动查找） |

日志配置：

```xml
<settings>
  <!-- SLF4J 选择slf4j输出！ -->
  <setting name="logImpl" value="SLF4J"/>
</settings>
```

#### 2.1.2 `#{}` 与 `${}` 对比

| 对比项 | `#{}` | `${}` |
| --- | --- | --- |
| 底层实现 | **预编译占位符**（`?`） | **字符串拼接** |
| SQL 注入风险 | 无（安全） | **有风险** |
| 适用场景 | 传递**值**（绝大多数情况） | 传递**列名、表名、关键字**等 |

> ⚠️ **注意**：`${}` 存在 SQL 注入风险，**实际开发中能用 `#{}` 就不用 `${}`**。仅在需要动态传入列名、表名等 SQL 结构性内容时才使用 `${}`。

```java
// 特殊场景：动态列名只能用 ${}
@Select("select * from user where ${column} = #{value}")
User findByColumn(@Param("column") String column,
                  @Param("value") String value);
```

### 2.2 数据输入

#### 2.2.1 **Mybatis总体机制概括**

上层（如 `Service`）调用 `Mapper` 接口时，MyBatis 会根据参数类型完成 `#{}` 绑定并执行 SQL。

#### 2.2.2 **概念说明**

| 类型分类 | 典型类型 |
| --- | --- |
| 简单类型 | 基本类型（`int`、`double`）、包装类型（`Integer`、`Double`）、`String` |
| 复杂类型 | 实体类（`Employee`）、集合（`List` / `Set` / `Map`）、数组（`int[]` / `String[]`）、复合类型（如 `List<Employee>`） |

#### 2.2.3 **单个简单类型参数**

Mapper接口中抽象方法的声明

```java
Employee selectEmployee(Integer empId);
```

SQL语句

```xml
<select id="selectEmployee" resultType="com.atguigu.mybatis.entity.Employee">
  select emp_id empId,emp_name empName,emp_salary empSalary from t_emp where emp_id=#{empId}
</select>
```

> 单个简单类型参数，在#{}中可以随意命名，但是没有必要。通常还是使用和接口方法参数同名。

#### 2.2.4 **实体类类型参数**

Mapper接口中抽象方法的声明

```java
int insertEmployee(Employee employee);
```

SQL语句

```xml
<insert id="insertEmployee">
  insert into t_emp(emp_name,emp_salary) values(#{empName},#{empSalary})
</insert>
```

对应关系

> MyBatis 会根据 `#{}` 中的属性名，调用实体类的 `getXxx()` 方法取值，填入 SQL 占位符。

#### 2.2.5 **零散的简单类型数据**

零散的多个简单类型参数，如果没有特殊处理，那么Mybatis无法识别自定义名称：

Mapper接口中抽象方法的声明

```java
int updateEmployee(@Param("empId") Integer empId,@Param("empSalary") Double empSalary);
```

SQL语句

```xml
<update id="updateEmployee">
  update t_emp set emp_salary=#{empSalary} where emp_id=#{empId}
</update>
```

对应关系

#### 2.2.6 **Map类型参数**

Mapper接口中抽象方法的声明

```java
int updateEmployeeByMap(Map<String, Object> paramMap);
```

SQL语句

```xml
<update id="updateEmployeeByMap">

  update t_emp set emp_salary=#{empSalaryKey} where emp_id=#{empIdKey}

</update>
```

junit测试

```java
private SqlSession session;
//junit5会在每一个@Test方法前执行@BeforeEach方法
@BeforeEach
public void init() throws IOException {
    session = new SqlSessionFactoryBuilder()
            .build(
                    Resources.getResourceAsStream("mybatis-config.xml"))
            .openSession();
}

@Test
public void testUpdateEmpNameByMap() {
  EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
  Map<String, Object> paramMap = new HashMap<>();
  paramMap.put("empSalaryKey", 999.99);
  paramMap.put("empIdKey", 5);
  int result = mapper.updateEmployeeByMap(paramMap);
  log.info("result = " + result);
}

//junit5会在每一个@Test方法后执行@@AfterEach方法
@AfterEach
public void clear() {
    session.commit();
    session.close();
}

```

**使用场景**：参数零散且无对应实体类，且数量多到用 `@Param` 一一传入嫌麻烦时，统一封装为 `Map`。`#{}` 中写 Map 的 key。

### 2.3数据输出

#### 2.3.1 输出概述

数据输出分两类：

- **增删改**：返回受影响行数（`int`/`long`）
- **查询**：返回单值、实体、`Map` 或集合

核心就是在映射语句中正确指定返回类型（`resultType` / `resultMap`），并在插入时按需开启主键回显。

#### 2.3.2 单个简单类型

Mapper接口中的抽象方法

```java
int selectEmpCount();
```

SQL语句

```xml
<select id="selectEmpCount" resultType="int">
  select count(*) from t_emp
</select>
```

> Mybatis 内部给常用的数据类型设定了很多别名。 以 int 类型为例，可以写的名称有：int、integer、Integer、java.lang.Integer、Int、INT、INTEGER 等等。

junit测试

```java
@Test

public void testEmpCount() {

  EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);

  int count = employeeMapper.selectEmpCount();

  log.info("count = " + count);

}
```

**`resultType`** 支持全限定名、别名或集合泛型类型。MyBatis 内建了常用类型的别名（不区分大小写），完整列表如下：

```xml
<typeAliases>
  <typeAlias alias="Author" type="domain.blog.Author"/>
  <typeAlias alias="Blog" type="domain.blog.Blog"/>
</typeAliases>
```

也可以指定包名，MyBatis 会将包下所有 Bean 以首字母小写的非限定类名作为别名；若有 `@Alias` 注解则以注解值为准：

```xml
<typeAliases> <package name="domain.blog"/> </typeAliases>
```

下面是 MyBatis 内建的常见 Java 类型别名（不区分大小写，原始类型前缀下划线以避命名冲突）：

```java
@Alias("author")
public class Author {
    ...
}
```

下面是Mybatis为常见的 Java 类型内建的类型别名。它们都是不区分大小写的，注意，为了应对原始类型的命名重复，采取了特殊的命名风格。

| 别名                      | 映射的类型 |
| ------------------------- | ---------- |
| _byte                     | byte       |
| _char (since 3.5.10)      | char       |
| _character (since 3.5.10) | char       |
| _long                     | long       |
| _short                    | short      |
| _int                      | int        |
| _integer                  | int        |
| _double                   | double     |
| _float                    | float      |
| _boolean                  | boolean    |
| string                    | String     |
| byte                      | Byte       |
| char (since 3.5.10)       | Character  |
| character (since 3.5.10)  | Character  |
| long                      | Long       |
| short                     | Short      |
| int                       | Integer    |
| integer                   | Integer    |
| double                    | Double     |
| float                     | Float      |
| boolean                   | Boolean    |
| date                      | Date       |
| decimal                   | BigDecimal |
| bigdecimal                | BigDecimal |
| biginteger                | BigInteger |
| object                    | Object     |
| object[]                  | Object[]   |
| map                       | Map        |
| hashmap                   | HashMap    |
| list                      | List       |
| arraylist                 | ArrayList  |
| collection                | Collection |

#### 2.3.3 返回实体类对象

Mapper接口的抽象方法

```java
Employee selectEmployee(Integer empId);

```

SQL语句

```xml
<!-- 编写具体的SQL语句，使用id属性唯一的标记一条SQL语句 -->
<!-- resultType属性：指定封装查询结果的Java实体类的全类名 -->
<select id="selectEmployee" resultType="com.atguigu.mybatis.entity.Employee">

  <!-- Mybatis负责把SQL语句中的#{}部分替换成“?”占位符 -->
  <!-- 给每一个字段设置一个别名，让别名和Java实体类中属性名一致 -->
  select emp_id empId,emp_name empName,emp_salary empSalary from t_emp where emp_id=#{maomi}

</select>
```

通过给数据库表字段加别名，让查询结果的每一列都和Java实体类中属性对应起来。

增加全局配置自动识别对应关系

在 Mybatis 全局配置文件中，做了下面的配置，select语句中可以不给字段设置别名

```xml
<!-- 在全局范围内对Mybatis进行配置 -->
<settings>

  <!-- 具体配置 -->
  <!-- 从org.apache.ibatis.session.Configuration类中可以查看能使用的配置项 -->
  <!-- 将mapUnderscoreToCamelCase属性配置为true，表示开启自动映射驼峰式命名规则 -->
  <!-- 规则要求数据库表字段命名方式：单词_单词 -->
  <!-- 规则要求Java实体类属性名命名方式：首字母小写的驼峰式命名 -->
  <setting name="mapUnderscoreToCamelCase" value="true"/>

</settings>
```

#### 2.3.4 返回Map类型

适用于SQL查询返回的各个字段综合起来并不和任何一个现有的实体类对应，没法封装到实体类对象中。能够封装成实体类类型的，就不使用Map类型。

Mapper接口的抽象方法

```java
Map<String,Object> selectEmpNameAndMaxSalary();
```

SQL语句

```xml
<!-- Map<String,Object> selectEmpNameAndMaxSalary(); -->
<!-- 返回工资最高的员工的姓名和他的工资 -->
<select id="selectEmpNameAndMaxSalary" resultType="map">
  SELECT
    emp_name 员工姓名,
    emp_salary 员工工资,
    (SELECT AVG(emp_salary) FROM t_emp) 部门平均工资
  FROM t_emp WHERE emp_salary=(
    SELECT MAX(emp_salary) FROM t_emp
  )
</select>
```

junit测试

```java
@Test
public void testQueryEmpNameAndSalary() {

  EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);

  Map<String, Object> resultMap = employeeMapper.selectEmpNameAndMaxSalary();

  Set<Map.Entry<String, Object>> entrySet = resultMap.entrySet();

  for (Map.Entry<String, Object> entry : entrySet) {

    String key = entry.getKey();

    Object value = entry.getValue();

    log.info(key + "=" + value);

  }
}
```

#### 2.3.5 返回List类型

查询结果返回多个实体类对象，希望把多个实体类对象放在List集合中返回。此时不需要任何特殊处理，在resultType属性中还是设置实体类类型即可。

Mapper接口中抽象方法

```java
List<Employee> selectAll();
```

SQL语句

```xml
<!-- List<Employee> selectAll(); -->
<select id="selectAll" resultType="com.atguigu.mybatis.entity.Employee">
  select emp_id empId,emp_name empName,emp_salary empSalary
  from t_emp
</select>
```

junit测试

```java
@Test
public void testSelectAll() {
  EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
  List<Employee> employeeList = employeeMapper.selectAll();
  for (Employee employee : employeeList) {
    log.info("employee = " + employee);
  }
}
```

#### 2.3.6 返回主键值
1. **自增长类型主键**

Mapper接口中的抽象方法

```java
int insertEmployee(Employee employee);
```

SQL语句

```xml
<!-- int insertEmployee(Employee employee); -->
<!-- useGeneratedKeys属性字面意思就是“使用生成的主键” -->
<!-- keyProperty属性可以指定主键在实体类对象中对应的属性名，Mybatis会将拿到的主键值存入这个属性 -->
<insert id="insertEmployee" useGeneratedKeys="true" keyProperty="empId">
  insert into t_emp(emp_name,emp_salary)
  values(#{empName},#{empSalary})
</insert>
```

junit测试

```java
@Test
public void testSaveEmp() {
  EmployeeMapper employeeMapper = session.getMapper(EmployeeMapper.class);
  Employee employee = new Employee();
  employee.setEmpName("john");
  employee.setEmpSalary(666.66);
  employeeMapper.insertEmployee(employee);
  log.info("employee.getEmpId() = " + employee.getEmpId());
}
```

注意

Mybatis是将自增主键的值设置到实体类对象中，而不是以Mapper接口方法返回值的形式返回。
2. **非自增长类型主键**

而对于不支持自增型主键的数据库（例如 Oracle）或者字符串类型主键，则可以使用 selectKey 子元素：selectKey 元素将会首先运行，id 会被设置，然后插入语句会被调用！

使用 `selectKey` 帮助插入UUID作为字符串类型主键示例：

```xml
<insert id="insertUser" parameterType="User">
    <selectKey keyProperty="id" resultType="java.lang.String"
        order="BEFORE">
        SELECT UUID() as id
    </selectKey>
    INSERT INTO user (id, username, password) 
    VALUES (
        #{id},
        #{username},
        #{password}
    )
</insert>

```

> `<selectKey>` 在 `INSERT` 执行前先查询并设置 `id`（此处为 `UUID()`），`order="BEFORE"` 确保插入时 `id` 已就绪。

#### 2.3.7 实体类属性和数据库字段对应关系
1. 别名对应

将字段的别名设置成和实体类属性一致。

```xml
<!-- 编写具体的SQL语句，使用id属性唯一的标记一条SQL语句 -->
<!-- resultType属性：指定封装查询结果的Java实体类的全类名 -->
<select id="selectEmployee" resultType="com.atguigu.mybatis.entity.Employee">

  <!-- Mybatis负责把SQL语句中的#{}部分替换成“?”占位符 -->
  <!-- 给每一个字段设置一个别名，让别名和Java实体类中属性名一致 -->
  select emp_id empId,emp_name empName,emp_salary empSalary from t_emp where emp_id=#{maomi}

</select>
```

> 关于实体类属性的约定：
getXxx()方法、setXxx()方法把方法名中的get或set去掉，首字母小写。
2. 全局配置自动识别驼峰式命名规则

在Mybatis全局配置文件加入如下配置：

```xml
<!-- 使用settings对Mybatis全局进行设置 -->
<settings>

  <!-- 将xxx_xxx这样的列名自动映射到xxXxx这样驼峰式命名的属性名 -->
  <setting name="mapUnderscoreToCamelCase" value="true"/>

</settings>
```

SQL语句中可以不使用别名

```xml
<!-- Employee selectEmployee(Integer empId); -->
<select id="selectEmployee" resultType="com.atguigu.mybatis.entity.Employee">

  select emp_id,emp_name,emp_salary from t_emp where emp_id=#{empId}

</select>
```
3. 使用resultMap

使用resultMap标签定义对应关系，再在后面的SQL语句中引用这个对应关系

```xml
<!-- 专门声明一个resultMap设定column到property之间的对应关系 -->
<resultMap id="selectEmployeeByRMResultMap" type="com.atguigu.mybatis.entity.Employee">

  <!-- 使用id标签设置主键列和主键属性之间的对应关系 -->
  <!-- column属性用于指定字段名；property属性用于指定Java实体类属性名 -->
  <id column="emp_id" property="empId"/>

  <!-- 使用result标签设置普通字段和Java实体类属性之间的关系 -->
  <result column="emp_name" property="empName"/>

  <result column="emp_salary" property="empSalary"/>

</resultMap>

<!-- Employee selectEmployeeByRM(Integer empId); -->
<select id="selectEmployeeByRM" resultMap="selectEmployeeByRMResultMap">

  select emp_id,emp_name,emp_salary from t_emp where emp_id=#{empId}

</select>
```

### 2.4 CRUD强化练习
1. 准备数据库数据

首先，我们需要准备一张名为 `user` 的表。该表包含字段 id（主键）、username、password。创建SQL如下：

```sql
CREATE TABLE `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

```
2. 实体类准备

接下来，我们需要定义一个实体类 `User`，来对应 user 表的一行数据。

```sql
@Data //lombok
public class User {
  private Integer id;
  private String username;
  private String password;
}
```
3. Mapper接口定义

定义一个 Mapper 接口 `UserMapper`，并在其中添加 user 表的增、删、改、查方法。

```java
public interface UserMapper {
  
  int insert(User user);

  int update(User user);

  int delete(Integer id);

  User selectById(Integer id);

  List<User> selectAll();
}
```
4. MapperXML编写

在 resources /mappers目录下创建一个名为 `UserMapper.xml` 的 XML 文件，包含与 Mapper 接口中相同的五个 SQL 语句，并在其中，将查询结果映射到 `User` 实体中。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace等于mapper接口类的全限定名,这样实现对应 -->
<mapper namespace="com.atguigu.mapper.UserMapper">
  <!-- 定义一个插入语句，并获取主键值 -->
  <insert id="insert" useGeneratedKeys="true" keyProperty="id">
    INSERT INTO user(username, password)
                VALUES(#{username}, #{password})
  </insert>
  
  <update id="update">
    UPDATE user SET username=#{username}, password=#{password}
    WHERE id=#{id}
  </update>
  
  <delete id="delete">
    DELETE FROM user WHERE id=#{id}
  </delete>
  <!-- resultType使用user别名，稍后需要配置！-->
  <select id="selectById" resultType="user">
    SELECT id, username, password FROM user WHERE id=#{id}
  </select>
  
  <!-- resultType返回值类型为集合，所以只写范型即可！ -->
  <select id="selectAll" resultType="user">
    SELECT id, username, password FROM user
  </select>
  
</mapper>

```
5. MyBatis配置文件

位置：resources: mybatis-config.xml 

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <settings>
        <!-- 开启驼峰式映射-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <!-- 开启logback日志输出-->
        <setting name="logImpl" value="SLF4J"/>
    </settings>

    <typeAliases>
        <!-- 给实体类起别名 -->
        <package name="com.atguigu.pojo"/>
    </typeAliases>

    <!-- environments表示配置Mybatis的开发环境，可以配置多个环境，在众多具体环境中，使用default属性指定实际运行时使用的环境。default属性的取值是environment标签的id属性的值。 -->
    <environments default="development">
        <!-- environment表示配置Mybatis的一个具体的环境 -->
        <environment id="development">
            <!-- Mybatis的内置的事务管理器 -->
            <transactionManager type="JDBC"/>
            <!-- 配置数据源 -->
            <dataSource type="POOLED">
                <!-- 建立数据库连接的具体信息 -->
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatis-example"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>

    <mappers>
        <!-- Mapper注册：指定Mybatis映射文件的具体位置 -->
        <!-- mapper标签：配置一个具体的Mapper映射文件 -->
        <!-- resource属性：指定Mapper映射文件的实际存储位置，这里需要使用一个以类路径根目录为基准的相对路径 -->
        <!--    对Maven工程的目录结构来说，resources目录下的内容会直接放入类路径，所以这里我们可以以resources目录为基准 -->
        <mapper resource="mappers/UserMapper.xml"/>
    </mappers>

</configuration>
```
6. 效果测试

```java
package com.atguigu.test;

import com.atguigu.mapper.UserMapper;
import com.atguigu.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * projectName: com.atguigu.test
 */
public class MyBatisTest {

    private SqlSession session;
    // junit会在每一个@Test方法前执行@BeforeEach方法

    @BeforeEach
    public void init() throws IOException {
        session = new SqlSessionFactoryBuilder()
                .build(
                        Resources.getResourceAsStream("mybatis-config.xml"))
                .openSession();
    }

    @Test
    public void createTest() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        UserMapper userMapper = session.getMapper(UserMapper.class);
        userMapper.insert(user);
        System.out.println(user);
    }

    @Test
    public void updateTest() {
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectById(1);
        user.setUsername("root");
        user.setPassword("111111");
        userMapper.update(user);
        user = userMapper.selectById(1);
        System.out.println(user);
    }

    @Test
    public void deleteTest() {
        UserMapper userMapper = session.getMapper(UserMapper.class);
        userMapper.delete(1);
        User user = userMapper.selectById(1);
        System.out.println("user = " + user);
    }

    @Test
    public void selectByIdTest() {
        UserMapper userMapper = session.getMapper(UserMapper.class);
        User user = userMapper.selectById(1);
        System.out.println("user = " + user);
    }

    @Test
    public void selectAllTest() {
        UserMapper userMapper = session.getMapper(UserMapper.class);
        List<User> userList = userMapper.selectAll();
        System.out.println("userList = " + userList);
    }

    // junit会在每一个@Test方法后执行@@AfterEach方法
    @AfterEach
    public void clear() {
        session.commit();
        session.close();
    }
}

```

### 2.5 mapperXML标签总结

SQL 映射文件的顶级元素（按定义顺序）：

- `insert` – 映射插入语句。
- `update` – 映射更新语句。
- `delete` – 映射删除语句。
- `select` – 映射查询语句。

**select标签：**

> **示例**：以下展示 `select` 标签的基本格式。

```xml
<select id="selectPerson" 
resultType="hashmap" resultMap="自定义结构"> SELECT * FROM PERSON WHERE ID = #{id} </select>
```

`#{}` 对应 JDBC 预处理语句的 `?` 占位符，`${}` 是字符串拼接（已在 2.1.2 节说明区别）。

```java
// 近似的 JDBC 代码，非 MyBatis 代码...
String selectPerson = "SELECT * FROM PERSON WHERE ID=?";
PreparedStatement ps = conn.prepareStatement(selectPerson);
ps.setInt(1,id);
```

select 元素允许你配置很多属性来配置每条语句的行为细节：

| 属性            | 描述                                                         |
| --------------- | ------------------------------------------------------------ |
| `id`            | 在命名空间中唯一的标识符，可以被用来引用这条语句。           |
| `resultType`    | 期望从这条语句中返回结果的类全限定名或别名。 注意，如果返回的是集合，那应该设置为集合包含的类型，而不是集合本身的类型。 resultType 和 resultMap 之间只能同时使用一个。 |
| `resultMap`     | 对外部 resultMap 的命名引用。结果映射是 MyBatis 最强大的特性，如果你对其理解透彻，许多复杂的映射问题都能迎刃而解。 resultType 和 resultMap 之间只能同时使用一个。 |
| `timeout`       | 这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为未设置（unset）（依赖数据库驱动）。 |
| `statementType` | 可选 STATEMENT，PREPARED 或 CALLABLE。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 CallableStatement，默认值：PREPARED。 |

**insert, update 和 delete标签：**

数据变更语句 insert，update 和 delete 的实现非常接近：

```xml
<insert
  id="insertAuthor"
  statementType="PREPARED"
  keyProperty=""
  keyColumn=""
  useGeneratedKeys=""
  timeout="20">

<update
  id="updateAuthor"
  statementType="PREPARED"
  timeout="20">

<delete
  id="deleteAuthor"
  statementType="PREPARED"
  timeout="20">
```

| 属性               | 描述                                                         |
| ------------------ | ------------------------------------------------------------ |
| `id`               | 在命名空间中唯一的标识符，可以被用来引用这条语句。           |
| `timeout`          | 这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为未设置（unset）（依赖数据库驱动）。 |
| `statementType`    | 可选 STATEMENT，PREPARED 或 CALLABLE。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 CallableStatement，默认值：PREPARED。 |
| `useGeneratedKeys` | （仅适用于 insert 和 update）这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键（比如：像 MySQL 和 SQL Server 这样的关系型数据库管理系统的自动递增字段），默认值：false。 |
| `keyProperty`      | （仅适用于 insert 和 update）指定能够唯一识别对象的属性，MyBatis 会使用 getGeneratedKeys 的返回值或 insert 语句的 selectKey 子元素设置它的值，默认值：未设置（`unset`）。如果生成列不止一个，可以用逗号分隔多个属性名称。 |
| `keyColumn`        | （仅适用于 insert 和 update）设置生成键值在表中的列名，在某些数据库（像 PostgreSQL）中，当主键列不是表中的第一列的时候，是必须设置的。如果生成列不止一个，可以用逗号分隔多个属性名称。 |

## 三、MyBatis多表映射

### 3.1 多表映射概念
1. **多表查询结果映射思路**

**多表查询的核心解法**：用 `ResultMap` 将 JOIN 查询的多列结果映射到嵌套实体中。

**学习目标**：
1. 掌握多表联查 SQL 的写法
2. 根据查询关系设计实体类（含嵌套对象或集合属性）
3. 用 `resultMap` 完成多表结果映射

```xml
<!-- 非常复杂的语句 -->
<select id="selectBlogDetails" resultMap="detailedBlogResultMap">
  select
       B.id as blog_id,
       B.title as blog_title,
       B.author_id as blog_author_id,
       A.id as author_id,
       A.username as author_username,
       A.password as author_password,
       A.email as author_email,
       A.bio as author_bio,
       A.favourite_section as author_favourite_section,
       P.id as post_id,
       P.blog_id as post_blog_id,
       P.author_id as post_author_id,
       P.created_on as post_created_on,
       P.section as post_section,
       P.subject as post_subject,
       P.draft as draft,
       P.body as post_body,
       C.id as comment_id,
       C.post_id as comment_post_id,
       C.name as comment_name,
       C.comment as comment_text,
       T.id as tag_id,
       T.name as tag_name
  from Blog B
       left outer join Author A on B.author_id = A.id
       left outer join Post P on B.id = P.blog_id
       left outer join Comment C on P.id = C.post_id
       left outer join Post_Tag PT on PT.post_id = P.id
       left outer join Tag T on PT.tag_id = T.id
  where B.id = #{id}
</select>
```

你可能想把它映射到一个智能的对象模型，这个对象表示了一篇博客，它由某位作者所写，有很多的博文，每篇博文有零或多条的评论和标签。 我们先来看看下面这个完整的例子，它是一个非常复杂的结果映射（假设作者，博客，博文，评论和标签都是类型别名）。 虽然它看起来令人望而生畏，但其实非常简单。 

```java
<!-- 非常复杂的结果映射 -->
<resultMap id="detailedBlogResultMap" type="Blog">
  <constructor>
    <idArg column="blog_id" javaType="int"/>
  </constructor>
  <result property="title" column="blog_title"/>
  <association property="author" javaType="Author">
    <id property="id" column="author_id"/>
    <result property="username" column="author_username"/>
    <result property="password" column="author_password"/>
    <result property="email" column="author_email"/>
    <result property="bio" column="author_bio"/>
    <result property="favouriteSection" column="author_favourite_section"/>
  </association>
  <collection property="posts" ofType="Post">
    <id property="id" column="post_id"/>
    <result property="subject" column="post_subject"/>
    <association property="author" javaType="Author"/>
    <collection property="comments" ofType="Comment">
      <id property="id" column="comment_id"/>
    </collection>
    <collection property="tags" ofType="Tag" >
      <id property="id" column="tag_id"/>
    </collection>
  </collection>
</resultMap>
```

上面官方示例展示了 `ResultMap` 处理复杂多层嵌套的能力，下面从简单关系开始学习。
2. **实体类设计方案**

**实体类设计规则**（两两关系考虑，按实际查询方向设计）：

| 关系 | 实例 | 实体类设计 |
| --- | --- | --- |
| 对一 | 订单 → 用户 | 包含**单个**对方对象属性 |
| 对多 | 用户 → 订单列表 | 包含对方类型的 **`List`** 属性 |

> 只有发生多表查询时才需要修改实体类；无论多少表联查，都按两两关系设计；只关注本次查询相关的属性。

```java
public class Customer {

  private Integer customerId;
  private String customerName;

}

public class Order {

  private Integer orderId;
  private String orderName;
  private Customer customer;// 体现的是对一的关系

}  

```
- 对多: 用户对应的订单，讲师对应的学生或者学生对应的讲师都是对多关系：

```java
public class Customer {

  private Integer customerId;
  private String customerName;
  private List<Order> orderList;// 体现的是对多的关系
}

public class Order {

  private Integer orderId;
  private String orderName;
  private Customer customer;// 体现的是对一的关系
  
}

//查询客户和客户对应的订单集合  不要管!
```

只有真实发生多表查询时，才需要修改实体类；查询映射时只关注本次查询相关的属性。
3. **多表映射案例准备**

数据库：

```sql
CREATE TABLE `t_customer` (`customer_id` INT NOT NULL AUTO_INCREMENT, `customer_name` CHAR(100), PRIMARY KEY (`customer_id`) );

CREATE TABLE `t_order` ( `order_id` INT NOT NULL AUTO_INCREMENT, `order_name` CHAR(100), `customer_id` INT, PRIMARY KEY (`order_id`) ); 

INSERT INTO `t_customer` (`customer_name`) VALUES ('c01');

INSERT INTO `t_order` (`order_name`, `customer_id`) VALUES ('o1', '1');
INSERT INTO `t_order` (`order_name`, `customer_id`) VALUES ('o2', '1');
INSERT INTO `t_order` (`order_name`, `customer_id`) VALUES ('o3', '1'); 
```

> 实际开发中通常不在开发阶段设置外键约束，功能稳定后再加。

```java
@Data
public class Customer {

  private Integer customerId;
  private String customerName;
  private List<Order> orderList;// 体现的是对多的关系
  
}  

@Data
public class Order {
  private Integer orderId;
  private String orderName;
  private Customer customer;// 体现的是对一的关系
  
}  

```

### 3.2 对一映射
1. 需求说明

根据ID查询订单，以及订单关联的用户的信息！
2. OrderMapper接口

```java
public interface OrderMapper {
  Order selectOrderWithCustomer(Integer orderId);
}
```
3. OrderMapper.xml配置文件

```xml
<!-- 创建resultMap实现“对一”关联关系映射 -->
<!-- id属性：通常设置为这个resultMap所服务的那条SQL语句的id加上“ResultMap” -->
<!-- type属性：要设置为这个resultMap所服务的那条SQL语句最终要返回的类型 -->
<resultMap id="selectOrderWithCustomerResultMap" type="order">

  <!-- 先设置Order自身属性和字段的对应关系 -->
  <id column="order_id" property="orderId"/>

  <result column="order_name" property="orderName"/>

  <!-- 使用association标签配置“对一”关联关系 -->
  <!-- property属性：在Order类中对一的一端进行引用时使用的属性名 -->
  <!-- javaType属性：一的一端类的全类名 -->
  <association property="customer" javaType="customer">

    <!-- 配置Customer类的属性和字段名之间的对应关系 -->
    <id column="customer_id" property="customerId"/>
    <result column="customer_name" property="customerName"/>

  </association>

</resultMap>

<!-- Order selectOrderWithCustomer(Integer orderId); -->
<select id="selectOrderWithCustomer" resultMap="selectOrderWithCustomerResultMap">

  SELECT order_id,order_name,c.customer_id,customer_name
  FROM t_order o
  LEFT JOIN t_customer c
  ON o.customer_id=c.customer_id
  WHERE o.order_id=#{orderId}

</select>
```

对应关系可以参考下图：

4. Mybatis全局注册Mapper文件

```xml
<!-- 注册Mapper配置文件：告诉Mybatis我们的Mapper配置文件的位置 -->
<mappers>

  <!-- 在mapper标签的resource属性中指定Mapper配置文件以“类路径根目录”为基准的相对路径 -->
  <mapper resource="mappers/OrderMapper.xml"/>

</mappers>
```
5. junit测试程序

```java
@Slf4j
public class MyBatisTest {

    private SqlSession session;
    // junit会在每一个@Test方法前执行@BeforeEach方法

    @BeforeEach
    public void init() throws IOException {
        session = new SqlSessionFactoryBuilder()
                .build(
                        Resources.getResourceAsStream("mybatis-config.xml"))
                .openSession();
    }

    @Test
    public void testRelationshipToOne() {
    
      OrderMapper orderMapper = session.getMapper(OrderMapper.class);
      // 查询Order对象，检查是否同时查询了关联的Customer对象
      Order order = orderMapper.selectOrderWithCustomer(2);
      log.info("order = " + order);
    
    }

    // junit会在每一个@Test方法后执行@@AfterEach方法
    @AfterEach
    public void clear() {
        session.commit();
        session.close();
    }
}
```
6. 关键词

在“对一”关联关系中，我们的配置比较多，但是关键词就只有：**association**和**javaType**

### 3.3 对多映射
1. 需求说明

查询客户和客户关联的订单信息！
2. CustomerMapper接口

```java
public interface CustomerMapper {

  Customer selectCustomerWithOrderList(Integer customerId);

}
```
3. CustomerMapper.xml文件

```java
<!-- 配置resultMap实现从Customer到OrderList的“对多”关联关系 -->
<resultMap id="selectCustomerWithOrderListResultMap"

  type="customer">

  <!-- 映射Customer本身的属性 -->
  <id column="customer_id" property="customerId"/>

  <result column="customer_name" property="customerName"/>

  <!-- collection标签：映射“对多”的关联关系 -->
  <!-- property属性：在Customer类中，关联“多”的一端的属性名 -->
  <!-- ofType属性：集合属性中元素的类型 -->
  <collection property="orderList" ofType="order">

    <!-- 映射Order的属性 -->
    <id column="order_id" property="orderId"/>

    <result column="order_name" property="orderName"/>

  </collection>

</resultMap>

<!-- Customer selectCustomerWithOrderList(Integer customerId); -->
<select id="selectCustomerWithOrderList" resultMap="selectCustomerWithOrderListResultMap">
  SELECT c.customer_id,c.customer_name,o.order_id,o.order_name
  FROM t_customer c
  LEFT JOIN t_order o
  ON c.customer_id=o.customer_id
  WHERE c.customer_id=#{customerId}
</select>
```

对应关系可以参考下图：

4. Mybatis全局注册Mapper文件

```xml
<!-- 注册Mapper配置文件：告诉Mybatis我们的Mapper配置文件的位置 -->
<mappers>
  <!-- 在mapper标签的resource属性中指定Mapper配置文件以“类路径根目录”为基准的相对路径 -->
  <mapper resource="mappers/OrderMapper.xml"/>
  <mapper resource="mappers/CustomerMapper.xml"/>
</mappers>
```
5. junit测试程序

```java
@Test
public void testRelationshipToMulti() {

  CustomerMapper customerMapper = session.getMapper(CustomerMapper.class);
  // 查询Customer对象同时将关联的Order集合查询出来
  Customer customer = customerMapper.selectCustomerWithOrderList(1);
  log.info("customer.getCustomerId() = " + customer.getCustomerId());
  log.info("customer.getCustomerName() = " + customer.getCustomerName());
  List<Order> orderList = customer.getOrderList();
  for (Order order : orderList) {
    log.info("order = " + order);
  }
}
```
6. 关键词

在“对多”关联关系中，同样有很多配置，但是提炼出来最关键的就是：“collection”和“ofType”

### 3.4 多表映射总结

#### 3.4.1 多表映射优化

| setting属性         | 属性含义                                                     | 可选值              | 默认值  |
| ------------------- | ------------------------------------------------------------ | ------------------- | ------- |
| autoMappingBehavior | 指定 MyBatis 应如何自动映射列到字段或属性。 NONE 表示关闭自动映射；PARTIAL 只会自动映射没有定义嵌套结果映射的字段。 FULL 会自动映射任何复杂的结果集（无论是否嵌套）。 | NONE, PARTIAL, FULL | PARTIAL |

将 `autoMappingBehavior` 设为 `FULL` 后，多表 `resultMap` 中符合命名映射规则的字段可省略 `<result>` 标签。

```xml
<!--开启resultMap自动映射 -->
<setting name="autoMappingBehavior" value="FULL"/>
```

修改teacherMapper.xml

```xml
<resultMap id="teacherMap" type="teacher">
    <id property="tId" column="t_id" />
    <!-- 开启自动映射,并且开启驼峰式支持!可以省略 result!-->
<!--        <result property="tName" column="t_name" />-->
    <collection property="students" ofType="student" >
        <id property="sId" column="s_id" />
<!--            <result property="sName" column="s_name" />-->
    </collection>
</resultMap>
```

#### 3.4.2 多表映射总结

| 关联关系 | 配置项关键词                              | 所在配置文件和具体位置            |
| -------- | ----------------------------------------- | --------------------------------- |
| 对一     | association标签/javaType属性/property属性 | Mapper配置文件中的resultMap标签内 |
| 对多     | collection标签/ofType属性/property属性    | Mapper配置文件中的resultMap标签内 |

## 四、MyBatis动态语句

### 4.1 动态语句需求和简介

动态 SQL 是 MyBatis 的核心特性之一，用于解决**按条件拼接 SQL** 的痛点（如过滤条件可选、字段部分更新等）。MyBatis 3 基于 OGNL 表达式提供了以下标签：`if`、`where`、`set`、`trim`、`choose/when/otherwise`、`foreach`、`sql`。

### 4.2 if和where标签

使用动态 SQL 最常见情景是根据条件包含 where  / if 子句的一部分。比如：

```xml
<!-- List<Employee> selectEmployeeByCondition(Employee employee); -->
<select id="selectEmployeeByCondition" resultType="employee">
    select emp_id,emp_name,emp_salary from t_emp
    <!-- where标签会自动去掉“标签体内前面多余的and/or” -->
    <where>
        <!-- 使用if标签，让我们可以有选择的加入SQL语句的片段。这个SQL语句片段是否要加入整个SQL语句，就看if标签判断的结果是否为true -->
        <!-- 在if标签的test属性中，可以访问实体类的属性，不可以访问数据库表的字段 -->
        <if test="empName != null">
            <!-- 在if标签内部，需要访问接口的参数时还是正常写#{} -->
            or emp_name=#{empName}
        </if>
        <if test="empSalary &gt; 2000">
            or emp_salary>#{empSalary}
        </if>
        <!--
         第一种情况：所有条件都满足 WHERE emp_name=? or emp_salary>?
         第二种情况：部分条件满足 WHERE emp_salary>?
         第三种情况：所有条件都不满足 没有where子句
         -->
    </where>
</select>
```

### 4.3 set标签

```xml
<!-- void updateEmployeeDynamic(Employee employee) -->
<update id="updateEmployeeDynamic">
    update t_emp
    <!-- set emp_name=#{empName},emp_salary=#{empSalary} -->
    <!-- 使用set标签动态管理set子句，并且动态去掉两端多余的逗号 -->
    <set>
        <if test="empName != null">
            emp_name=#{empName},
        </if>
        <if test="empSalary &lt; 3000">
            emp_salary=#{empSalary},
        </if>
    </set>
    where emp_id=#{empId}
    <!--
         第一种情况：所有条件都满足 SET emp_name=?, emp_salary=?
         第二种情况：部分条件满足 SET emp_salary=?
         第三种情况：所有条件都不满足 update t_emp where emp_id=?
            没有set子句的update语句会导致SQL语法错误
     -->
</update>
```

### 4.4 trim标签(了解)

使用trim标签控制条件部分两端是否包含某些字符

- prefix属性：指定要动态添加的前缀
- suffix属性：指定要动态添加的后缀
- prefixOverrides属性：指定要动态去掉的前缀，使用“|”分隔有可能的多个值
- suffixOverrides属性：指定要动态去掉的后缀，使用“|”分隔有可能的多个值

```xml
<!-- List<Employee> selectEmployeeByConditionByTrim(Employee employee) -->
<select id="selectEmployeeByConditionByTrim" resultType="com.atguigu.mybatis.entity.Employee">
    select emp_id,emp_name,emp_age,emp_salary,emp_gender
    from t_emp
    
    <!-- prefix属性指定要动态添加的前缀 -->
    <!-- suffix属性指定要动态添加的后缀 -->
    <!-- prefixOverrides属性指定要动态去掉的前缀，使用“|”分隔有可能的多个值 -->
    <!-- suffixOverrides属性指定要动态去掉的后缀，使用“|”分隔有可能的多个值 -->
    <!-- 当前例子用where标签实现更简洁，但是trim标签更灵活，可以用在任何有需要的地方 -->
    <trim prefix="where" suffixOverrides="and|or">
        <if test="empName != null">
            emp_name=#{empName} and
        </if>
        <if test="empSalary &gt; 3000">
            emp_salary>#{empSalary} and
        </if>
        <if test="empAge &lt;= 20">
            emp_age=#{empAge} or
        </if>
        <if test="empGender=='male'">
            emp_gender=#{empGender}
        </if>
    </trim>
</select>
```

### 4.5 choose/when/otherwise标签

在多个分支条件中，仅执行一个。

- 从上到下依次执行条件判断
- 遇到的第一个满足条件的分支会被采纳
- 被采纳分支后面的分支都将不被考虑
- 如果所有的when分支都不满足，那么就执行otherwise分支

```xml
<!-- List<Employee> selectEmployeeByConditionByChoose(Employee employee) -->
<select id="selectEmployeeByConditionByChoose" resultType="com.atguigu.mybatis.entity.Employee">
    select emp_id,emp_name,emp_salary from t_emp
    where
    <choose>
        <when test="empName != null">emp_name=#{empName}</when>
        <when test="empSalary &lt; 3000">emp_salary &lt; 3000</when>
        <otherwise>1=1</otherwise>
    </choose>
    
    <!--
     第一种情况：第一个when满足条件 where emp_name=?
     第二种情况：第二个when满足条件 where emp_salary < 3000
     第三种情况：两个when都不满足 where 1=1 执行了otherwise
     -->
</select>
```

### 4.6 foreach标签

**基本用法**

用批量插入举例

```xml
<!--
    collection属性：要遍历的集合
    item属性：遍历集合的过程中能得到每一个具体对象，在item属性中设置一个名字，将来通过这个名字引用遍历出来的对象
    separator属性：指定当foreach标签的标签体重复拼接字符串时，各个标签体字符串之间的分隔符
    open属性：指定整个循环把字符串拼好后，字符串整体的前面要添加的字符串
    close属性：指定整个循环把字符串拼好后，字符串整体的后面要添加的字符串
    index属性：这里起一个名字，便于后面引用
        遍历List集合，这里能够得到List集合的索引值
        遍历Map集合，这里能够得到Map集合的key
 -->
<foreach collection="empList" item="emp" separator="," open="values" index="myIndex">
    <!-- 在foreach标签内部如果需要引用遍历得到的具体的一个对象，需要使用item属性声明的名称 -->
    (#{emp.empName},#{myIndex},#{emp.empSalary},#{emp.empGender})
</foreach>
```

**批量更新时需要注意**

上面批量插入的例子本质上是一条SQL语句，而实现批量更新则需要多条SQL语句拼起来，用分号分开。也就是一次性发送多条SQL语句让数据库执行。此时需要在数据库连接信息的URL地址中设置：

```.properties
atguigu.dev.url=jdbc:mysql:///mybatis-example?allowMultiQueries=true
对应的foreach标签如下：XML
<!-- int updateEmployeeBatch(@Param("empList") List<Employee> empList) -->
<update id="updateEmployeeBatch">
    <foreach collection="empList" item="emp" separator=";">
        update t_emp set emp_name=#{emp.empName} where emp_id=#{emp.empId}
    </foreach>
</update>
**关于foreach标签的collection属性**

如果 `List` 参数未使用 `@Param` 指定名称，`collection` 默认可写 `collection` 或 `list`。

> ⚠️ **注意**：异常 `Parameter 'empList' not found. Available parameters are [arg0, collection, list]` 表明当前只能用默认参数名。建议统一使用 `@Param` 显式命名。

### 4.7 sql片段

**抽取重复 SQL 片段**：

```xml
<!-- 使用sql标签抽取重复出现的SQL片段 -->
<sql id="mySelectSql">
    select emp_id,emp_name,emp_age,emp_salary,emp_gender from t_emp
</sql>
```

**引用已抽取的 SQL 片段**：

```xml
<!-- 使用include标签引用声明的SQL片段 -->
<include refid="mySelectSql"/>
```
## 五、MyBatis高级扩展

### 5.1 Mapper批量映射优化

Mapper 文件多时逐个注册繁琐，可按包名批量注册：

```xml
<mappers>
    <package name="com.atguigu.mapper"/>
</mappers>
```

> ⚠️ **注意**：使用包扫描需满足以下条件：
> - Mapper 接口与 XML 文件**同名**（如 `EmployeeMapper.java` 对应 `EmployeeMapper.xml`）
> - XML 文件放在 Mapper 接口**同包**下（可在 `resources` 下创建相同包路径存放）

### 5.2 插件和分页插件PageHelper

#### 5.2.1 插件机制和PageHelper插件介绍

MyBatis 对插件进行了标准化的设计，并提供了一套可扩展的插件机制。插件可以在用于语句执行过程中进行拦截，并允许通过自定义处理程序来拦截和修改 SQL 语句、映射语句的结果等。

具体来说，MyBatis 的插件机制包括以下三个组件：

1. `Interceptor`（拦截器）：定义一个拦截方法 `intercept`，该方法在执行 SQL 语句、执行查询、查询结果的映射时会被调用。
2. `Invocation`（调用）：实际上是对被拦截的方法的封装，封装了 `Object target`、`Method method` 和 `Object[] args` 这三个字段。
3. `InterceptorChain`（拦截器链）：对所有的拦截器进行管理，包括将所有的 Interceptor 链接成一条链，并在执行 SQL 语句时按顺序调用。

插件的开发非常简单，只需要实现 Interceptor 接口，并使用注解 `@Intercepts` 来标注需要拦截的对象和方法，然后在 MyBatis 的配置文件中添加插件即可。

PageHelper 是 MyBatis 中比较著名的分页插件，它提供了多种分页方式（例如 MySQL 和 Oracle 分页方式），支持多种数据库，并且使用非常简单。下面就介绍一下 PageHelper 的使用方式。

https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md#如何配置数据库方言

#### 5.2.2 PageHelper插件使用
1. pom.xml引入依赖

```xml
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.1.11</version>
</dependency>

2. `mybatis-config.xml` 配置分页插件：

```xml
<plugins>
    <plugin interceptor="com.github.pagehelper.PageInterceptor">
        <property name="helperDialect" value="mysql"/>
    </plugin>
</plugins>
```

`helperDialect` 指定数据库类型（支持 MySQL、Oracle 等多种数据库）。

3. 在查询方法中使用分页：

```java
public void testTeacherRelationshipToMulti() {

    TeacherMapper teacherMapper = session.getMapper(TeacherMapper.class);

    PageHelper.startPage(1,2);
    // 查询Customer对象同时将关联的Order集合查询出来
    List<Teacher> allTeachers = teacherMapper.findAllTeachers();
//
    PageInfo<Teacher> pageInfo = new PageInfo<>(allTeachers);

    System.out.println("pageInfo = " + pageInfo);
    long total = pageInfo.getTotal(); // 获取总记录数
    System.out.println("total = " + total);
    int pages = pageInfo.getPages();  // 获取总页数
    System.out.println("pages = " + pages);
    int pageNum = pageInfo.getPageNum(); // 获取当前页码
    System.out.println("pageNum = " + pageNum);
    int pageSize = pageInfo.getPageSize(); // 获取每页显示记录数
    System.out.println("pageSize = " + pageSize);
    List<Teacher> teachers = pageInfo.getList(); //获取查询页的数据集合
    System.out.println("teachers = " + teachers);
    teachers.forEach(System.out::println);

}
```

### 5.3 逆向工程和MybatisX插件

#### 5.3.1 ORM思维介绍

ORM（Object-Relational Mapping）用于建立对象与关系数据库之间的映射，让我们以**面向对象方式**执行数据库操作。

| 对比维度 | 半自动 ORM（如 MyBatis） | 全自动 ORM（如 Hibernate/JPA） |
| --- | --- | --- |
| 映射方式 | 手动配置映射（XML/注解） | 自动映射为主 |
| 查询方式 | 常需手写 SQL | 框架自动生成 SQL |
| 性能可控性 | 高（可精细优化 SQL） | 中（依赖框架生成策略） |
| 学习侧重 | SQL 与数据库基础 | 框架 API 与领域模型 |

常见的半自动 ORM 框架包括 MyBatis 等；常见的全自动 ORM 框架包括 Hibernate、Spring Data JPA、MyBatis-Plus 等。

#### 5.3.2 逆向工程

MyBatis 逆向工程可根据表结构自动生成实体类、`Mapper` 接口和 `Mapper.xml`，用于快速搭建单表 CRUD。常见方式包括 **MyBatis Generator** 与 **Maven 插件**。

**注意：逆向工程只能生成单表crud的操作，多表查询依然需要我们自己编写！**

#### 5.3.3 逆向工程插件MyBatisX使用

MyBatisX 是 MyBatis 代码生成插件，可快速生成 `Mapper`、`POJO` 和 `Mapper.xml`。常用步骤：

1. 在 IDEA 插件市场安装 `MyBatisX`
2. 连接数据库并选中目标表
3. 执行代码生成并检查产物
4. 在项目中直接使用生成的单表 CRUD 方法

```java
package com.atguigu.mapper;

import com.atguigu.pojo.User;

/**
* @author Jackiechan
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-06-02 16:55:32
* @Entity com.atguigu.pojo.User
*/
public interface UserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

}

```

## 六、MyBatis总结

| 核心点 | 掌握目标 |
| --- | --- |
| `mybatis基础` | 使用流程、参数输入、`#{}`/`${}`、参数输出 |
| `mybatis多表` | 实体类设计、`resultMap` 多表结果映射 |
| `mybatis动态语句` | 动态语句概念、`where`、`if`、`foreach` 标签 |
| `mybatis扩展` | Mapper 批量注册、分页插件、逆向工程 |

# 一、MyBatis-Plus快速入门

### 1.1 简介

> 课程版本：3.5.3.1

https://baomidou.com/

[MyBatis-Plus](https://github.com/baomidou/mybatis-plus) [ (opens new window)](https://github.com/baomidou/mybatis-plus)（简称 MP）是一个 [MyBatis](https://www.mybatis.org/mybatis-3/) [ (opens new window)](https://www.mybatis.org/mybatis-3/) 的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

特性：

- **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑
- **损耗小**：启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作
- **强大的 CRUD 操作**：内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求
- **支持 Lambda 形式调用**：通过 Lambda 表达式，方便的编写各类查询条件，无需再担心字段写错
- **支持主键自动生成**：支持多达 4 种主键策略（内含分布式唯一 ID 生成器 - Sequence），可自由配置，完美解决主键问题
- **支持 ActiveRecord 模式**：支持 ActiveRecord 形式调用，实体类只需继承 Model 类即可进行强大的 CRUD 操作
- **支持自定义全局通用操作**：支持全局通用方法注入（ Write once, use anywhere ）
- **内置代码生成器**：采用代码或者 Maven 插件可快速生成 Mapper 、 Model 、 Service 、 Controller 层代码，支持模板引擎，更有超多自定义配置等您来使用
- **内置分页插件**：基于 MyBatis 物理分页，开发者无需关心具体操作，配置好插件之后，写分页等同于普通 List 查询
- **分页插件支持多种数据库**：支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库
- **内置性能分析插件**：可输出 SQL 语句以及其执行时间，建议开发测试时启用该功能，能快速揪出慢查询
- **内置全局拦截插件**：提供全表 delete 、 update 操作智能分析阻断，也可自定义拦截规则，预防误操作

支持数据库：

- MySQL，Oracle，DB2，H2，HSQL，SQLite，PostgreSQL，SQLServer，Phoenix，Gauss ，ClickHouse，Sybase，OceanBase，Firebird，Cubrid，Goldilocks，csiidb，informix，TDengine，redshift
- 达梦数据库，虚谷数据库，人大金仓数据库，南大通用(华库)数据库，南大通用数据库，神通数据库，瀚高数据库，优炫数据库

mybatis-plus总结：

自动生成单表的CRUD功能

提供丰富的条件拼接方式

全自动ORM类型持久层框架

### 1.2 快速入门
1. 准备数据库脚本

现有一张 `User` 表，其表结构如下：

| id   | name   | age  | email                                           |
| ---- | ------ | ---- | ----------------------------------------------- |
| 1    | Jone   | 18   | [test1@baomidou.com](mailto:test1@baomidou.com) |
| 2    | Jack   | 20   | [test2@baomidou.com](mailto:test2@baomidou.com) |
| 3    | Tom    | 28   | [test3@baomidou.com](mailto:test3@baomidou.com) |
| 4    | Sandy  | 21   | [test4@baomidou.com](mailto:test4@baomidou.com) |
| 5    | Billie | 24   | [test5@baomidou.com](mailto:test5@baomidou.com) |

```sql
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
);

INSERT INTO user (id, name, age, email) VALUES
(1, 'Jone', 18, 'test1@baomidou.com'),
(2, 'Jack', 20, 'test2@baomidou.com'),
(3, 'Tom', 28, 'test3@baomidou.com'),
(4, 'Sandy', 21, 'test4@baomidou.com'),
(5, 'Billie', 24, 'test5@baomidou.com');

2. 准备boot工程
3. 导入依赖XML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.0.5</version>
    </parent>
    <groupId>com.atguigu</groupId>
    <artifactId>springboot-starter-mybatis-plus-06</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        
        <!-- 测试环境 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
        
        <!-- mybatis-plus  -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.3.1</version>
        </dependency>

        <!-- 数据库相关配置启动器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>

        <!-- druid启动器的依赖  -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-3-starter</artifactId>
            <version>1.2.18</version>
        </dependency>

        <!-- 驱动类-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.28</version>
        </dependency>

    </dependencies>

    <!--    SpringBoot应用打包插件-->
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
4. 配置文件和启动类

    完善连接池配置：

    文件夹：META-INF.spring

    文件名：

    org.springframework.boot.autoconfigure.AutoConfiguration.imports

    内容：com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure

    application.yamlYAML
# 连接池配置
spring:
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
      url: jdbc:mysql:///day01
      username: root
      password: root
      driver-class-name: com.mysql.cj.jdbc.Driver

启动类Java
@MapperScan("com.atguigu.mapper")
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class,args);
    }
    
}

```
5. 功能编码

编写实体类 `User.java`（此处使用了 [Lombok](https://www.projectlombok.org/) [ ](https://www.projectlombok.org/)简化代码）

```java
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}

编写 Mapper 包下的 `UserMapper`接口Java
public interface UserMapper extends BaseMapper<User> {

}

    继承mybatis-plus提供的基础Mapper接口，自带crud方法！
6. 测试和使用

    添加测试类，进行功能测试：Java
@SpringBootTest //springboot下测试环境注解
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }
}

```
7. 小结

通过以上几个简单的步骤，我们就实现了 User 表的 CRUD 功能，甚至连 XML 文件都不用编写！

从以上步骤中，我们可以看到集成`MyBatis-Plus`非常的简单，只需要引入 starter 工程，并配置 mapper 扫描路径即可。

# 二、MyBatis-Plus核心功能

### 2.1 基于Mapper接口CRUD

> 通用 CRUD 封装[BaseMapper (opens new window)](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-core/src/main/java/com/baomidou/mybatisplus/core/mapper/BaseMapper.java)接口， `Mybatis-Plus` 启动时自动解析实体表关系映射转换为 `Mybatis` 内部对象注入容器! 内部包含常见的单表操作！

#### Insert方法

```java
// 插入一条记录
// T 就是要插入的实体对象
// 默认主键生成策略为雪花算法（后面讲解）
int insert(T entity);
| 类型 | 参数名 | 描述     |
| ---- | ------ | -------- |
| T    | entity | 实体对象 |

#### Delete方法Java
// 根据 entity 条件，删除记录
int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

// 删除（根据ID 批量删除）
int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

// 根据 ID 删除
int deleteById(Serializable id);

// 根据 columnMap 条件，删除记录
int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);
```

| 类型                               | 参数名    | 描述                                 |
| ---------------------------------- | --------- | ------------------------------------ |
| Wrapper<T>                         | wrapper   | 实体对象封装操作类（可以为 null）    |
| Collection<? extends Serializable> | idList    | 主键 ID 列表(不能为 null 以及 empty) |
| Serializable                       | id        | 主键 ID                              |
| Map<String, Object>                | columnMap | 表字段 map 对象                      |

#### Update方法

```java
// 根据 whereWrapper 条件，更新记录
int update(@Param(Constants.ENTITY) T updateEntity, 
            @Param(Constants.WRAPPER) Wrapper<T> whereWrapper);

// 根据 ID 修改  主键属性必须值
int updateById(@Param(Constants.ENTITY) T entity);
```

| 类型       | 参数名        | 描述                                                         |
| ---------- | ------------- | ------------------------------------------------------------ |
| T          | entity        | 实体对象 (set 条件值,可为 null)                              |
| Wrapper<T> | updateWrapper | 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句） |

#### Select方法

```java
// 根据 ID 查询
T selectById(Serializable id);

// 根据 entity 条件，查询一条记录
T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

// 查询（根据ID 批量查询）
List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

// 根据 entity 条件，查询全部记录
List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

// 查询（根据 columnMap 条件）
List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

// 根据 Wrapper 条件，查询全部记录
List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

// 根据 Wrapper 条件，查询全部记录。注意： 只返回第一个字段的值
List<Object> selectObjs(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

// 根据 entity 条件，查询全部记录（并翻页）
IPage<T> selectPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

// 根据 Wrapper 条件，查询全部记录（并翻页）
IPage<Map<String, Object>> selectMapsPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

// 根据 Wrapper 条件，查询总记录数
Integer selectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
```

参数说明

| 类型                               | 参数名       | 描述                                     |
| ---------------------------------- | ------------ | ---------------------------------------- |
| Serializable                       | id           | 主键 ID                                  |
| Wrapper<T>                         | queryWrapper | 实体对象封装操作类（可以为 null）        |
| Collection<? extends Serializable> | idList       | 主键 ID 列表(不能为 null 以及 empty)     |
| Map<String, Object>                | columnMap    | 表字段 map 对象                          |
| IPage<T>                           | page         | 分页查询条件（可以为 RowBounds.DEFAULT） |

#### 自定义和多表映射

mybatis-plus的默认mapperxml位置

```YAML
mybatis-plus: # mybatis-plus的配置
# 默认位置 private String[] mapperLocations = new String[]{"classpath*:/mapper/**/*.xml"};    
  mapper-locations: classpath:/mapper/*.xml
自定义mapper方法：Java
public interface UserMapper extends BaseMapper<User> {

    //正常自定义方法！
    //可以使用注解@Select或者mapper.xml实现
    List<User> queryAll();
}

基于mapper.xml实现：XML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace = 接口的全限定符 -->
<mapper namespace="com.atguigu.mapper.UserMapper">

   <select id="queryAll" resultType="user" >
       select * from user
   </select>
</mapper>
```

### 2.2 基于Service接口CRUD

通用 Service CRUD 封装[IService (opens new window)](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-extension/src/main/java/com/baomidou/mybatisplus/extension/service/IService.java)接口，进一步封装 CRUD 采用 `get 查询单行` `remove 删除` `list 查询集合` `page 分页` 前缀命名方式区分 `Mapper` 层避免混淆，

#### 对比Mapper接口CRUD区别：

- service添加了批量方法
- service层的方法自动添加事务

#### 使用Iservice接口方式

接口继承IService接口

```java
public interface UserService extends IService<User> {
}

类继承ServiceImpl实现类Java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{

}

#### CRUD方法介绍Java
保存：
// 插入一条记录（选择字段，策略插入）
boolean save(T entity);
// 插入（批量）
boolean saveBatch(Collection<T> entityList);
// 插入（批量）
boolean saveBatch(Collection<T> entityList, int batchSize);

修改或者保存：
// TableId 注解存在更新记录，否插入一条记录
boolean saveOrUpdate(T entity);
// 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
boolean saveOrUpdate(T entity, Wrapper<T> updateWrapper);
// 批量修改插入
boolean saveOrUpdateBatch(Collection<T> entityList);
// 批量修改插入
boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize);

移除：
// 根据 queryWrapper 设置的条件，删除记录
boolean remove(Wrapper<T> queryWrapper);
// 根据 ID 删除
boolean removeById(Serializable id);
// 根据 columnMap 条件，删除记录
boolean removeByMap(Map<String, Object> columnMap);
// 删除（根据ID 批量删除）
boolean removeByIds(Collection<? extends Serializable> idList);

更新：
// 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
boolean update(Wrapper<T> updateWrapper);
// 根据 whereWrapper 条件，更新记录
boolean update(T updateEntity, Wrapper<T> whereWrapper);
// 根据 ID 选择修改
boolean updateById(T entity);
// 根据ID 批量更新
boolean updateBatchById(Collection<T> entityList);
// 根据ID 批量更新
boolean updateBatchById(Collection<T> entityList, int batchSize);

数量： 
// 查询总记录数
int count();
// 根据 Wrapper 条件，查询总记录数
int count(Wrapper<T> queryWrapper);

查询：
// 根据 ID 查询
T getById(Serializable id);
// 根据 Wrapper，查询一条记录。结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
T getOne(Wrapper<T> queryWrapper);
// 根据 Wrapper，查询一条记录
T getOne(Wrapper<T> queryWrapper, boolean throwEx);
// 根据 Wrapper，查询一条记录
Map<String, Object> getMap(Wrapper<T> queryWrapper);
// 根据 Wrapper，查询一条记录
<V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);

集合：
// 查询所有
List<T> list();
// 查询列表
List<T> list(Wrapper<T> queryWrapper);
// 查询（根据ID 批量查询）
Collection<T> listByIds(Collection<? extends Serializable> idList);
// 查询（根据 columnMap 条件）
Collection<T> listByMap(Map<String, Object> columnMap);
// 查询所有列表
List<Map<String, Object>> listMaps();
// 查询列表
List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);
// 查询全部记录
List<Object> listObjs();
// 查询全部记录
<V> List<V> listObjs(Function<? super Object, V> mapper);
// 根据 Wrapper 条件，查询全部记录
List<Object> listObjs(Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询全部记录
<V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);

### 2.3 分页查询实现
  1. 导入分页插件Java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
}
2. 使用分页查询Java
@Test
public void testPageQuery(){
    //设置分页参数
    Page<User> page = new Page<>(1, 5);
    userMapper.selectPage(page, null);
    //获取分页数据
    List<User> list = page.getRecords();
    list.forEach(System.out::println);
    System.out.println("当前页："+page.getCurrent());
    System.out.println("每页显示的条数："+page.getSize());
    System.out.println("总记录数："+page.getTotal());
    System.out.println("总页数："+page.getPages());
    System.out.println("是否有上一页："+page.hasPrevious());
    System.out.println("是否有下一页："+page.hasNext());
}
3. 自定义的mapper方法使用分页

    方法Java
//传入参数携带Ipage接口
//返回结果为IPage
IPage<User> selectPageVo(IPage<?> page, Integer id);

接口实现Java
<select id="selectPageVo" resultType="xxx.xxx.xxx.User">
    SELECT * FROM user WHERE id > #{id}
</select>
测试Java
@Test
public void testQuick(){

    IPage page = new Page(1,2);

    userMapper.selectPageVo(page,2);

    long current = page.getCurrent();
    System.out.println("current = " + current);
    long pages = page.getPages();
    System.out.println("pages = " + pages);
    long total = page.getTotal();
    System.out.println("total = " + total);
    List records = page.getRecords();
    System.out.println("records = " + records);

}
### 2.4 条件构造器使用

#### 2.4.1 条件构造器作用
  
    实例代码：Java
QueryWrapper<User> queryWrapper = new QueryWrapper<>();
queryWrapper.eq("name", "John"); // 添加等于条件
queryWrapper.ne("age", 30); // 添加不等于条件
queryWrapper.like("email", "@gmail.com"); // 添加模糊匹配条件
等同于： 
delete from user where name = "John" and age != 30
                                  and email like "%@gmail.com%"
// 根据 entity 条件，删除记录
int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);
```

使用MyBatis-Plus的条件构造器，你可以构建灵活、高效的查询条件，而不需要手动编写复杂的 SQL 语句。它提供了许多方法来支持各种条件操作符，并且可以通过链式调用来组合多个条件。这样可以简化查询的编写过程，并提高开发效率。

#### 2.4.2 条件构造器继承结构

条件构造器类结构：

Wrapper ： 条件构造抽象类，最顶端父类

- AbstractWrapper ： 用于查询条件封装，生成 sql 的 where 条件
- QueryWrapper ： 查询/删除条件封装
- UpdateWrapper ： 修改条件封装
- AbstractLambdaWrapper ： 使用Lambda 语法
- LambdaQueryWrapper ：用于Lambda语法使用的查询Wrapper
- LambdaUpdateWrapper ： Lambda 更新封装Wrapper

#### 2.4.3 基于QueryWrapper 组装条件

组装查询条件：

```java
@Test
public void test01(){
    //查询用户名包含a，年龄在20到30之间，并且邮箱不为null的用户信息
    //SELECT id,username AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 AND (username LIKE ? AND age BETWEEN ? AND ? AND email IS NOT NULL)
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.like("username", "a")
            .between("age", 20, 30)
            .isNotNull("email");
    List<User> list = userMapper.selectList(queryWrapper);
    list.forEach(System.out::println);

组装排序条件:Java
@Test
public void test02(){
    //按年龄降序查询用户，如果年龄相同则按id升序排列
    //SELECT id,username AS name,age,email,is_deleted FROM t_user WHERE is_deleted=0 ORDER BY age DESC,id ASC
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper
            .orderByDesc("age")
            .orderByAsc("id");
    List<User> users = userMapper.selectList(queryWrapper);
    users.forEach(System.out::println);
}
组装删除条件:Java
@Test
public void test03(){
    //删除email为空的用户
    //DELETE FROM t_user WHERE (email IS NULL)
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.isNull("email");
    //条件构造器也可以构建删除语句的条件
    int result = userMapper.delete(queryWrapper);
    System.out.println("受影响的行数：" + result);
}
and和or关键字使用(修改)：Java
@Test
public void test04() {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    //将年龄大于20并且用户名中包含有a或邮箱为null的用户信息修改
    //UPDATE t_user SET age=?, email=? WHERE username LIKE ? AND age > ? OR email IS NULL)
    queryWrapper
            .like("username", "a")
            .gt("age", 20)
            .or()
            .isNull("email");
    User user = new User();
    user.setAge(18);
    user.setEmail("user@atguigu.com");
    int result = userMapper.update(user, queryWrapper);
    System.out.println("受影响的行数：" + result);
}
指定列映射查询：Java
@Test
public void test05() {
    //查询用户信息的username和age字段
    //SELECT username,age FROM t_user
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.select("username", "age");
    //selectMaps()返回Map集合列表，通常配合select()使用，避免User对象中没有被查询到的列值为null
    List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
    maps.forEach(System.out::println);
}
condition判断组织条件:Java
 @Test
public void testQuick3(){
    
    String name = "root";
    int    age = 18;

    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    //判断条件拼接
    //当name不为null拼接等于, age > 1 拼接等于判断
    //方案1: 手动判断
    if (!StringUtils.isEmpty(name)){
        queryWrapper.eq("name",name);
    }
    if (age > 1){
        queryWrapper.eq("age",age);
    }
    
    //方案2: 拼接condition判断
    //每个条件拼接方法都condition参数,这是一个比较运算,为true追加当前条件!
    //eq(condition,列名,值)
    queryWrapper.eq(!StringUtils.isEmpty(name),"name",name)
            .eq(age>1,"age",age);   
}
#### 2.3.4 基于 UpdateWrapper组装条件

  使用queryWrapper:Java
@Test
public void test04() {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    //将年龄大于20并且用户名中包含有a或邮箱为null的用户信息修改
    //UPDATE t_user SET age=?, email=? WHERE username LIKE ? AND age > ? OR email IS NULL)
    queryWrapper
            .like("username", "a")
            .gt("age", 20)
            .or()
            .isNull("email");
    User user = new User();
    user.setAge(18);
    user.setEmail("user@atguigu.com");
    int result = userMapper.update(user, queryWrapper);
    System.out.println("受影响的行数：" + result);
}
注意：使用queryWrapper + 实体类形式可以实现修改，但是无法将列值修改为null值！
    
使用updateWrapper:Java
@Test
public void testQuick2(){

    UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
    //将id = 3 的email设置为null, age = 18
    updateWrapper.eq("id",3)
            .set("email",null)  // set 指定列和结果
            .set("age",18);
    //如果使用updateWrapper 实体对象写null即可!
    int result = userMapper.update(null, updateWrapper);
    System.out.println("result = " + result);

}
      使用updateWrapper可以随意设置列的值！！

​      

#### 2.3.5 基于LambdaQueryWrapper组装条件
    
      1. **LambdaQueryWrapper对比QueryWrapper优势**
    
      QueryWrapper 示例代码：Java
QueryWrapper<User> queryWrapper = new QueryWrapper<>();
queryWrapper.eq("name", "John")
  .ge("age", 18)
  .orderByDesc("create_time")
  .last("limit 10");
List<User> userList = userMapper.selectList(queryWrapper);
LambdaQueryWrapper 示例代码：Java
LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

lambdaQueryWrapper.eq(User::getName, "John")
  .ge(User::getAge, 18)
  .orderByDesc(User::getCreateTime)
  .last("limit 10");
List<User> userList = userMapper.selectList(lambdaQueryWrapper);
```

从上面的代码对比可以看出，相比于 QueryWrapper，LambdaQueryWrapper 使用了实体类的属性引用（例如 `User::getName`、`User::getAge`），而不是字符串来表示字段名，这提高了代码的可读性和可维护性。

​      

2. **lambda表达式回顾**

Lambda 表达式是 Java 8 引入的一种函数式编程特性，它提供了一种更简洁、更直观的方式来表示匿名函数或函数式接口的实现。Lambda 表达式可以用于简化代码，提高代码的可读性和可维护性。

Lambda 表达式的语法可以分为以下几个部分：

1. **参数列表：** 参数列表用小括号 `()` 括起来，可以指定零个或多个参数。如果没有参数，可以省略小括号；如果只有一个参数，可以省略小括号。

示例：`(a, b)`, `x ->`, `() ->`
2. **箭头符号：** 箭头符号 `->` 分割参数列表和 Lambda 表达式的主体部分。

示例：`->`
3. **Lambda 表达式的主体：** Lambda 表达式的主体部分可以是一个表达式或一个代码块。如果是一个表达式，可以省略 return 关键字；如果是多条语句的代码块，需要使用大括号 `{}` 括起来，并且需要明确指定 return 关键字。

示例：

- 单个表达式：`x -> x * x`
- 代码块：`(x, y) -> { int sum = x + y; return sum; }`

Lambda 表达式的语法可以更具体地描述如下：

```java
// 使用 Lambda 表达式实现一个接口的方法
interface Greeting {
    void sayHello();
}

public class LambdaExample {
    public static void main(String[] args) {
    
        //原始匿名内部类方式
        Greeting greeting = new Greeting() {
            @Override
            public void sayHello(int a) {
                System.out.println("Hello, world!");
            }
        };
        
        a->System.out.println("Hello, world!")
        
        // 使用 Lambda 表达式实现接口的方法
        greeting = () -> System.out.println("Hello, world!");

          System.out::println;
           () ->  类.XXX(); -> 类：：方法名
        // 调用接口的方法
        greeting.sayHello();
    }
}
```

**3. 方法引用回顾:**

方法引用是 Java 8 中引入的一种语法特性，它提供了一种简洁的方式来直接引用已有的方法或构造函数。方法引用可以替代 Lambda 表达式，使代码更简洁、更易读。

Java 8 支持以下几种方法引用的形式：

1. **静态方法引用：** 引用静态方法，语法为 `类名::静态方法名`。
2. **实例方法引用：** 引用实例方法，语法为 `实例对象::实例方法名`。
3. **对象方法引用：** 引用特定对象的实例方法，语法为 `类名::实例方法名`。
4. **构造函数引用：** 引用构造函数，语法为 `类名::new`。

演示代码:

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class MethodReferenceExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("John", "Tom", "Alice");
        // 使用 Lambda 表达式
        names.forEach(name -> System.out.println(name));
        // 使用方法引用
        names.forEach(System.out::println);
    }
}
**4. lambdaQueryWrapper使用案例:**Java
@Test
public void testQuick4(){

    String name = "root";
    int    age = 18;

    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    //每个条件拼接方法都condition参数,这是一个比较运算,为true追加当前条件!
    //eq(condition,列名,值)
    queryWrapper.eq(!StringUtils.isEmpty(name),"name",name)
            .eq(age>1,"age",age);

    //TODO: 使用lambdaQueryWrapper
    LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    //注意: 需要使用方法引用
    //技巧: 类名::方法名
    lambdaQueryWrapper.eq(!StringUtils.isEmpty(name), User::getName,name);
    List<User> users= userMapper.selectList(lambdaQueryWrapper);
    System.out.println(users);
}
#### 2.3.6 基于LambdaUpdateWrapper组装条件

  使用案例:Java
@Test
public void testQuick2(){

    UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
    //将id = 3 的email设置为null, age = 18
    updateWrapper.eq("id",3)
            .set("email",null)  // set 指定列和结果
            .set("age",18);

    //使用lambdaUpdateWrapper
    LambdaUpdateWrapper<User> updateWrapper1 = new LambdaUpdateWrapper<>();
    updateWrapper1.eq(User::getId,3)
            .set(User::getEmail,null)
            .set(User::getAge,18);
    
    //如果使用updateWrapper 实体对象写null即可!
    int result = userMapper.update(null, updateWrapper);
    System.out.println("result = " + result);
}
### 2.5 核心注解使用
  1. 理解和介绍
  
      MyBatis-Plus是一个基于MyBatis框架的增强工具，提供了一系列简化和增强的功能，用于加快开发人员在使用MyBatis进行数据库访问时的效率。
  
      MyBatis-Plus提供了一种基于注解的方式来定义和映射数据库操作，其中的注解起到了重要作用。
  
      理解：Java
public interface UserMapper extends BaseMapper<User> {

}
    此接口对应的方法为什么会自动触发 user表的crud呢？

    默认情况下， 根据指定的<实体类>的名称对应数据库表名，属性名对应数据库的列名！

    但是不是所有数据库的信息和实体类都完全映射！

    例如： 表名 t_user  → 实体类 User 这时候就不对应了！

    自定义映射关系就可以使用mybatis-plus提供的注解即可！
2. @TableName注解
    - 描述：表名注解，标识实体类对应的表
    - 使用位置：实体类Java
@TableName("sys_user") //对应数据库表名
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}

```

特殊情况：如果表名和实体类名相同（忽略大小写）可以省略该注解！

其他解决方案：全局设置前缀 ([https://www.baomidou.com/pages/56bac0/#基本配置](https://www.baomidou.com/pages/56bac0/#基本配置))

```YAML
mybatis-plus: # mybatis-plus的配置
  global-config:
    db-config:
      table-prefix: sys_ # 表名前缀字符串
3. @TableId 注解
    - 描述：主键注解
    - 使用位置：实体类主键字段Java
@TableName("sys_user")
public class User {
    @TableId(value="主键列名",type=主键策略)
    private Long id;
    private String name;
    private Integer age;
    private String email;
}

```

| 属性  | 类型   | 必须指定 | 默认值      | 描述         |
| ----- | ------ | -------- | ----------- | ------------ |
| value | String | 否       | ""          | 主键字段名   |
| type  | Enum   | 否       | IdType.NONE | 指定主键类型 |

[IdType](https://github.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-annotation/src/main/java/com/baomidou/mybatisplus/annotation/IdType.java)属性可选值：

| 值                | 描述                                                         |
| ----------------- | ------------------------------------------------------------ |
| AUTO              | 数据库 ID 自增 (mysql配置主键自增长)                         |
| ASSIGN_ID（默认） | 分配 ID(主键类型为 Number(Long )或 String)(since 3.3.0),使用接口`IdentifierGenerator`的方法`nextId`(默认实现类为`DefaultIdentifierGenerator`雪花算法) |

全局配置修改主键策略:

```java
mybatis-plus:
  configuration:
# 配置MyBatis日志
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
# 配置MyBatis-Plus操作表的默认前缀
      table-prefix: t_
# 配置MyBatis-Plus的主键策略
      id-type: auto
    在以下场景下，添加`@TableId`注解是必要的：

    1. 实体类的字段与数据库表的主键字段不同名：如果实体类中的字段与数据库表的主键字段不一致，需要使用`@TableId`注解来指定实体类中表示主键的字段。
    2. 主键生成策略不是默认策略：如果需要使用除了默认主键生成策略以外的策略，也需要添加`@TableId`注解，并通过`value`属性指定生成策略。
4. 雪花算法使用场景

    雪花算法（Snowflake Algorithm）是一种用于生成唯一ID的算法。它由Twitter公司提出，用于解决分布式系统中生成全局唯一ID的需求。

    在传统的自增ID生成方式中，使用单点数据库生成ID会成为系统的瓶颈，而雪花算法通过在分布式系统中生成唯一ID，避免了单点故障和性能瓶颈的问题。

    雪花算法生成的ID是一个64位的整数，由以下几个部分组成：

    1. 时间戳：41位，精确到毫秒级，可以使用69年。
    2. 节点ID：10位，用于标识分布式系统中的不同节点。
    3. 序列号：12位，表示在同一毫秒内生成的不同ID的序号。

    通过将这三个部分组合在一起，雪花算法可以在分布式系统中生成全局唯一的ID，并保证ID的生成顺序性。

    雪花算法的工作方式如下：

    1. 当前时间戳从某一固定的起始时间开始计算，可以用于计算ID的时间部分。
    2. 节点ID是分布式系统中每个节点的唯一标识，可以通过配置或自动分配的方式获得。
    3. 序列号用于记录在同一毫秒内生成的不同ID的序号，从0开始自增，最多支持4096个ID生成。

    需要注意的是，雪花算法依赖于系统的时钟，需要确保系统时钟的准确性和单调性，否则可能会导致生成的ID不唯一或不符合预期的顺序。

    雪花算法是一种简单但有效的生成唯一ID的算法，广泛应用于分布式系统中，如微服务架构、分布式数据库、分布式锁等场景，以满足全局唯一标识的需求。

    **你需要记住的: 雪花算法生成的数字,需要使用Long 或者 String类型主键!!**
5. @TableField

    描述：字段注解（非主键）Java
@TableName("sys_user")
public class User {
    @TableId
    private Long id;
    @TableField("nickname")
    private String name;
    private Integer age;
    private String email;
}
        属性	        类型	    必须指定	默认值	描述
value	String	否	          ""	        数据库字段名
exist	        boolean	否	         true	        是否为数据库表字段

        **MyBatis-Plus会自动开启驼峰命名风格映射!!!**

# 三、MyBatis-Plus高级扩展

### 3.1 逻辑删除实现

    **概念:**
    
    逻辑删除，可以方便地实现对数据库记录的逻辑删除而不是物理删除。逻辑删除是指通过更改记录的状态或添加标记字段来模拟删除操作，从而保留了删除前的数据，便于后续的数据分析和恢复。
    
    - 物理删除：真实删除，将对应数据从数据库中删除，之后查询不到此条被删除的数据
    - 逻辑删除：假删除，将对应数据中代表是否被删除字段的状态修改为“被删除状态”，之后在数据库中仍旧能看到此条数据记录
    
    **逻辑删除实现:**
    
    1. 数据库和实体类添加逻辑删除字段
        1. 表添加逻辑删除字段
    
            可以是一个布尔类型、整数类型或枚举类型。SQL
ALTER TABLE USER ADD deleted INT DEFAULT 0 ;  # int 类型 1 逻辑删除 0 未逻辑删除
2. 实体类添加逻辑删除属性SQL
@Data
public class User {

   // @TableId
    private Integer id;
    private String name;
    private Integer age;
    private String email;
    
    @TableLogic
    //逻辑删除字段 int mybatis-plus下,默认 逻辑删除值为1 未逻辑删除 1 
    private Integer deleted;
}

2. 指定逻辑删除字段和属性值
    1. 单一指定SQL
@Data
public class User {

   // @TableId
    private Integer id;
    private String name;
    private Integer age;
    private String email;
     @TableLogic
    //逻辑删除字段 int mybatis-plus下,默认 逻辑删除值为1 未逻辑删除 1 
    private Integer deleted;
}
2. 全局指定YAML
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: deleted # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
3. 演示逻辑删除操作

    > 逻辑删除以后,没有真正的删除语句,删除改为修改语句!

    删除代码:Java
//逻辑删除
@Test
public void testQuick5(){
    //逻辑删除
    userMapper.deleteById(5);
}
```

执行效果:

JDBC Connection [com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl@5871a482] will not be managed by Spring
==> Preparing: UPDATE user SET deleted=1 WHERE id=? AND deleted=0
==> Parameters: 5(Integer)
<==    Updates: 1
4. 测试查询数据

```java
@Test
public void testQuick6(){
    //正常查询.默认查询非逻辑删除数据
    userMapper.selectList(null);
}

//SELECT id,name,age,email,deleted FROM user WHERE deleted=0
```

### 3.2 乐观锁实现

#### 3.2.1 悲观锁和乐观锁场景和介绍

**并发问题场景演示:**

​    

**解决思路: **

乐观锁和悲观锁是在并发编程中用于处理并发访问和资源竞争的两种不同的锁机制!!

悲观锁：  
悲观锁的基本思想是，在整个数据访问过程中，将共享资源锁定，以确保其他线程或进程不能同时访问和修改该资源。悲观锁的核心思想是"先保护，再修改"。在悲观锁的应用中，线程在访问共享资源之前会获取到锁，并在整个操作过程中保持锁的状态，阻塞其他线程的访问。只有当前线程完成操作后，才会释放锁，让其他线程继续操作资源。这种锁机制可以确保资源独占性和数据的一致性，但是在高并发环境下，悲观锁的效率相对较低。

乐观锁：  
乐观锁的基本思想是，认为并发冲突的概率较低，因此不需要提前加锁，而是在数据更新阶段进行冲突检测和处理。乐观锁的核心思想是"先修改，后校验"。在乐观锁的应用中，线程在读取共享资源时不会加锁，而是记录特定的版本信息。当线程准备更新资源时，会先检查该资源的版本信息是否与之前读取的版本信息一致，如果一致则执行更新操作，否则说明有其他线程修改了该资源，需要进行相应的冲突处理。乐观锁通过避免加锁操作，提高了系统的并发性能和吞吐量，但是在并发冲突较为频繁的情况下，乐观锁会导致较多的冲突处理和重试操作。

理解点: 悲观锁和乐观锁是两种解决并发数据问题的思路,不是具体技术!!!

​    

**具体技术和方案:**

1. 乐观锁实现方案和技术：
- 版本号/时间戳：为数据添加一个版本号或时间戳字段，每次更新数据时，比较当前版本号或时间戳与期望值是否一致，若一致则更新成功，否则表示数据已被修改，需要进行冲突处理。
- CAS（Compare-and-Swap）：使用原子操作比较当前值与旧值是否一致，若一致则进行更新操作，否则重新尝试。
- 无锁数据结构：采用无锁数据结构，如无锁队列、无锁哈希表等，通过使用原子操作实现并发安全。
2. 悲观锁实现方案和技术：
- 锁机制：使用传统的锁机制，如互斥锁（Mutex Lock）或读写锁（Read-Write Lock）来保证对共享资源的独占访问。
- 数据库锁：在数据库层面使用行级锁或表级锁来控制并发访问。
- 信号量（Semaphore）：使用信号量来限制对资源的并发访问。

​    

**介绍版本号乐观锁技术的实现流程:**

- 每条数据添加一个版本号字段version
- 取出记录时，获取当前 version
- 更新时，检查获取版本号是不是数据库当前最新版本号
- 如果是[证明没有人修改数据], 执行更新, set 数据更新 , version = version+ 1 
- 如果 version 不对[证明有人已经修改了]，我们现在的其他记录就是失效数据!就更新失败

#### 3.2.2 使用mybatis-plus数据使用乐观锁

1.  添加版本号更新插件

```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    return interceptor;
}
1. 乐观锁字段添加@Version注解

    注意: 数据库也需要添加version字段Java
ALTER TABLE USER ADD VERSION INT DEFAULT 1 ;  # int 类型 乐观锁字段
```

- 支持的数据类型只有:int,Integer,long,Long,Date,Timestamp,LocalDateTime
- 仅支持 `updateById(id)` 与 `update(entity, wrapper)` 方法

```java
@Version
private Integer version;
2. 正常更新使用即可Java
//演示乐观锁生效场景
@Test
public void testQuick7(){
    //步骤1: 先查询,在更新 获取version数据
    //同时查询两条,但是version唯一,最后更新的失败
    User user  = userMapper.selectById(5);
    User user1  = userMapper.selectById(5);

    user.setAge(20);
    user1.setAge(30);

    userMapper.updateById(user);
    //乐观锁生效,失败!
    userMapper.updateById(user1);
}
### 3.3 防全表更新和删除实现

  针对 update 和 delete 语句 作用: 阻止恶意的全表更新删除
  
  添加防止全表更新和删除拦截器Java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
  MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
  interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
  return interceptor;
}
}
测试全部更新或者删除Java
@Test
public void testQuick8(){
    User user = new User();
    user.setName("custom_name");
    user.setEmail("xxx@mail.com");
    //Caused by: com.baomidou.mybatisplus.core.exceptions.MybatisPlusException: Prohibition of table update operation
    //全局更新,报错
    userService.saveOrUpdate(user,null);
}
```

# 四、MyBatis-Plus代码生成器(MyBatisX插件)

### 4.1 Mybatisx插件逆向工程

MyBatis-Plus为我们提供了强大的mapper和service模板，能够大大的提高开发效率

但是在真正开发过程中，MyBatis-Plus并不能为我们解决所有问题，例如一些复杂的SQL，多表联查，我们就需要自己去编写代码和SQL语句，我们该如何快速的解决这个问题呢，这个时候可以使用MyBatisX插件

MyBatisX一款基于 IDEA 的快速开发插件，为效率而生。

### 4.2 MyBatisX快速代码生成

使用mybatisX插件,自动生成sql语句实现

https://baomidou.com/pages/ba5b24/#功能
