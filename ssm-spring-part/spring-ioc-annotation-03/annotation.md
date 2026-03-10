# Spring IoC 注解开发笔记

## 一、整体思路

| 注解标识（IoC / DI） | 对应 XML 配置 |
|----------------------|--------------|
| `@Component` / `@Controller` / `@Service` / `@Repository` | `<context:component-scan base-package=""/>` |
| `@Value("${key:defaultValue}")` | `<context:property-placeholder location="classpath:"/>` |
| 第三方类无法加注解时 | `<bean id="" class=""><property name="" value/ref=""/></bean>` |

---

## 二、IoC 注解 —— 组件注册

### 2.1 四大组件注解

| 注解 | 语义 | 适用层 | 示例 |
|------|------|--------|------|
| `@Component` | 通用组件 | 任意 | `CommonComponent` |
| `@Controller` | 控制器 | 表现层 | `XxxController` |
| `@Service` | 业务逻辑 | 业务层 | `XxxService` |
| `@Repository` | 数据访问 | 持久层 | `XxxDao` |

> 四个注解功能完全相同，区别仅在于语义。默认 bean id = **类名首字母小写**（如 `CommonComponent` → `commonComponent`）。  
> 手动指定：`@Component("myName")` 或 `@Component(value="myName")`。

### 2.2 包扫描配置（spring-01.xml）

```xml
<!-- 1. 普通扫描：扫描指定包及其子包下所有带注解的类 -->
<context:component-scan base-package="com.study.ioc_01"/>

<!-- 2. 排除特定注解 -->
<context:component-scan base-package="com.study">
    <context:exclude-filter type="annotation"
        expression="org.springframework.stereotype.Repository"/>
</context:component-scan>

<!-- 3. 只包含特定注解（需先关闭默认过滤器） -->
<context:component-scan base-package="com.study" use-default-filters="false">
    <context:include-filter type="annotation"
        expression="org.springframework.stereotype.Repository"/>
</context:component-scan>
```

---

## 三、生命周期与作用域

### 3.1 作用域 `@Scope`

| 值 | 含义 | 说明 |
|----|------|------|
| `ConfigurableBeanFactory.SCOPE_SINGLETON` | 单例（默认） | IoC 容器中只有一个实例 |
| `ConfigurableBeanFactory.SCOPE_PROTOTYPE` | 多例 | 每次 `getBean()` 创建新实例 |

### 3.2 生命周期回调

```java
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class JavaBean {
    @PostConstruct   // 初始化后执行
    public void init() { }

    @PreDestroy      // 销毁前执行
    public void destroy() { }
}
```

> **注意**：多例模式下，Spring **不会**调用 `@PreDestroy`，因为 Spring 不管理多例 bean 的完整生命周期。

---

## 四、DI 注解 —— 依赖注入

### 4.1 注入其他 Bean 对象

#### `@Autowired`（Spring 提供）

- **按类型**自动装配，IoC 容器中查找匹配类型的 bean 注入
- `required` 属性：默认 `true`（找不到 bean 直接报错），设为 `false` 则允许为 `null`（不推荐）

#### 同一接口有多个实现类时的解决方案

| 方案 | 写法 | 说明 |
|------|------|------|
| 方案一 | 成员变量名与 bean id 一致 | `@Autowired` 先按类型、再按变量名匹配 |
| 方案二 | `@Autowired` + `@Qualifier("beanId")` | 显式指定 bean id |
| 方案三 | `@Resource(name="beanId")` | JSR-250 标准注解，等价于方案二 |

```java
@Controller
public class UserController {
    // 方案二：@Autowired + @Qualifier
    @Autowired
    @Qualifier(value = "userServiceImpl")
    private UserServiceImpl userService;

    // 方案三：@Resource（推荐，更简洁）
    @Resource(name = "userServiceImpl")
    private UserService userService01;
}
```

> **等价关系**：`@Autowired(required=true)` + `@Qualifier("xxx")` = `@Resource(name="xxx")`

### 4.2 注入基本类型 `@Value`

```java
public class JavaBean {
    // 直接赋值
    @Value("19")
    private int age;

    // 读取外部配置文件的值
    @Value("${jdbc.userName}")
    private String userName;

    // 带默认值（配置文件中找不到 key 时使用默认值）
    @Value("${jdbc.password:123456}")
    private String password;
}
```

**前提条件**（spring-04.xml）：

```xml
<!-- 1. 扫描组件 -->
<context:component-scan base-package="com.study.ioc_04"/>
<!-- 2. 引入外部配置文件 -->
<context:property-placeholder location="classpath:jdbc.properties"/>
```

`jdbc.properties`：

```properties
jdbc.userName=root
jdbc.password=root
```

> 与 XML 方式相比，`@Value` **不需要** setter 方法。

---

## 五、项目结构速览

```
com.study
├── ioc_01/           # IoC 四大注解 + 包扫描
│   ├── CommonComponent.java   @Component
│   ├── XxxController.java     @Controller
│   ├── XxxService.java        @Service
│   └── XxxDao.java            @Repository
├── ioc_02/           # 生命周期 + 作用域
│   └── JavaBean.java          @PostConstruct / @PreDestroy / @Scope
├── ioc_03/           # DI 注入（Bean 对象）
│   ├── UserService.java            接口
│   ├── UserServiceImpl.java        实现类 1
│   ├── NewuserServiceImpl.java     实现类 2
│   └── UserController.java         @Autowired / @Qualifier / @Resource
└── ioc_04/           # DI 注入（基本类型）
    └── JavaBean.java          @Value + 外部配置
```

---

## 六、常见注意事项

1. **Bean 默认命名规则**：类名首字母小写（特例：前两个字母都大写则保持原样，如 `URL` → `URL`）
2. **`@Autowired` 找不到 bean**：默认直接报错，`required=false` 可以容忍空值但不推荐
3. **多实现类冲突**：必须用 `@Qualifier` 或 `@Resource(name=)` 明确指定
4. **`@PreDestroy` 在多例模式下不生效**：Spring 不管理 prototype bean 的销毁
