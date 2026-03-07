# Spring MVC 核心知识

> 本文围绕 Spring MVC 的请求处理主线展开：核心组件、参数绑定、响应处理、视图解析、拦截器与异常处理。
>
> 阅读后应能快速回答：请求如何被分发、参数如何注入、返回值如何渲染，以及常见配置的落地点。

## 一、SpringMVC简介和体验

### 1.1 介绍

官方文档：https://docs.spring.io/spring-framework/reference/web/webmvc.html

Spring Web MVC 是基于 Servlet API 构建的原始 Web 框架，正式名称来自源模块 `spring-webmvc`，通常简称 **Spring MVC**。

目前业界普遍选择 SpringMVC 作为 Java EE 项目表述层开发的**首选方案**，核心优势如下：

- **Spring 家族原生产品**，与 IOC 容器等基础设施无缝对接
- 表述层各细分领域**全方位覆盖**，提供完整解决方案
- 代码简洁，大幅提升开发效率
- 内部组件化程度高，**可插拔式**，按需配置
- **性能卓著**，适合大型、超大型互联网项目

原生 Servlet API 开发代码片段

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response) 
                                                        throws ServletException, IOException {  
    String userName = request.getParameter("userName");
    
    System.out.println("userName="+userName);
}
```

基于 SpringMVC 开发代码片段

```java
@RequestMapping("/user/login")
public String login(@RequestParam("userName") String userName,Sting password){
    
    log.debug("userName="+userName);
    //调用业务即可
    
    return "result";
}
```

### 1.2 主要作用

SpringMVC 在 SSM 框架中负责**表述层（控制层）**的实现简化，覆盖范围包括：

- 请求映射、请求分发
- 数据输入、类型转换、数据校验
- 视图界面、表单回显
- 会话控制、过滤拦截
- 异步交互、文件上传/下载

**核心价值：**
1. **简化前端参数接收**（通过形参列表）
2. **简化后端数据响应**（通过返回值）

### 1.3 核心组件和调用流程理解

Spring MVC 围绕**前端控制器模式**设计，中央 `DispatcherServlet` 负责整体请求处理调度。

**SpringMVC 涉及组件说明：**

| 组件 | 提供方 | 职责 |
| --- | --- | --- |
| `DispatcherServlet` | SpringMVC | 请求处理核心，所有请求都经过它分发（需在 `web.xml` 配置） |
| `HandlerMapping` | SpringMVC | 缓存 handler 与路径的映射关系，供 DS 查找（需加入 IoC 容器） |
| `HandlerAdapter` | SpringMVC | 处理请求参数和响应数据，作为 handler 与 DS 之间的适配器（需加入 IoC 容器） |
| `Handler` | 开发者 | `Controller` 类内部的方法，负责接收参数、调用业务、返回响应 |
| `ViewResolver` | SpringMVC | 简化模板视图查找（需加入 IoC 容器）。**前后端分离项目只返回 JSON，无需配置** |

### 1.4 快速体验

1. 体验场景需求

2. 配置分析
   1. `DispatcherServlet`：设置处理所有请求
   2. `HandlerMapping`、`HandlerAdapter`、`Handler` 需加入 IoC 容器，供 DS 调用
   3. `Handler`（Controller）需配置到 `HandlerMapping` 中供 DS 查找

3. 准备项目
   1. 创建项目

      项目名：`springmvc-base-quick`（需转成 maven/web 程序）

   2. 导入依赖

```xml
<properties>
    <spring.version>6.0.6</spring.version>
    <servlet.api>9.1.0</servlet.api>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<dependencies>
    <!-- springioc相关依赖  -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>

    <!-- web相关依赖  -->
    <!-- 在 pom.xml 中引入 Jakarta EE Web API 的依赖 -->
    <!--
        在 Spring Web MVC 6 中，Servlet API 迁移到了 Jakarta EE API，因此在配置 DispatcherServlet 时需要使用
         Jakarta EE 提供的相应类库和命名空间。错误信息 “‘org.springframework.web.servlet.DispatcherServlet’
         is not assignable to ‘javax.servlet.Servlet,jakarta.servlet.Servlet’” 表明你使用了旧版本的
         Servlet API，没有更新到 Jakarta EE 规范。
    -->
    <dependency>
        <groupId>jakarta.platform</groupId>
        <artifactId>jakarta.jakartaee-web-api</artifactId>
        <version>${servlet.api}</version>
        <scope>provided</scope>
    </dependency>

    <!-- springwebmvc相关依赖  -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>${spring.version}</version>
    </dependency>

</dependencies>
```
4. Controller 声明

```java
@Controller
public class HelloController {

    //handlers

    /**
     * handler就是controller内部的具体方法
     * @RequestMapping("/springmvc/hello") 就是用来向handlerMapping中注册的方法注解!
     * @ResponseBody 代表向浏览器直接返回数据!
     */
    @RequestMapping("/springmvc/hello")
    @ResponseBody
    public String hello(){
        System.out.println("HelloController.hello");
        return "hello springmvc!!";
    }
}

```
5. Spring MVC 核心组件配置类

> 声明 SpringMVC 涉及组件信息的配置类

```java
//TODO: SpringMVC对应组件的配置类 [声明SpringMVC需要的组件信息]

//TODO: 导入handlerMapping和handlerAdapter的三种方式
 //1.自动导入handlerMapping和handlerAdapter [推荐]
 //2.可以不添加,springmvc会检查是否配置handlerMapping和handlerAdapter,没有配置默认加载
 //3.使用@Bean方式配置handlerMapper和handlerAdapter
@EnableWebMvc     
@Configuration
@ComponentScan(basePackages = "com.atguigu.controller") //TODO: 进行controller扫
//WebMvcConfigurer springMvc进行组件配置的规范,配置组件,提供各种方法! 前期可以实现
public class SpringMvcConfig implements WebMvcConfigurer {

    @Bean
    public HandlerMapping handlerMapping(){
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public HandlerAdapter handlerAdapter(){
        return new RequestMappingHandlerAdapter();
    }
    
}

```
6. SpringMVC 环境搭建

> 对于使用基于 Java 的 Spring 配置的应用程序，建议这样做，如以下示例所示：

```java
//TODO: SpringMVC提供的接口,是替代web.xml的方案,更方便实现完全注解方式ssm处理!
//TODO: Springmvc框架会自动检查当前类的实现类,会自动加载 getRootConfigClasses / getServletConfigClasses 提供的配置类
//TODO: getServletMappings 返回的地址 设置DispatherServlet对应处理的地址
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

  /**
   * 指定service / mapper层的配置类
   */
  @Override
  protected Class<?>[] getRootConfigClasses() {
    return null;
  }

  /**
   * 指定springmvc的配置类
   * @return
   */
  @Override
  protected Class<?>[] getServletConfigClasses() {
    return new Class<?>[] { SpringMvcConfig.class };
  }

  /**
   * 设置dispatcherServlet的处理路径!
   * 一般情况下为 / 代表处理所有请求!
   */
  @Override
  protected String[] getServletMappings() {
    return new String[] { "/" };
  }
}
```
7. 启动测试

> **注意**：Tomcat 须为 **10+** 版本，方可支持 Jakarta EE API。

## 二、SpringMVC接收数据

### 2.1 访问路径设置

`@RequestMapping` 注解的作用是将请求 URL 与处理方法（handler）关联，建立映射关系。SpringMVC 接收到请求后，根据映射关系找到对应方法处理。

1. **精准路径匹配**

不使用任何通配符，按请求地址精确匹配。

```java
@Controller
public class UserController {

    /**
     * 精准设置访问地址 /user/login
     */
    @RequestMapping(value = {"/user/login"})
    @ResponseBody
    public String login(){
        System.out.println("UserController.login");
        return "login success!!";
    }

    /**
     * 精准设置访问地址 /user/register
     */
    @RequestMapping(value = {"/user/register"})
    @ResponseBody
    public String register(){
        System.out.println("UserController.register");
        return "register success!!";
    }
    
}

```
2. **模糊路径匹配**

通过通配符匹配多个类似地址。

```java
@Controller
public class ProductController {

    /**
     *  路径设置为 /product/*  
     *    /* 为单层任意字符串  /product/a  /product/aaa 可以访问此handler  
     *    /product/a/a 不可以
     *  路径设置为 /product/** 
     *   /** 为任意层任意字符串  /product/a  /product/aaa 可以访问此handler  
     *   /product/a/a 也可以访问
     */
    @RequestMapping("/product/*")
    @ResponseBody
    public String show(){
        System.out.println("ProductController.show");
        return "product show!";
    }
}

```

```text
单层匹配和多层匹配：
  /*：只能匹配URL地址中的一层，如果想准确匹配两层，那么就写“/*/*”以此类推。
  /**：可以匹配URL地址中的多层。
其中所谓的一层或多层是指一个URL地址字符串被“/”划分出来的各个层次
这个知识点虽然对于@RequestMapping注解来说实用性不大，但是将来配置拦截器的时候也遵循这个规则。
```
3. **类和方法级别区别**

`@RequestMapping` 可标注在类或方法上：

- **类级别**：映射整个控制器的通用前缀路径，避免每个方法重复写相同前缀
- **方法级别**：精细映射单个方法的请求路径和操作

```java
//1.标记到handler方法
@RequestMapping("/user/login")
@RequestMapping("/user/register")
@RequestMapping("/user/logout")

//2.优化标记类+handler方法
//类上
@RequestMapping("/user")
//handler方法上
@RequestMapping("/login")
@RequestMapping("/register")
@RequestMapping("/logout")

```
4. **附带请求方式限制**

HTTP 协议定义了八种请求方式，在 SpringMVC 中封装到了以下枚举类：

```java
public enum RequestMethod {
  GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
}
```

默认情况下 `@RequestMapping("/logout")` 接受任意请求方式；如需限制：

```java
@Controller
public class UserController {

    /**
     * 精准设置访问地址 /user/login
     * method = RequestMethod.POST 可以指定单个或者多个请求方式!
     * 注意:违背请求方式会出现405异常!
     */
    @RequestMapping(value = {"/user/login"} , method = RequestMethod.POST)
    @ResponseBody
    public String login(){
        System.out.println("UserController.login");
        return "login success!!";
    }

    /**
     * 精准设置访问地址 /user/register
     */
    @RequestMapping(value = {"/user/register"},method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String register(){
        System.out.println("UserController.register");
        return "register success!!";
    }

}
```

注意：违背请求方式会出现 **405 异常**。
5. **进阶注解**

`@RequestMapping` 提供以下 HTTP 方法快捷变体（**只能标注到方法上，不能标注到类上**）：

- `@GetMapping`
- `@PostMapping`
- `@PutMapping`
- `@DeleteMapping`
- `@PatchMapping`

```java
@RequestMapping(value="/login",method=RequestMethod.GET)
||
@GetMapping(value="/login")
```

注意：进阶注解只能添加到handler方法上，无法添加到类上！
6. **常见配置问题**

多个 handler 方法映射了同一个地址，SpringMVC 无法确定调用哪个方法，会报错：

> There is already 'demo03MappingMethodHandler' bean method com.atguigu.mvc.handler.Demo03MappingMethodHandler#empGet() **mapped**.

### 2.2 接收参数（重点）

#### 2.2.1 param 和 json参数比较

| 对比维度 | param 类型 | JSON 类型 |
| --- | --- | --- |
| 编码 | ASCII（如空格编码为 `%20`） | UTF-8 |
| 参数顺序 | 无顺序要求 | 键值对有序 |
| 数据类型 | 仅支持字符串、数值、布尔等简单类型 | 支持数组、对象等复杂类型 |
| 嵌套 | 不支持 | 支持 |
| 可读性 | 格式简单直观 | 嵌套结构更清晰 |

**实践惯例**：GET 请求用 param 参数；POST 请求用 JSON 请求体。

#### 2.2.2 param参数接收
1. **直接接值**

**要求**：形参名与传递参数名相同，类型一致，即可自动接收。

```java
@Controller
@RequestMapping("param")
public class ParamController {

    /**
     * 前端请求: http://localhost:8080/param/value?name=xx&age=18
     *
     * 可以利用形参列表,直接接收前端传递的param参数!
     *    要求: 参数名 = 形参名
     *          类型相同
     * 出现乱码正常，json接收具体解决！！
     * @return 返回前端数据
     */
    @GetMapping(value="/value")
    @ResponseBody
    public String setupForm(String name,int age){
        System.out.println("name = " + name + ", age = " + age);
        return name + age;
    }
}
```
2. **@RequestParam注解**

可以使用 `@RequestParam` 注释将 Servlet 请求参数（即查询参数或表单数据）绑定到控制器中的方法参数。

`@RequestParam`使用场景：
- 指定绑定的请求参数名
- 要求请求参数必须传递
- 为请求参数提供默认值

基本用法：

```java
 /**
 * 前端请求: http://localhost:8080/param/data?name=xx&stuAge=18
 * 
 *  使用@RequestParam注解标记handler方法的形参
 *  指定形参对应的请求参数@RequestParam(请求参数名称)
 */
@GetMapping(value="/data")
@ResponseBody
public Object paramForm(@RequestParam("name") String name, 
                        @RequestParam("stuAge") int age){
    System.out.println("name = " + name + ", age = " + age);
    return name+age;
}
```

默认情况下，`@RequestParam` 标注的参数是**必传**的。可通过设置 `required = false` 改为可选，并配合 `defaultValue` 指定默认值：将参数设置非必须，并且设置默认值：

```java
@GetMapping(value="/data")
@ResponseBody
public Object paramForm(@RequestParam("name") String name, 
                        @RequestParam(value = "stuAge",required = false,defaultValue = "18") int age){
    System.out.println("name = " + name + ", age = " + age);
    return name+age;
}

```
3. **特殊场景接值**
1. 一名多值

一个 key 对应多个值（如多选框），使用集合接收，**必须配合 `@RequestParam` 注解**。

```java
  /**
   * 前端请求: http://localhost:8080/param/mul?hbs=吃&hbs=喝
   *
   *  一名多值,可以使用集合接收即可!但是需要使用@RequestParam注解指定
   */
  @GetMapping(value="/mul")
  @ResponseBody
  public Object mulForm(@RequestParam List<String> hbs){
      System.out.println("hbs = " + hbs);
      return hbs;
  }
```
2. 实体接收

Spring MVC 支持用实体对象直接接收 HTTP 请求参数，将参数自动映射到对象属性上。**要求：属性名必须与参数名相同。**

定义用于接收参数的实体类：

```java
public class User {

  private String name;

  private int age = 18;

  // getter 和 setter 略
}
```

在控制器中使用实体对象接收：

```java
@Controller
@RequestMapping("param")
public class ParamController {

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public String addUser(User user) {
        // 在这里可以使用 user 对象的属性来接收请求参数
        System.out.println("user = " + user);
        return "success";
    }
}
```



#### 2.2.3 路径参数接收

在 RESTful 风格中，常用路径参数表示资源唯一标识符。Spring MVC 使用 `@PathVariable` 注解将 URL 占位符映射到方法参数。

```java
 /**
 * 动态路径设计: /user/{动态部分}/{动态部分}   动态部分使用{}包含即可! {}内部动态标识!
 * 形参列表取值: @PathVariable Long id  如果形参名 = {动态标识} 自动赋值!
 *              @PathVariable("动态标识") Long id  如果形参名 != {动态标识} 可以通过指定动态标识赋值!
 *
 * 访问测试:  /param/user/1/root  -> id = 1  uname = root
 */
@GetMapping("/user/{id}/{name}")
@ResponseBody
public String getUser(@PathVariable Long id, 
                      @PathVariable("name") String uname) {
    System.out.println("id = " + id + ", uname = " + uname);
    return "user_detail";
}
```

#### 2.2.4 json参数接收

前端传递 JSON 数据时，使用 `@RequestBody` 注解将请求体中的 JSON 自动转换为 Java 对象。

1. 前端发送 JSON 数据（使用 Postman 测试）：

```JSON
{
  "name": "张三",
  "age": 18,
  "gender": "男"
}
```
2. 定义接收 JSON 数据的 Java 类：

```java
public class Person {
  private String name;
  private int age;
  private String gender;
  // getter 和 setter 略
}
```
3. 在控制器中使用 `@RequestBody` 接收 JSON 并转换为对象：

```java
@PostMapping("/person")
@ResponseBody
public String addPerson(@RequestBody Person person) {

  // 在这里可以使用 person 对象来操作 JSON 数据中包含的属性
  return "success";
}
```

在上述代码中，`@RequestBody` 注解将请求体中的 JSON 数据映射到 `Person` 类型的 `person` 参数上，并将其作为一个对象来传递给 `addPerson()` 方法进行处理。
4. 完善配置

若出现以下错误：

```
org.springframework.web.HttpMediaTypeNotSupportedException: Content-Type 'application/json;charset=UTF-8' is not supported
```

**原因**：缺少 JSON 处理工具（Jackson）或未配置 JSON 转化器。

**解决**：在 `SpringMvcConfig` 配置类添加 `@EnableWebMvc`，并在 `pom.xml` 引入 jackson 依赖。配置类：

```java
//TODO: SpringMVC对应组件的配置类 [声明SpringMVC需要的组件信息]

//TODO: 导入handlerMapping和handlerAdapter的三种方式
 //1.自动导入handlerMapping和handlerAdapter [推荐]
 //2.可以不添加,springmvc会检查是否配置handlerMapping和handlerAdapter,没有配置默认加载
 //3.使用@Bean方式配置handlerMapper和handlerAdapter
@EnableWebMvc  //json数据处理,必须使用此注解,因为他会加入json处理器
@Configuration
@ComponentScan(basePackages = "com.atguigu.controller") //TODO: 进行controller扫描

//WebMvcConfigurer springMvc进行组件配置的规范,配置组件,提供各种方法! 前期可以实现
public class SpringMvcConfig implements WebMvcConfigurer {

}
```

pom.xml 加入jackson依赖

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.0</version>
</dependency>
```
5. @EnableWebMvc注解说明

`@EnableWebMvc` 等效于 XML 中的 `<mvc:annotation-driven>` 元素。其底层通过 `AnnotationDrivenBeanDefinitionParser` 完成以下关键动作：

- 将 `HandlerMapping` 和 `HandlerAdapter` 注册到 IoC 容器
- 自动检测并添加 Jackson 的请求体/响应体转化器

源码位置：`org.springframework.web.servlet.config.MvcNamespaceHandler`、`org.springframework.web.servlet.config.AnnotationDrivenBeanDefinitionParser`

```java
class AnnotationDrivenBeanDefinitionParser implements BeanDefinitionParser {

  public static final String HANDLER_MAPPING_BEAN_NAME = RequestMappingHandlerMapping.class.getName();

  public static final String HANDLER_ADAPTER_BEAN_NAME = RequestMappingHandlerAdapter.class.getName();

  static {
    ClassLoader classLoader = AnnotationDrivenBeanDefinitionParser.class.getClassLoader();
    javaxValidationPresent = ClassUtils.isPresent("jakarta.validation.Validator", classLoader);
    romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", classLoader);
    jaxb2Present = ClassUtils.isPresent("jakarta.xml.bind.Binder", classLoader);
    jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) &&
            ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader);
    jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);
    jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
    jackson2CborPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.cbor.CBORFactory", classLoader);
    gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
  }

  @Override
  @Nullable
  public BeanDefinition parse(Element element, ParserContext context) {
    //handlerMapping加入到ioc容器
    readerContext.getRegistry().registerBeanDefinition(HANDLER_MAPPING_BEAN_NAME, handlerMappingDef);

    //添加jackson转化器
    addRequestBodyAdvice(handlerAdapterDef);
    addResponseBodyAdvice(handlerAdapterDef);

    //handlerAdapter加入到ioc容器
    readerContext.getRegistry().registerBeanDefinition(HANDLER_ADAPTER_BEAN_NAME, handlerAdapterDef);
    return null;
  }

  //具体添加jackson转化对象方法
  protected void addRequestBodyAdvice(RootBeanDefinition beanDef) {
    if (jackson2Present) {
      beanDef.getPropertyValues().add("requestBodyAdvice",
          new RootBeanDefinition(JsonViewRequestBodyAdvice.class));
    }
  }

  protected void addResponseBodyAdvice(RootBeanDefinition beanDef) {
    if (jackson2Present) {
      beanDef.getPropertyValues().add("responseBodyAdvice",
          new RootBeanDefinition(JsonViewResponseBodyAdvice.class));
    }
  }

```

### 2.3 接收Cookie数据

使用 `@CookieValue` 将 HTTP Cookie 值绑定到方法参数。示例（Cookie 为 `JSESSIONID=415A4AC178C59DACE0B2C9CA727CDD84`）：

```java
@GetMapping("/demo")
public void handle(@CookieValue("JSESSIONID") String cookie) { 
  //...
}
```

### 2.4 接收请求头数据

使用 `@RequestHeader` 将请求头绑定到方法参数。示例（获取 `Accept-Encoding` 和 `Keep-Alive` 头）：

```java
@GetMapping("/demo")
public void handle(
    @RequestHeader("Accept-Encoding") String encoding, 
    @RequestHeader("Keep-Alive") long keepAlive) { 
  //...
}
```

### 2.5 原生Api对象操作

官方文档：https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/arguments.html

支持的控制器方法参数类型：

| Controller method argument 控制器方法参数                    | Description                                                  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `jakarta.servlet.ServletRequest`, `jakarta.servlet.ServletResponse` | 请求/响应对象                                                |
| `jakarta.servlet.http.HttpSession`                           | 强制存在会话。因此，这样的参数永远不会为 `null` 。           |
| `java.io.InputStream`, `java.io.Reader`                      | 用于访问由 Servlet API 公开的原始请求正文。                  |
| `java.io.OutputStream`, `java.io.Writer`                     | 用于访问由 Servlet API 公开的原始响应正文。                  |
| `@PathVariable`                                              | 接收路径参数注解                                             |
| `@RequestParam`                                              | 用于访问 Servlet 请求参数，包括多部分文件。参数值将转换为声明的方法参数类型。 |
| `@RequestHeader`                                             | 用于访问请求标头。标头值将转换为声明的方法参数类型。         |
| `@CookieValue`                                               | 用于访问Cookie。Cookie 值将转换为声明的方法参数类型。        |
| `@RequestBody`                                               | 用于访问 HTTP 请求正文。正文内容通过使用 `HttpMessageConverter` 实现转换为声明的方法参数类型。 |
| `java.util.Map`, `org.springframework.ui.Model`, `org.springframework.ui.ModelMap` | 共享域对象，并在视图呈现过程中向模板公开。                   |
| `Errors`, `BindingResult`                                    | 验证和数据绑定中的错误信息获取对象！                         |

获取原生对象示例：

```java
/**
 * 如果想要获取请求或者响应对象,或者会话等,可以直接在形参列表传入,并且不分先后顺序!
 * 注意: 接收原生对象,并不影响参数接收!
 */
@GetMapping("api")
@ResponseBody
public String api(HttpSession session , HttpServletRequest request,
                  HttpServletResponse response){
    String method = request.getMethod();
    System.out.println("method = " + method);
    return "api";
}
```

### 2.6 共享域对象操作

#### 2.6.1 属性（共享）域作用回顾

JavaWeb 中有四种常见共享域：

| 共享域 | 作用范围 | 典型用途 |
| --- | --- | --- |
| `ServletContext` | 整个 Web 应用 | 全局配置、所有用户共享的数据（线程安全） |
| `HttpSession` | 同一用户的多次请求 | 用户登录状态等会话级数据 |
| `HttpServletRequest` | 同一请求的多个处理器方法间 | 请求参数、属性在方法间传递 |
| `PageContext` | JSP 页面各作用域 | 在 `pageScope`/`requestScope` 等 JSP 作用域中共享数据 |

#### 2.6.2 Request级别属性（共享）域
1. 使用 Model 类型的形参

```java
@RequestMapping("/attr/request/model")
@ResponseBody
public String testAttrRequestModel(
    
        // 在形参位置声明Model类型变量，用于存储模型数据
        Model model) {
    
    // 我们将数据存入模型，SpringMVC 会帮我们把模型数据存入请求域
    // 存入请求域这个动作也被称为暴露到请求域
    model.addAttribute("requestScopeMessageModel","i am very happy[model]");
    
    return "target";
}
```
2. 使用 ModelMap 类型的形参

```java
@RequestMapping("/attr/request/model/map")
@ResponseBody
public String testAttrRequestModelMap(
    
        // 在形参位置声明ModelMap类型变量，用于存储模型数据
        ModelMap modelMap) {
    
    // 我们将数据存入模型，SpringMVC 会帮我们把模型数据存入请求域
    // 存入请求域这个动作也被称为暴露到请求域
    modelMap.addAttribute("requestScopeMessageModelMap","i am very happy[model map]");
    
    return "target";
}
```
3. 使用 Map 类型的形参

```java
@RequestMapping("/attr/request/map")
@ResponseBody
public String testAttrRequestMap(
    
        // 在形参位置声明Map类型变量，用于存储模型数据
        Map<String, Object> map) {
    
    // 我们将数据存入模型，SpringMVC 会帮我们把模型数据存入请求域
    // 存入请求域这个动作也被称为暴露到请求域
    map.put("requestScopeMessageMap", "i am very happy[map]");
    
    return "target";
}
```
4. 使用原生 request 对象

```java
@RequestMapping("/attr/request/original")
@ResponseBody
public String testAttrOriginalRequest(
    
        // 拿到原生对象，就可以调用原生方法执行各种操作
        HttpServletRequest request) {
    
    request.setAttribute("requestScopeMessageOriginal", "i am very happy[original]");
    
    return "target";
}
```
5. 使用 ModelAndView 对象

```java
@RequestMapping("/attr/request/mav")
public ModelAndView testAttrByModelAndView() {
    
    // 1.创建ModelAndView对象
    ModelAndView modelAndView = new ModelAndView();
    // 2.存入模型数据
    modelAndView.addObject("requestScopeMessageMAV", "i am very happy[mav]");
    // 3.设置视图名称
    modelAndView.setViewName("target");
    
    return modelAndView;
}
```

#### 2.6.3 Session级别属性（共享）域

```java
@RequestMapping("/attr/session")
@ResponseBody
public String testAttrSession(HttpSession session) {
    //直接对session对象操作,即对会话范围操作!
    return "target";
}
```

#### 2.6.4 Application级别属性（共享）域

SpringMVC 初始化容器时，会将 `ServletContext` 对象存入 IoC 容器，可直接注入使用：

```java
@Autowired
private ServletContext servletContext;

@RequestMapping("/attr/application")
@ResponseBody
public String attrApplication() {
    
    servletContext.setAttribute("appScopeMsg", "i am hungry...");
    
    return "target";
}
```

## 三、SpringMVC响应数据

### 3.1 handler方法分析

handler 方法结构说明：

```java
/**
 * TODO: 一个controller的方法是控制层的一个处理器,我们称为handler
 * TODO: handler需要使用@RequestMapping/@GetMapping系列,声明路径,在HandlerMapping中注册,供DS查找!
 * TODO: handler作用总结:
 *       1.接收请求参数(param,json,pathVariable,共享域等) 
 *       2.调用业务逻辑 
 *       3.响应前端数据(页面（不讲解模版页面跳转）,json,转发和重定向等)
 * TODO: handler如何处理呢
 *       1.接收参数: handler(形参列表: 主要的作用就是用来接收参数)
 *       2.调用业务: { 方法体  可以向后调用业务方法 service.xx() }
 *       3.响应数据: return 返回结果,可以快速响应前端数据
 */
@GetMapping
public Object handler(简化请求参数接收){
    调用业务方法
    返回的结果 （页面跳转，返回数据（json））
    return 简化响应前端数据;
}
```

**总结**：请求数据接收，都通过 handler 的**形参列表**；前端数据响应，都通过 handler 的 **`return`** 关键字处理。SpringMVC 大幅简化了参数接收和响应！

### 3.2 页面跳转控制

#### 3.2.1 快速返回模板视图
1. 开发模式回顾

Web 开发有两种主要模式：

- **前后端分离**（重点）：前后端使用不同技术栈，通过 API 接口通信，数据格式为 JSON/XML，效率高、易维护。
- **混合开发**：前后端代码在同一项目，小型项目常见，大型项目耦合度高、维护难度大。混合开发需要动态页面技术（如 JSP）来展示 Java 共享域数据。

2. jsp技术了解

JSP（JavaServer Pages）是 Sun 公司提出的基于 Java 的动态网页技术，通过在 HTML 中嵌入 Java 代码生成动态内容。

主要特点：

1. 简单：Java 代码直接嵌入 HTML，生成动态内容方便
2. 高效：首次运行时转换为 Servlet 并编译字节码，支持 JIT 编译
3. 多样化：支持 JSTL、EL 等标准标签库

3. 准备jsp页面和依赖

`pom.xml` 依赖

```xml
<!-- jsp需要依赖! jstl-->
<dependency>
    <groupId>jakarta.servlet.jsp.jstl</groupId>
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
    <version>3.0.0</version>
</dependency>
```

JSP 页面创建

建议位置：`/WEB-INF/` 下，避免外部直接访问（如 `/WEB-INF/views/home.jsp`）。

```java
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Title</title>
  </head>
  <body>
        <!-- 可以获取共享域的数据,动态展示! jsp== 后台vue -->
        ${msg}
  </body>
</html>

```
4. 快速响应模版页面
1. 配置 JSP 视图解析器

```java
@EnableWebMvc  //json数据处理,必须使用此注解,因为他会加入json处理器
@Configuration
@ComponentScan(basePackages = "com.atguigu.controller") //TODO: 进行controller扫描

//WebMvcConfigurer springMvc进行组件配置的规范,配置组件,提供各种方法! 前期可以实现
public class SpringMvcConfig implements WebMvcConfigurer {

    //配置jsp对应的视图解析器
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //快速配置jsp模板语言对应的
        registry.jsp("/WEB-INF/views/",".jsp");
    }
}
```
2. handler 返回视图

```java
/**
 *  跳转到提交文件页面  /save/jump
 *  
 *  如果要返回jsp页面!
 *     1.方法返回值改成字符串类型
 *     2.返回逻辑视图名即可    
 *         <property name="prefix" value="/WEB-INF/views/"/>
 *            + 逻辑视图名 +
 *         <property name="suffix" value=".jsp"/>
 */
@GetMapping("jump")
public String jumpJsp(Model model){
    System.out.println("FileController.jumpJsp");
    model.addAttribute("msg","request data!!");
    return "home";
}
```

#### 3.2.2 转发和重定向

在 Spring MVC 中，通过 handler 方法返回值中的 `redirect` 或 `forward` 关键字实现重定向和转发。

```java
@RequestMapping("/redirect-demo")
public String redirectDemo() {
    // 重定向到 /demo 路径 
    return "redirect:/demo";
}

@RequestMapping("/forward-demo")
public String forwardDemo() {
    // 转发到 /demo 路径
    return "forward:/demo";
}

//注意： 转发和重定向到项目下资源路径都是相同，都不需要添加项目根路径！填写项目下路径即可！
```

**总结：**

- 将方法的返回值设置为 `String` 类型
- **转发**：使用 **`forward`** 关键字，**重定向**：使用 **`redirect`** 关键字
- 格式：**`关键字:/路径`**
- **注意**：项目下的资源，转发和重定向都使用项目下路径，不需要添加项目根路径

### 3.3 返回JSON数据（重点）

#### 3.3.1 前置准备

在 `pom.xml` 中引入 jackson 依赖，并在配置类添加 `@EnableWebMvc` 开启 JSON 转化器。

引入依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.0</version>
</dependency>
```

添加 JSON 数据转化器（配置类添加 `@EnableWebMvc`）：

```java
//TODO: SpringMVC对应组件的配置类 [声明SpringMVC需要的组件信息]

//TODO: 导入handlerMapping和handlerAdapter的三种方式
 //1.自动导入handlerMapping和handlerAdapter [推荐]
 //2.可以不添加,springmvc会检查是否配置handlerMapping和handlerAdapter,没有配置默认加载
 //3.使用@Bean方式配置handlerMapper和handlerAdapter
@EnableWebMvc  //json数据处理,必须使用此注解,因为他会加入json处理器
@Configuration
@ComponentScan(basePackages = "com.atguigu.controller") //TODO: 进行controller扫描

//WebMvcConfigurer springMvc进行组件配置的规范,配置组件,提供各种方法! 前期可以实现
public class SpringMvcConfig implements WebMvcConfigurer {

}
```

#### 3.3.2 @ResponseBody
1. 方法上使用@ResponseBody

`@ResponseBody` 标注在方法上，将返回值直接序列化为 JSON/XML 响应给客户端，**不经过视图解析器**。前后端分离项目使用。

```java
@GetMapping("/accounts/{id}")
@ResponseBody
public Object handle() {
  // ...
  return obj;
}
```

接收 JSON 请求体并返回 JSON 示例（对象由 Jackson 自动序列化）：

```java
@RequestMapping(value = "/user/detail", method = RequestMethod.POST)
@ResponseBody
public User getUser(@RequestBody User userParam) {
    System.out.println("userParam = " + userParam);
    User user = new User();
    user.setAge(18);
    user.setName("John");
    //返回的对象,会使用jackson的序列化工具,转成json返回给前端!
    return user;
}
```

返回结果：

2. 类上使用@ResponseBody

若类中所有方法都需要 `@ResponseBody`，可将注解提升到类上，使全部方法生效：

```java
@ResponseBody  //responseBody可以添加到类上,代表默认类中的所有方法都生效!
@Controller
@RequestMapping("param")
public class ParamController {
```

#### 3.3.3 @RestController

`@RestController` = `@Controller` + `@ResponseBody`，标注在类上时，类中所有方法默认返回 JSON 数据。

`@RestController` 源码：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController {
 
  /**
   * The value may indicate a suggestion for a logical component name,
   * to be turned into a Spring bean in case of an autodetected component.
   * @return the suggested component name, if any (or empty String otherwise)
   * @since 4.0.1
   */
  @AliasFor(annotation = Controller.class)
  String value() default "";
 
}
```

### 3.4 返回静态资源处理
1. **静态资源概念**

资源本身已经是可以直接拿到浏览器上使用的程度了，**不需要在服务器端做任何运算、处理**。典型的静态资源包括：

- 纯HTML文件
- 图片
- CSS文件
- JavaScript文件
- ……
2. **静态资源访问和问题解决**

**问题原因**：`DispatcherServlet` 的 `url-pattern` 配置为 `”/”` 时，所有请求都由 SpringMVC 处理；而静态资源（如 `images/mi.jpg`）没有对应的 `@RequestMapping`，导致返回 404。

**解决方案**：在 `SpringMvcConfig` 配置类中覆盖 `configureDefaultServletHandling` 方法：

```java
@EnableWebMvc  //json数据处理,必须使用此注解,因为他会加入json处理器
@Configuration
@ComponentScan(basePackages = "com.atguigu.controller") //TODO: 进行controller扫描
//WebMvcConfigurer springMvc进行组件配置的规范,配置组件,提供各种方法! 前期可以实现
public class SpringMvcConfig implements WebMvcConfigurer {

    //配置jsp对应的视图解析器
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //快速配置jsp模板语言对应的
        registry.jsp("/WEB-INF/views/",".jsp");
    }
    
    //开启静态资源处理 <mvc:default-servlet-handler/>
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
```

- **新问题**：开启默认 Servlet 处理后，其他正常 handler 请求访问不了

**解决方案**：同时配置 `@EnableWebMvc`：

```xml
@EnableWebMvc  //json数据处理,必须使用此注解,因为他会加入json处理器
```

## 四、RESTFul风格设计和实战

### 4.1 RESTFul风格概述

#### 4.1.1 RESTFul风格简介

RESTful（Representational State Transfer）是一种基于标准 HTTP 方法的软件架构风格，广泛用于现代 Web 服务开发。遵循 RESTful 原则可构建出易于理解、可扩展、松耦合的 Web 服务，有助于更好地设计 HTTP API 接口。

#### 4.1.2 RESTFul风格特点
1. 每一个URI代表1种资源（URI 是名词）；
2. 客户端使用GET、POST、PUT、DELETE 4个表示操作方式的动词对服务端资源进行操作：GET用来获取资源，POST用来新建资源（也可以用于更新资源），PUT用来更新资源，DELETE用来删除资源；
3. 资源的表现形式是XML或者**JSON**；
4. 客户端与服务端之间的交互在请求之间是无状态的，从客户端到服务端的每个请求都必须包含理解请求所必需的信息。

#### 4.1.3 **RESTFul风格设计规范**
1. **HTTP协议请求方式要求**

REST 风格主张在项目设计、开发过程中，具体的操作符合**HTTP协议定义的请求方式的语义**。

| 操作     | 请求方式 |
| -------- | -------- |
| 查询操作 | GET      |
| 保存操作 | POST     |
| 删除操作 | DELETE   |
| 更新操作 | PUT      |

2. **URL路径风格要求**

REST 风格下，每个资源有唯一 URI，路径设计**用名词表示资源**（不含动词），操作语义由 HTTP 方法表达。

| 操作 | 传统风格                | REST 风格                              |
| ---- | ----------------------- | -------------------------------------- |
| 保存 | /CRUD/saveEmp           | URL 地址：/CRUD/emp 请求方式：POST     |
| 删除 | /CRUD/removeEmp?empId=2 | URL 地址：/CRUD/emp/2 请求方式：DELETE |
| 更新 | /CRUD/updateEmp         | URL 地址：/CRUD/emp 请求方式：PUT      |
| 查询 | /CRUD/editEmp?empId=2   | URL 地址：/CRUD/emp/2 请求方式：GET    |

#### 4.1.4 RESTFul风格好处
1. 含蓄，安全

使用问号键值对的方式给服务器传递数据太明显，容易被人利用来对系统进行破坏。使用 REST 风格携带数据不再需要明显的暴露数据的名称。
2. 风格统一

URL 地址整体格式统一，从前到后始终都使用斜杠划分各个单词，用简单一致的格式表达语义。
3. 无状态

在调用一个接口（访问、操作资源）的时候，可以不用考虑上下文，不用考虑当前状态，极大的降低了系统设计的复杂度。
4. 严谨，规范

严格按照 HTTP1.1 协议中定义的请求方式本身的语义进行操作。
5. 简洁，优雅

过去做增删改查操作需要设计 4 个不同的 URL，现在一个就够了（参见 4.1.3 表格）。

6. 丰富的语义

通过 URL 地址即可表达资源层级关系，示例：

> `http://localhost:8080/shop` → `http://localhost:8080/shop/product` → `http://localhost:8080/shop/product/cellPhone` → `http://localhost:8080/shop/product/cellPhone/iPhone`

### 4.2 RESTFul风格实战

#### 4.2.1 需求分析
- **数据结构**：`User {id 唯一标识, name 用户名, age 用户年龄}`
- **功能列表**：
  - 用户数据分页展示（参数：`page` 默认 1，`size` 默认 10）
  - 保存用户
  - 根据用户 id 查询详情
  - 根据用户 id 更新数据
  - 根据用户 id 删除数据
  - 多条件模糊查询（参数：`keyword`、`page`、`size`）

#### 4.2.2 RESTFul风格接口设计
1. **接口设计**

|          |                  |                               |              |
| -------- | ---------------- | ----------------------------- | ------------ |
| 功能     | 接口和请求方式   | 请求参数                      | 返回值       |
| 分页查询 | GET  /user       | page=1&size=10                | { 响应数据 } |
| 用户添加 | POST /user       | { user 数据 }                 | {响应数据}   |
| 用户详情 | GET /user/1      | 路径参数                      | {响应数据}   |
| 用户更新 | PUT /user        | { user 更新数据}              | {响应数据}   |
| 用户删除 | DELETE /user/1   | 路径参数                      | {响应数据}   |
| 条件模糊 | GET /user/search | page=1&size=10&keywork=关键字 | {响应数据}   |

2. **问题讨论**

> RESTful 风格下，并非所有参数都必须路径传递，可按需选择路径参数、请求参数或请求体。

选择原则：

- **路径参数**：指定单一资源的唯一标识（如查询用户详情 `/user/1`），语义明确
- **请求参数**：多条件组合查询时更灵活简洁（如 `?page=1&size=10&keyword=xx`）
- 请求参数建议**不超过 10 个**
- 敏感信息使用 **POST + 请求体**传递

#### 4.2.3 后台接口实现

准备用户实体类：

```java
package com.atguigu.pojo;

/**
 * projectName: com.atguigu.pojo
 * 用户实体类
 */
public class User {

    private Integer id;
    private String name;

    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

```

准备用户Controller:

```java
/**
 * projectName: com.atguigu.controller
 *
 * description: 用户模块的控制器
 */
@RequestMapping("user")
@RestController
public class UserController {

    /**
     * 模拟分页查询业务接口
     */
    @GetMapping
    public Object queryPage(@RequestParam(name = "page",required = false,defaultValue = "1")int page,
                            @RequestParam(name = "size",required = false,defaultValue = "10")int size){
        System.out.println("page = " + page + ", size = " + size);
        System.out.println("分页查询业务!");
        return "{'status':'ok'}";
    }

    /**
     * 模拟用户保存业务接口
     */
    @PostMapping
    public Object saveUser(@RequestBody User user){
        System.out.println("user = " + user);
        System.out.println("用户保存业务!");
        return "{'status':'ok'}";
    }

    /**
     * 模拟用户详情业务接口
     */
    @PostMapping("/{id}")
    public Object detailUser(@PathVariable Integer id){
        System.out.println("id = " + id);
        System.out.println("用户详情业务!");
        return "{'status':'ok'}";
    }

    /**
     * 模拟用户更新业务接口
     */
    @PutMapping
    public Object updateUser(@RequestBody User user){
        System.out.println("user = " + user);
        System.out.println("用户更新业务!");
        return "{'status':'ok'}";
    }

    /**
     * 模拟条件分页查询业务接口
     */
    @GetMapping("search")
    public Object queryPage(@RequestParam(name = "page",required = false,defaultValue = "1")int page,
                            @RequestParam(name = "size",required = false,defaultValue = "10")int size,
                            @RequestParam(name = "keyword",required= false)String keyword){
        System.out.println("page = " + page + ", size = " + size + ", keyword = " + keyword);
        System.out.println("条件分页查询业务!");
        return "{'status':'ok'}";
    }
}
```

## 五、SpringMVC其他扩展

### 5.1 全局异常处理机制

#### 5.1.1 异常处理两种方式

| 方式 | 实现 | 特点 |
| --- | --- | --- |
| **编程式异常处理** | `try-catch` 块手动捕获 | 灵活，但异常代码混入业务代码，可读性差 |
| **声明式异常处理** | `@ExceptionHandler` 注解 | 异常逻辑与业务分离，**推荐使用** |

> **最佳实践**：使用 `@RestControllerAdvice` + `@ExceptionHandler` 实现**全局统一异常处理**，所有模块遵循同一套异常响应规范，项目更清晰易维护。

#### 5.1.2 基于注解异常声明异常处理
1. 声明异常处理控制器类

统一定义异常处理 handler 方法：

```java
/**
 * projectName: com.atguigu.execptionhandler
 * 
 * description: 全局异常处理器,内部可以定义异常处理Handler!
 */

/**
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 * @ControllerAdvice 代表当前类的异常处理controller! 
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  
}
```
2. 声明异常处理 handler 方法

异常处理 handler 与普通 handler 参数接收/响应方式一致，区别在于：普通 handler 通过 `@RequestMapping` 映射路径；异常处理 handler 通过 `@ExceptionHandler` 映射异常类型，发生对应异常时自动调用。

```java
/**
 * 异常处理handler 
 * @ExceptionHandler(HttpMessageNotReadableException.class) 
 * 该注解标记异常处理Handler,并且指定发生异常调用该方法!
 * 
 * 
 * @param e 获取异常对象!
 * @return 返回handler处理结果!
 */
@ExceptionHandler(HttpMessageNotReadableException.class)
public Object handlerJsonDateException(HttpMessageNotReadableException e){
    
    return null;
}

/**
 * 当发生空指针异常会触发此方法!
 * @param e
 * @return
 */
@ExceptionHandler(NullPointerException.class)
public Object handlerNullException(NullPointerException e){

    return null;
}

/**
 * 所有异常都会触发此方法!但是如果有具体的异常处理Handler! 
 * 具体异常处理Handler优先级更高!
 * 例如: 发生NullPointerException异常!
 *       会触发handlerNullException方法,不会触发handlerException方法!
 * @param e
 * @return
 */
@ExceptionHandler(Exception.class)
public Object handlerException(Exception e){

    return null;
}
```
3. 配置文件扫描控制器类配置

确保异常处理控制类所在包被 `@ComponentScan` 扫描：

```java
 <!-- 扫描controller对应的包,将handler加入到ioc-->
 @ComponentScan(basePackages = {"com.atguigu.controller",
 "com.atguigu.exceptionhandler"})
```

### 5.2 拦截器使用

#### 5.2.1 拦截器概念

拦截器在请求到达具体 handler 方法前统一执行检测（类似进站统一检票）。

**拦截器（SpringMVC）vs 过滤器（JavaWeb）对比：**

| 维度 | 过滤器（Filter） | 拦截器（Interceptor） |
| --- | --- | --- |
| 工作平台 | Servlet 容器 | SpringMVC |
| 拦截范围 | 整个 Web 应用 | SpringMVC 负责的请求 |
| IOC 容器支持 | 间接（需调用工具方法） | 直接（自身在 IoC 容器中） |

> **选择原则**：功能若能用 SpringMVC 拦截器实现，优先使用拦截器。

#### 5.2.2 拦截器使用
1. 创建拦截器类

```java
public class Process01Interceptor implements HandlerInterceptor {

    // if( ! preHandler()){return;}
    // 在处理请求的目标 handler 方法前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("request = " + request + ", response = " + response + ", handler = " + handler);
        System.out.println("Process01Interceptor.preHandle");
         
        // 返回true：放行
        // 返回false：不放行
        return true;
    }
 
    // 在目标 handler 方法之后，handler报错不执行!
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("request = " + request + ", response = " + response + ", handler = " + handler + ", modelAndView = " + modelAndView);
        System.out.println("Process01Interceptor.postHandle");
    }
 
    // 渲染视图之后执行(最后),一定执行!
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("request = " + request + ", response = " + response + ", handler = " + handler + ", ex = " + ex);
        System.out.println("Process01Interceptor.afterCompletion");
    }
}
```

2. 修改配置类添加拦截器

```java
@EnableWebMvc  //json数据处理,必须使用此注解,因为他会加入json处理器
@Configuration
@ComponentScan(basePackages = {"com.atguigu.controller","com.atguigu.exceptionhandler"}) //TODO: 进行controller扫描
//WebMvcConfigurer springMvc进行组件配置的规范,配置组件,提供各种方法! 前期可以实现
public class SpringMvcConfig implements WebMvcConfigurer {

    //配置jsp对应的视图解析器
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //快速配置jsp模板语言对应的
        registry.jsp("/WEB-INF/views/",".jsp");
    }

    //开启静态资源处理 <mvc:default-servlet-handler/>
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) { 
        //将拦截器添加到Springmvc环境,默认拦截所有Springmvc分发的请求
        registry.addInterceptor(new Process01Interceptor());
    }
}

```
3. 配置详解
1. 默认拦截全部

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    //将拦截器添加到Springmvc环境,默认拦截所有Springmvc分发的请求
    registry.addInterceptor(new Process01Interceptor());
}

```
2. 精准配置

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    
    //将拦截器添加到Springmvc环境,默认拦截所有Springmvc分发的请求
    registry.addInterceptor(new Process01Interceptor());
    
    //精准匹配,设置拦截器处理指定请求 路径可以设置一个或者多个,为项目下路径即可
    //addPathPatterns("/common/request/one") 添加拦截路径
    //也支持 /* 和 /** 模糊路径。 * 任意一层字符串 ** 任意层 任意字符串
    registry.addInterceptor(new Process01Interceptor()).addPathPatterns("/common/request/one","/common/request/tow");
}

```
3. 排除配置

```java
//添加拦截器
@Override
public void addInterceptors(InterceptorRegistry registry) {
    
    //将拦截器添加到Springmvc环境,默认拦截所有Springmvc分发的请求
    registry.addInterceptor(new Process01Interceptor());
    
    //精准匹配,设置拦截器处理指定请求 路径可以设置一个或者多个,为项目下路径即可
    //addPathPatterns("/common/request/one") 添加拦截路径
    registry.addInterceptor(new Process01Interceptor()).addPathPatterns("/common/request/one","/common/request/tow");
    
    
    //排除匹配,排除应该在匹配的范围内排除
    //addPathPatterns("/common/request/one") 添加拦截路径
    //excludePathPatterns("/common/request/tow"); 排除路径,排除应该在拦截的范围内
    registry.addInterceptor(new Process01Interceptor())
            .addPathPatterns("/common/request/one","/common/request/tow")
            .excludePathPatterns("/common/request/tow");
}
```
**多个拦截器执行顺序**

> ⚠️ **注意**：多个拦截器的执行顺序与配置顺序相关，`postHandle` 和 `afterCompletion` 是**逆序**执行的。

| 方法 | 执行顺序 | 说明 |
| --- | --- | --- |
| `preHandle()` | **正序**（按配置顺序） | 返回 `false` 则中断后续处理 |
| `postHandle()` | **逆序**（与配置相反） | Handler 执行后、视图渲染前 |
| `afterCompletion()` | **逆序**（与配置相反） | 视图渲染完成后，用于资源清理 |

### 5.3 参数校验

> 在 Web 应用三层架构体系中，表述层负责接收浏览器提交的数据，业务逻辑层负责数据的处理。为了能够让业务逻辑层基于正确的数据进行处理，我们需要在表述层对数据进行检查，将错误的数据隔绝在业务逻辑层之外。

1. **校验概述**

JSR 303 是 Java 为 Bean 数据合法性校验提供的标准框架，它已经包含在 JavaEE 6.0 标准中。JSR 303 通过在 Bean 属性上标注类似于 @NotNull、@Max 等标准的注解指定校验规则，并通过标准的验证接口对Bean进行验证。

| 注解                       | 规则                                           |
| -------------------------- | ---------------------------------------------- |
| @Null                      | 标注值必须为 null                              |
| @NotNull                   | 标注值不可为 null                              |
| @AssertTrue                | 标注值必须为 true                              |
| @AssertFalse               | 标注值必须为 false                             |
| @Min(value)                | 标注值必须大于或等于 value                     |
| @Max(value)                | 标注值必须小于或等于 value                     |
| @DecimalMin(value)         | 标注值必须大于或等于 value                     |
| @DecimalMax(value)         | 标注值必须小于或等于 value                     |
| @Size(max,min)             | 标注值大小必须在 max 和 min 限定的范围内       |
| @Digits(integer,fratction) | 标注值值必须是一个数字，且必须在可接受的范围内 |
| @Past                      | 标注值只能用于日期型，且必须是过去的日期       |
| @Future                    | 标注值只能用于日期型，且必须是将来的日期       |
| @Pattern(value)            | 标注值必须符合指定的正则表达式                 |

JSR 303 只是一套标准，需要提供其实现才可以使用。Hibernate Validator 是 JSR 303 的一个参考实现，除支持所有标准的校验注解外，它还支持以下的扩展注解：

| 注解      | 规则                               |
| --------- | ---------------------------------- |
| @Email    | 标注值必须是格式正确的 Email 地址  |
| @Length   | 标注值字符串大小必须在指定的范围内 |
| @NotEmpty | 标注值字符串不能是空字符串         |
| @Range    | 标注值必须在指定的范围内           |

Spring 4.0 版本已经拥有自己独立的数据校验框架，同时支持 JSR 303 标准的校验框架。Spring 在进行数据绑定时，可同时调用校验框架完成数据校验工作。在SpringMVC 中，可直接通过注解驱动 @EnableWebMvc 的方式进行数据校验。Spring 的 LocalValidatorFactoryBean 既实现了 Spring 的 Validator 接口，也实现了 JSR 303 的 Validator 接口。只要在Spring容器中定义了一个LocalValidatorFactoryBean，即可将其注入到需要数据校验的 Bean中。Spring本身并没有提供JSR 303的实现，所以必须将JSR 303的实现者的jar包放到类路径下。

配置 @EnableWebMvc后，SpringMVC 会默认装配好一个 LocalValidatorFactoryBean，通过在处理方法的入参上标注 @Validated 注解即可让 SpringMVC 在完成数据绑定后执行数据校验的工作。
2. **操作演示**
- 导入依赖

```xml
<!-- 校验注解 -->
<dependency>
    <groupId>jakarta.platform</groupId>
    <artifactId>jakarta.jakartaee-web-api</artifactId>
    <version>9.1.0</version>
    <scope>provided</scope>
</dependency>
        
<!-- 校验注解实现-->        
<!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.0.Final</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.hibernate.validator/hibernate-validator-annotation-processor -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator-annotation-processor</artifactId>
    <version>8.0.0.Final</version>
</dependency>
```
- 应用校验注解

```java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;

/**
 * projectName: com.atguigu.pojo
 */
public class User {
    //age   1 <=  age < = 150
    @Min(10)
    private int age;

    //name 3 <= name.length <= 6
    @Length(min = 3,max = 10)
    private String name;

    //email 邮箱格式
    @Email
    private String email;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

```
- handler标记和绑定错误收集

```java
@RestController
@RequestMapping("user")
public class UserController {

    /**
     * @Validated 代表应用校验注解! 必须添加!
     */
    @PostMapping("save")
    public Object save(@Validated @RequestBody User user,
                       //在实体类参数和 BindingResult 之间不能有任何其他参数, BindingResult可以接受错误信息,避免信息抛出!
                       BindingResult result){
       //判断是否有信息绑定错误! 有可以自行处理!
        if (result.hasErrors()){
            System.out.println("错误");
            String errorMsg = result.getFieldError().toString();
            return errorMsg;
        }
        //没有,正常处理业务即可
        System.out.println("正常");
        return user;
    }
}
```
- 测试效果

3. **易混总结**

@NotNull、@NotEmpty、@NotBlank 都是用于在数据校验中检查字段值是否为空的注解，但是它们的用法和校验规则有所不同。

1. @NotNull  (包装类型不为null)

@NotNull 注解是 JSR 303 规范中定义的注解，当被标注的字段值为 null 时，会认为校验失败而抛出异常。该注解不能用于字符串类型的校验，若要对字符串进行校验，应该使用 @NotBlank 或 @NotEmpty 注解。
2. @NotEmpty (集合类型长度大于0)

@NotEmpty 注解同样是 JSR 303 规范中定义的注解，对于 CharSequence、Collection、Map 或者数组对象类型的属性进行校验，校验时会检查该属性是否为 Null 或者 size()==0，如果是的话就会校验失败。但是对于其他类型的属性，该注解无效。需要注意的是只校验空格前后的字符串，如果该字符串中间只有空格，不会被认为是空字符串，校验不会失败。
3. @NotBlank （字符串，不为null，切不为"  "字符串）

@NotBlank 注解是 Hibernate Validator 附加的注解，对于字符串类型的属性进行校验，校验时会检查该属性是否为 Null 或 “” 或者只包含空格，如果是的话就会校验失败。需要注意的是，@NotBlank 注解只能用于字符串类型的校验。

总之，这三种注解都是用于校验字段值是否为空的注解，但是其校验规则和用法有所不同。在进行数据校验时，需要根据具体情况选择合适的注解进行校验。

## 六、SpringMVC总结

|                 |                                            |
| --------------- | ------------------------------------------ |
| 核心点          | 掌握目标                                   |
| springmvc框架   | 主要作用、核心组件、调用流程               |
| 简化参数接收    | 路径设计、参数接收、请求头接收、cookie接收 |
| 简化数据响应    | 模板页面、转发和重定向、JSON数据、静态资源 |
| restful风格设计 | 主要作用、具体规范、请求方式和请求参数选择 |
| 功能扩展        | 全局异常处理、拦截器、参数校验注解         |