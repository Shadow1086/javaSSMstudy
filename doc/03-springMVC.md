# Spring MVC

## 1. 核心流程

![](https://cdn.jsdelivr.net/gh/Shadow1086/myPicture@master/uPic/2026/04/22/15-01-zhXRwL)

- **核心组件理解**：
  - `DispatcherServlet`：`Spring MVC` 提供，需要使用 `web.xml` 文件配置使其生效。它是**整个流程处理的核心**，所有请求都经过它的**处理和分发**。
  - `HandlerMapping`：`Spring MVC` 提供，需要进行 `IoC` 配置，使其加入 `IoC` 容器后生效。它内部缓存了 `handler`（`Controller` 方法）和 `handler` 访问路径数据，被 `DispatcherServlet` 调用，用于**查找路径对应的 `handler`**。
  - `HandlerAdapter`：`Spring MVC` 提供，需要进行 `IoC` 配置，使其加入 `IoC` 容器。它可以处理**请求参数**和**响应数据映射**，每次 `DispatcherServlet` 都是通过 `HandlerAdapter` 间接调用 `handler`。它是 `handler` 和 `DispatcherServlet` 之间的**适配器**。比如：可以自动转换json数据，类似于WebUtil
  - `Handler`：`handler` 又称处理器，是 `Controller` 类内部的方法简称，由我们自己定义，用来**接收参数**，向后调用业务，最终**返回响应结果**。
  - `ViewResolver`：视图解析器，**作用**是简化模板视图页面查找。**注意**：前后端分离的项目中，后端只返回 `JSON` 数据，不返回页面，就不需要使用视图解析器，所以它并不是必须的。

## 2. 快速构建

### 2.1 创建项目和配置类

1. 创建一个 `Java Web` 项目，创建 `controller` 和 `config` 两个目录。
2. 创建 `MvcConfig.class` 配置类。

```java
@EnableWebMvc
@Configuration
@ComponentScan("com.study.controller")
public class MvcConfig implements WebMvcConfigurer {

}
```

3. 创建 `SpringMvcInit.class` 类。

```java
public class SpringMvcInit extends AbstractAnnotationConfigDispatcherServletInitializer {
	/**
	 * 后期创建 service、mapper 层的 IoC 容器
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[0];
	}

	/**
	 * 设置项目对应的 Spring MVC Controller 配置
	 */
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{MvcConfig.class};
	}

	// 配置 Spring MVC 内部自带的 Servlet 的访问地址
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
}
```

4. 在 `Controller` 层中创建具体业务类。

```java
@RestController
@RequestMapping("hello")
public class HelloController {
	// handler -> /hello/springmvc/hello return "hello springMVC"
	// @RequestMapping("xxx") 中的 xxx 是对外访问的地址，是注册到 HandlerMapping 的注解
	@ResponseBody       // 直接返回字符串给前端，不会找视图解析器
	@RequestMapping("springmvc/hello")
	public String hello() {
		System.out.println("HelloController.hello");

		// 返回给前端
		return "hello springMVC";
	}
}
```

### 2.2 `SpringMvcInit.class` 详解

这个类需要继承 `AbstractAnnotationConfigDispatcherServletInitializer`，而这个类又继承很多类，如下：

```text
SpringMvcInit.class
-> AbstractAnnotationConfigDispatcherServletInitializer
-> AbstractDispatcherServletInitializer
-> WebApplicationInitializer 接口
```

其中 `WebApplicationInitializer` 中有 `onStartup` 方法，在其子类、孙子类中均有重写，目的就是在启动时创建 `IoC` 容器，并将配置文件放入容器中。

- `getServletConfigClasses()` 方法：继承自其父类，其父类中的 `createServletApplicationContext` 方法调用此方法，获取配置文件，再使用 `register()` 方法将配置类加载到容器中。

## 3. `Spring MVC` 接收数据

### 3.1 访问路径设置

- 使用 `@RequestMapping("path")`：将请求的 `URL` 地址和处理请求的方式（`handler` 方法）关联起来，建立**映射关系**。
- 写法：**注意**，在传统 `Servlet` 中 `@WebServlet` 必须以 `/` 开头，而 `@RequestMapping` 不需要使用 `/` 开头。
  1. **精准地址** [一个/多个]：`@RequestMapping({"user/login", "/user/register"})`
  2. **模糊地址**：`*` 表示任意一层字符串，`**` 表示任意层字符串。
     1. 比如：`/user/*`，那么 `/user/a` 可以，而 `/user/a/b` 不可以。
     2. 如果：`/user/**`，那么 `/user/a` 可以，`/user/a/b` 也可以。
- 类上和方法上添加 `@RequestMapping` 的区别：
  - **类上**：提取通用的访问地址，类似于 `@WebServlet`。
  - **方法上**：是具体的 `handler` 地址，效果相当于 `BaseController`。
  - **访问方式**：类地址 + 方法地址。
- 请求方式指定：客户端 -> `HTTP`（`GET`，`POST`）-> `DispatcherServlet` -> `handler`。
  - **默认情况下**：`@RequestMapping("login")` 只要地址正确，任意请求方式都可以。
  - 使用 `method` 属性，比如：`@RequestMapping(value = "login", method = RequestMethod.POST)`。也可以使用 `@GetMapping`、`@PostMapping` 这种注解，但是**只能放在方法上**，不能放在类上。
  - 如果不符合请求方式：会出现 `405` 异常。

### 3.2 接收参数

总结：

1. **路径设置**
   1. `@RequestMapping(value = "地址", method = "请求方式")`：可以加在**类/方法**上。
   2. 如果确定请求方式，可以直接使用 `XxxMapping` 方式，比如 `@GetMapping(value = "地址")`，只能加在**方法**上。
2. **接收参数**
   1. `param`
      1. **直接接收**：`handler(类型 形参名)`，只要**形参名 = 请求参数名**就会自动接收。
      2. **注解指定**：`handler(@RequestParam(name = "请求参数名", required = true, defaultValue = "默认值"))`
      3. **一名多值**：`handler(@RequestParam List key)`
      4. **实体接收**：`handler(实体 对象)`，要求：**对象的属性名 = 请求参数名**。
   2. **路径参数**
      1. 设置动态路径和标识：`/{key}/info/{value}`。
      2. 接收路径：`handler(@PathVariable("动态路径 key") 类型 形参名)`。
   3. `JSON`
      1. 数据接收：`handler(@RequestBody 实体类 对象)`。
      2. **准备工作**：
         1. 导入 `Jackson` 依赖。
         2. 添加 `@EnableWebMvc` 注解：
            1. 加入了 `HandlerMapping` 和 `HandlerAdapter` 到 `IoC` 容器中。
            2. 给 `HandlerAdapter` 配置了 `JSON` 处理器。
3. **`Cookie` 接收**
   1. `handler(@CookieValue("cookieName"))`
4. **请求头接收**
   1. `handler(@RequestHeader("HeaderName"))`
5. **原生 `API` 接收**
   1. `handler(HttpServletResponse response, HttpSession session)`
   2. 通过全局变量 `ServletContext`：`@Autowired ServletContext;`
6. **共享域获取**
   1. `Spring MVC` 提供了四种方式：
      1. `Model`：`addAttribute()` 方法。
      2. `ModelMap`：`addAttribute()` 方法。
      3. `Map`：`put()` 方法。
      4. `ModelAndView`：`addObject()` 方法和 `setViewName()` 方法。

### 3.2.1 `param` 和 `JSON` 参数比较

1. **参数编码**
   1. `param` 类型的参数为 `ASCII` 码，会进行转义。
   2. `JSON` 类型参数是 `UTF-8`。
2. **参数顺序**
   1. `param` 类型的参数没有顺序限制。
   2. `JSON` 类型的参数是有序的，采用键值对的方式进行传递，其中键值对是有序排列的。
3. **数据类型**
   1. `param` 仅支持字符串、数值和布尔类型等简单的数据类型。
   2. `JSON` 支持更复杂的数据类型，比如数组、对象等。
4. **嵌套性**
   1. `param`：不支持嵌套。
   2. `JSON`：支持嵌套，可以传递更为复杂的数据结构。
5. **可读性**
   1. `param` 类型的参数格式比 `JSON` 类型的参数更加简单易读。
   2. `JSON` 格式在传递嵌套数据结构时更加清晰易懂。

### 3.2.2 `param` 参数接收

1. **直接接值**：`handler` 接收参数，只要**形参名和传递参数相同**，即可自动接收。
2. `@RequestParam` 注解
   1. **使用场景**：
      - 指定绑定的请求参数名。
      - 要求请求参数必须传递。
      - 为请求参数提供默认值。
   2. **可选属性**：
      1. `value/name`：当参数名和 `param` 的名称不一致时，使用 `name/value = ""` 的方式指定请求参数名。
      2. `required`：需不需要一定传递，默认为 `true`，也就是必须传递。
      3. `defaultValue`：指定默认值。
3. **特殊场景**
   1. 可以直接使用集合进行参数的接收，但是在形参列表中**必须添加 `@RequestParam` 注解**，否则会报类型异常。
   2. 可以直接使用对象进行参数的接收，但是**对象的属性名必须与 `URL` 中传递的 `param` 一致**。

示例：

```java
@Controller
@RequestMapping("/param")
public class ParamController {
	// 直接接收
	// 形参列表填写对应名称的参数即可，要求：请求参数名 = 形参参数名
	// 1. 名称相同  2. 可以不传递，不报错
	@RequestMapping("/data")
	@ResponseBody
	public String data(String name, int age) {
		if (name == null) {
			name = "";
		}
		String result = "name = " + name + ",age = " + age;
		System.out.println(result);
		return result;
	}

	// 2. 注解指定
	// /param/data1?account=root&page=1，其中 account 必须传递，而 page 不一定传递，默认为 1
	// 指定任意的请求参数名，要求必须传递，要求不必须传递，给一个默认值
	@GetMapping("/data1")
	@ResponseBody
	public String data1(@RequestParam(value = "account") String username,
	                    @RequestParam(required = false, defaultValue = "1") int page) {
		String data1 = "account = " + username + ",page = " + page;
		System.out.println(data1);
		return data1;
	}

	// 3. 特殊情况：直接使用集合接值，必须添加 @RequestParam 注解，如果不加，会将 hbs 对应的一个字符串直接赋值给集合，导致类型异常
	// /param/data2?hbs=吃&hbs=玩&hbs=学习
	@GetMapping("/data2")
	@ResponseBody
	public String data2(@RequestParam List<String> hbs) {
		System.out.println("hbs = " + hbs);
		return "ok";
	}

	// 使用实体对象接值
	// /param/data3?username=二狗子&age=3
	@GetMapping("data3")
	@ResponseBody
	public String data3(User user) {
		System.out.println("User = " + user);
		return user.toString();
	}
}
```

### 3.2.3 路径参数接收

- **步骤**：
  1. **设计动态路径**：通过 `@RequestMapping("/{key}/{value}")` 的方式进行设置。
  2. **接收路径参数**：在形参列表中**必须使用 `@PathVariable` 注解**，具体用法和 `@RequestParam` 类似。

示例：

```java
@Controller
@RequestMapping("/path")
@ResponseBody
public class PathController {
	// /path/账号/密码
	// 动态路径的设计：{key} = *，{key} 在形参列表中获取传入的参数
	// 接收路径参数必须使用 @PathVariable
	@RequestMapping("{count}/{password}")
	public String login(@PathVariable("count") String count,
	                    @PathVariable("password") String password) {
		System.out.println("username = " + count + ",password = " + password);
		return "ok";
	}
}
```

### 3.2.4 `JSON` 数据接收

步骤：

1. 创建 `JSON` 数据对应的实体类。
2. 在 `Controller` 层中创建关键 `handler` 方法，参数使用创建的对象，**必须使用 `@RequestBody` 注解**。
3. 导入 `jackson-databind` 依赖。
4. 在配置类上加上 `@EnableWebMvc` 注解，表示：`HandlerAdapter` 配置了 `JSON` 转换器。

#### 3.2.4.1 `@EnableWebMvc` 注解

- 效果等同于在 `XML` 配置中使用 `<mvc:annotation-driven>` 元素。
- 在 `spring-framework:spring-webmvc/META-INF/spring.handlers` 文件中：

```text
http\://www.springframework.org/schema/mvc=org.springframework.web.servlet.config.MvcNamespaceHandler
```

- 而在 `MvcNamespaceHandler` 中有很多的标签，其中包括 `annotation-driven` 标签，会调用 `AnnotationDrivenBeanDefinitionParser` 这个类。
- 这个类中有 `parse` 方法，这个方法除了增加了 `JSON` 转换器，还会：
  - 创建秘书，也就是 `RequestMappingHandlerMapping`，将这个秘书变成一个 `RootBeanDefinition` 对象，并且添加到 `IoC` 容器中。
  - 创建经理，也就是 `RequestMappingHandlerAdapter`，将这个经理也变成一个 `RootBeanDefinition` 对象，并且添加到 `IoC` 容器中。
  - 通过 `addRequestBodyAdvice` 方法，给经理添加 `Jackson` 的 `JSON` 处理器。
- 所以添加 `@EnableWebMvc` 注解后，原本 `MvcConfig` 类中我们自己手动创建经理和秘书的过程就可以省略。

示例：

```java
@EnableWebMvc
@Configuration
@ComponentScan("com.study")
public class MvcConfig {

}
```

### 3.2.5 接收 `Cookie` 和请求头数据

- 可以使用 `@CookieValue` 注解将 `HTTP Cookie` 的值绑定到控制器中的方法参数。

示例：

```java
@Controller
@RequestMapping("cookie")
@ResponseBody
public class CookieController {
	@RequestMapping("data")
	public String data(@CookieValue(value = "cookieName") String value) {
		System.out.println("value = " + value);
		return value;
	}

	@GetMapping("save")
	public String save(HttpServletResponse response) {
		Cookie cookie = new Cookie("cookieName", "root");
		response.addCookie(cookie);
		return "ok";
	}
}
```

### 3.2.6 原生 `API` 对象

- **原生 `API`**：比如 `HttpServletResponse`、`HttpServletRequest`、`HttpSession`、`ServletContext` 等。

示例：

```java
@Controller
public class ApiController {
	@Autowired
	private ServletContext context;

	public void data(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		// 正常使用原生对象就可以了
		// ServletContext：1. 最大的配置文件  2. 全局最大的共享域  3. 核心 API，getRealPath
		// 方案一：通过 request、session 获取
		ServletContext context = request.getServletContext();
		ServletContext context1 = session.getServletContext();
		// 方案二：全局声明 ServletContext 对象，并且使用 @Autowired 注解即可，会自动装载到 IoC 容器中
	}
}
```

### 3.2.7 共享域

- `Spring MVC` 提供了四种方式，可以向 `request` 中存放数据。
  1. `Model`：通过 `addAttribute()` 方法存放。
  2. `ModelMap`：通过 `addAttribute()` 方法存放。
  3. `Map`：通过 `put()` 方法存放。
  4. `ModelAndView`：通过 `addObject()` 方法存放，再通过 `setViewName()` 方法设置视图名称，也就是页面的名称。

```java
@Controller
@EnableWebMvc
@ResponseBody
public class ShareController {
	@Autowired
	private ServletContext context;

	// 原生 API
	public void data(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		request.setAttribute("key", "value");
	}

	// Spring MVC 提供的方法：request 提供了几种，了解即可
	// Model、ModelMap、Map、ModelAndView
	public void data1(Model model) {
		model.addAttribute("key", "value");  // 存到 request 共享域
	}

	public void data2(ModelMap modelMap) {
		modelMap.addAttribute("key", "value");  // 存到 request 共享域
	}

	public void data3(Map map) {
		map.put("key", "value");  // 存到 request 共享域
	}

	public ModelAndView data4() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("key", "value");  // 存到 request 共享域
		mav.setViewName("视图名，页面的名称");
		return mav;
	}
}
```

## 4. 响应数据

### 4.1 页面跳转控制

#### 4.1.1 开发模式

- **前后端分离模式**：
  - **前端**：
    - `PC`：`Vue3` + `Node` + `Vite`
    - `App`：`Android`、`iOS`
    - `WeChat`
  - **后端**：
    - 三层架构。
- **混合开发模式**：
  - 三层架构：`controller`、`service`、`mapper/dao`。
  - 动态页面技术（模板页面）：`JSP/Thymeleaf`，根据 `Java` 数据动态展示界面，返回的是一个 `HTML` 文件。
  - **共享域**。

### 4.2 混合开发响应数据

- **导入依赖**：

```xml
<!-- Source: https://mvnrepository.com/artifact/jakarta.servlet.jsp/jakarta.servlet.jsp-api -->
<dependency>
    <groupId>jakarta.servlet.jsp</groupId>
    <artifactId>jakarta.servlet.jsp-api</artifactId>
    <version>4.1.0-M1</version>
    <scope>compile</scope>
</dependency>
```

- 在配置类中实现 `WebMvcConfigurer` 接口，这个接口中有很多方法，可以快速帮我们创建配置。
- **步骤**：
  1. 实现 `WebMvcConfigurer` 接口，重写 `configureViewResolvers` 方法。
  2. 使用 `registry.jsp()` 方法指定位置：
     - 底层就是字符串的拼接，这个方法帮助我们指定前后缀。比如：`registry.jsp("/WEB-INF/views", ".jsp");` 代表如果 `handler` 中返回的是 `/index`，那么访问的地址就是：`/WEB-INF/views/index.jsp`，其中只有 `/index` 是可变的，前后部分都是 `registry` 中指定的前后缀。
  3. 在 `handler` 中通过 `return` 返回一个字符串，也就是 `/WEB-INF/views` 和 `.jsp` 中间的部分。

示例：

```java
@Configuration
@ComponentScan("com.study")
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
	// 视图解析器
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		// registry 可以快速添加前后缀
		registry.jsp("/WEB-INF/views", ".jsp");
		// handler -> index
	}
}

@Controller
@RequestMapping("/jsp")
public class JspController {
	/**
	 * 快速查找视图：
	 * 1. 方法的返回值是字符串类型
	 * 2. 不能添加 @ResponseBody，直接返回字符串给浏览器；如果加了，就不找视图，不走视图解析器了
	 * 3. 返回值对应中间的视图名就可以了
	 */
	@GetMapping("index")
	public String index(HttpServletRequest request) {
		System.out.println("JspController.index");
		request.setAttribute("data", "hello jsp!!");
		return "/index";
	}
}
```

此时对应的 `JSP` 文件：

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
	<head>
		<title>Title</title>
	</head>

	<body>
		<%--request.setAttribute("data","hello jsp");--%>
		<font color="red">${data}</font>
	</body>
</html>
```

### 4.3 转发和重定向

- **转发**：只能是项目下的资源。
  1. 方法的返回值要写成字符串。
  2. 不能添加 `@ResponseBody` 注解。
  3. 在返回的字符串前添加：`forward:/转发地址`。
  4. 如果 `Tomcat` 的上下文路径（`Context`）不是 `/`，而是 `/springmvc`，那么转发地址是 `/jsp/index`，会忽略 `application context`。
- **重定向**：可以是项目外的地址。重定向属于二次请求，和转发不一样，不能忽略 `application context`。
  - 使用 `Spring MVC` 路径语法：`redirect:路径`。

```java
@Controller
@RequestMapping("/jsp")
public class JspController {
	@GetMapping("index")
	public String index(HttpServletRequest request) {
		System.out.println("JspController.index");
		request.setAttribute("data", "hello jsp!!");
		return "/index";
	}

	/**
	 * 转发：只能是项目下的资源
	 * 1. 方法的返回值要写成字符串
	 * 2. 不能添加 @ResponseBody 注解
	 * 3. 在返回的字符串前添加：forward:/转发地址
	 * 4. 如果 Tomcat 的上下文路径（Context）不是 /，而是 /springmvc，那么转发地址是 /jsp/index，忽略 application context
	 */
	@GetMapping("forward")
	public String forward() {
		return "forward:/jsp/index";
	}

	/**
	 * 重定向：可以是项目外的地址，重定向属于二次请求，和转发不一样，不能忽略 application context
	 * 使用 Spring MVC 路径语法：
	 * "redirect:路径"
	 */
	@GetMapping("redirect")
	public String redirect() {
		return "redirect:/jsp/index";
	}

	@GetMapping("redirect/baidu")
	public String redirectBaidu() {
		return "redirect:https://www.baidu.com";
	}
}
```

### 4.4 返回 `JSON` 数据

- `@RestController = @Controller + @ResponseBody`

```java
@RequestMapping("json")
// @ResponseBody       // 返回 JSON 的注解
@RestController     // 等同于 @Controller + @ResponseBody
public class JsonController {
	@GetMapping("data")
	public User data() {
		User user = new User();
		user.setAge(19);
		user.setName("二狗子");
		return user;        // user 会在 HandlerAdapter 中转换成 JSON 字符串，使用 @ResponseBody 后，JSON 会直接返回，也就是前后端分离模式
	}

	@GetMapping("data1")
	public List<User> data1() {
		List<User> list = new ArrayList<>();
		User user = new User();
		user.setAge(19);
		user.setName("二狗子");
		list.add(user);
		return list;
	}
}
```

## 5. 返回静态资源处理

- 底层使用转发实现静态资源的访问。

```java
/**
 * 告诉浏览器可以直接访问静态资源
 */
@Override
public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	configurer.enable();
}
```

## 6. `RESTful` 风格设计和实战

### 6.1 `RESTful` 简介

- **什么是 `RESTful`**
  - 是一种软件架构风格，用于设计网络应用程序和服务之间的通信。
  - 基于标准 `HTTP` 方法的简单和轻量级的通信协议，广泛应用于现代的 `Web` 服务开发。
- **`RESTful` 的风格特点**
  - 一个 `URI` 表示一种资源。
  - 客户端与服务端之间的交互在请求之间是无状态的，从客户端到服务端的每个请求都必须包含理解请求所必须的信息。
  - 资源的表现形式是：`XML` 或者 `JSON`。
  - 客户端使用 `GET`、`POST`、`PUT`、`DELETE` 4 个标识操作方式的动词对服务端资源进行操作。

示例：

```java
@RestController
@RequestMapping("/user")
public class UserController {
	/**
	 * 使用 param 形式接收数据
	 */
	@GetMapping
	public List<User> page(@RequestParam(required = false, defaultValue = "1") Integer page,
	                       @RequestParam(required = false, defaultValue = "10") Integer size) {
		List<User> list = new ArrayList<>();
		System.out.println("page = " + page + ",size = " + size);
		return list;
	}

	/**
	 * JSON 数据传递对象参数
	 */
	@PostMapping
	public User save(@RequestBody User user) {
		System.out.println("save()方法");
		return user;
	}

	/**
	 * 路径传参
	 */
	@GetMapping("{id}")
	public User detail(@PathVariable("id") Integer id) {
		System.out.println("detail()方法");
		return null;
	}

	/**
	 * JSON 数据传递对象
	 */
	@PutMapping
	public User update(@RequestBody User user) {
		System.out.println("update()方法");
		return user;
	}

	@DeleteMapping("{id}")
	public User delete(@PathVariable("id") Integer id) {
		System.out.println("delete()方法");
		return null;
	}

	@GetMapping("search")
	public List<User> search(
			@RequestParam(required = false, defaultValue = "1") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer size,
			@RequestParam(required = false) String keyword
	) {
		return null;
	}
}
```

## 7. `Spring MVC` 扩展

### 7.1 全局异常处理机制

- **异常的处理方式**：
  - **编程式异常处理**：
    - 在代码中显式地编写处理异常的逻辑，比如 `try-catch` 块。
  - **声明式异常处理**：
    - 将异常处理的逻辑从具体的业务逻辑中分离出来，通过配置等方式进行统一的管理和处理。
    - 开发人员只需要为方法或类添加相应的注解（比如：`@Throws`、`@ExceptionHandler`）就可以处理特定类型的异常。
- **步骤**：
  1. 声明异常处理控制器类，统一定义异常处理 `handler` 方法，使用 `@RestControllerAdvice` 注解。
  2. 声明异常处理 `handler` 方法，使用 `@ExceptionHandler(Exception.class)`。

示例：

```java
// @ControllerAdvice       // 可以返回逻辑视图，比如转发和重定向
@RestControllerAdvice       // @ResponseBody：直接返回 JSON 字符串
public class GlobalExceptionHandler {
	@ExceptionHandler(ArithmeticException.class)
	public Object arithmeticExceptionHandler(ArithmeticException e) {
		// 自定义处理异常即可
		return null;
	}

	@ExceptionHandler(NullPointerException.class)
	public Object nullPointerExceptionHandler(NullPointerException e) {
		// 自定义处理异常即可
		return null;
	}

	@ExceptionHandler(Exception.class)
	public Object exceptionHandler(Exception e) {
		// 自定义处理异常即可
		String message = e.getMessage();
		System.out.println("message = " + message);
		return message;
	}
}
```

### 7.2 拦截器（`HandlerInterceptor`）

- 传统的 `Filter` 处理不了 `Spring MVC` 中内部的细化流程，所以需要实现 `Spring` 提供的 `HandlerInterceptor` 接口。
- **重写的方法**：
  1. `preHandle`：执行 `handler` 之前，调用的拦截方法，比如：编码格式设置、登录保护、权限处理。
     1. `request`：请求对象，`response`：响应对象。
     2. `handler`：我们要调用的方法对象。
     3. 返回值：`true` 表示放行，`false` 表示拦截。
  2. `postHandle`：在 `handler` 执行完毕后执行。
     1. `handler`：`handler` 方法。
     2. `modelAndView`：返回的视图或者共享域数据对象。
  3. `afterCompletion`：整体处理完毕。
     1. `ex`：如果 `handler` 报错了，`ex` 就是异常对象。
- **步骤**：
  1. 创建一个类实现 `HandlerInterceptor` 接口，并重写方法（根据需求）。
  2. 还需要在配置类中配置拦截器的范围。
- **配置拦截器范围方法**
  1. **拦截全部请求**：
    - `registry.addInterceptor(new MyInterceptor())`
  
  2. **指定地址拦截**：`addPathPatterns("/user/**")`
    - `registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/data")`
  
  3. **排除拦截**：`excludePathPatterns("/user/data1")`
    - `registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/**").excludePathPatterns("/user/data1")`
  
- **多个拦截器的先后执行顺序**：先声明的优先级高，也就是**先进后出**
- 拦截器的实现：
  - DispatcherServlet在方法中先找HandlerMapping,找到之后找HandlerAdapter，使用apply.PreHandle方法触发拦截器，如果为true,再由HandlerAdapter.handler方法，如果是false，就直接return;
  - HandlerAdapter.handler方法执行完毕后，在执行applyPostHandle方法触发postHandle拦截器


示例：

```java
public class MyInterceptor implements HandlerInterceptor {
	// 执行 handler 之前调用的拦截方法
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("request = " + request);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}

@Configuration
@ComponentScan({"com.study.controller", "com.study.error"})
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 配置方案一：拦截全部请求
		registry.addInterceptor(new MyInterceptor());
		// 配置方案二：指定地址拦截：也可以使用*/**,s
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/data1");
		// 配置方案三：排除拦截
		registry.addInterceptor(new MyInterceptor()).addPathPatterns("/user/**").excludePathPatterns("/user/data");
	}
}
```

### 参数校验 JSR 303

- 使用注解的步骤：
  1. 给实体类属性添加注解
  2. 在handler(@Validated 实体类 对象)，param/json校验注解都有效果，如果是json，就再加一个@RequestBody
- 如果不符合校验规则：
  - 捕捉错误绑定错误信息：
    1. handler(校验对象，BindingResult result) ： 要求：BindingResult必须紧挨着校验对象，即参数列表中两者之间不能存在其他参数
    2. 通过bindingResult获取错误，绑定错误

|            注解            | 规则                                           |
| :------------------------: | ---------------------------------------------- |
|           @Null            | 标注值必须为 null                              |
|          @NotNull          | 标注值不可为 null                              |
|        @AssertTrue         | 标注值必须为 true                              |
|        @AssertFalse        | 标注值必须为 false                             |
|        @Min(value)         | 标注值必须大于或等于 value                     |
|        @Max(value)         | 标注值必须小于或等于 value                     |
|     @DecimalMin(value)     | 标注值必须大于或等于 value                     |
|     @DecimalMax(value)     | 标注值必须小于或等于 value                     |
|       @Size(max,min)       | 标注值大小必须在 max 和 min 限定的范围内       |
| @Digits(integer,fratction) | 标注值值必须是一个数字，且必须在可接受的范围内 |
|           @Past            | 标注值只能用于日期型，且必须是过去的日期       |
|          @Future           | 标注值只能用于日期型，且必须是将来的日期       |
|      @Pattern(value)       | 标注值必须符合指定的正则表达式                 |
|           @Email           | 标注值必须是格式正确的 Email 地址              |
|          @Length           | 标注值字符串大小必须在指定的范围内             |
|         @NotEmpty          | 标注值字符串不能是空字符串                     |
|           @Range           | 标注值必须在指定的范围内                       |

- 检查字段值是否为空：@NotNull,@NotEmpty,@NotBlank
  - @NotNull(包装类行不为null)
    - 当字段值为null时，会以为校验失败而抛出异常，不能用于字符串类型的校验
    - 若对字符串进行校验，应该使用@NotBlank/@NotEmpty注解
  - @NotEmpty(集合类型长度大于0)
    - 对于CharSequence,Collection,Map或者数组对象类型的属性进行校验，会见查该属性是否为Null或者size()==0，如果是的话就校验失败
    - 对于其他类型的属性，该注解无效
    - 注意：值校验空格前后的字符串，如果该字符串中间只有空格，不会被认为是空字符串，校验不会失败
  - @NotBlank(字符串，不为null,不为""字符串)
    - 对于祖父串类型的属性进行校验，检查该属性是否为Null或者""捉着值包含空格，
    - 只能用于字符串类型的校验

示例：

```java
@RestController
@RequestMapping("user")
public class UserController {
	//	@ResponseBody
	@PostMapping("register")
	// 接收用户数据，用户有校验注解
	public Object register(@RequestBody @Validated User user, BindingResult result) {
		if (result.hasErrors()) {
			//有绑定错误，就不直接返回了，由我们处理
			Map<String, Object> data = new HashMap<>();
			data.put("code", 400);
			data.put("msg", "参数校验异常");
			return data;
		}
		System.out.println("user = " + user);
		return user;
	}

	@GetMapping("data")
	public String data() {
		// 空指针异常
		String name = null;
		name.toString();// 空指针异常NullPointerException
		return "ok";
	}

	@GetMapping("data1")
	public String data1() {
		// 算数异常
		int i = 1 / 0;        // 算数异常：除以0ArithmeticException
		return "ok";
	}
}

```

