# Memo Web Application

一个使用 Spring Boot + Thymeleaf + PostgreSQL + JPA 构建的优美备忘录Web应用程序。

## 功能特性

- ✅ 用户注册和登录认证
- ✅ 创建、编辑、删除备忘录
- ✅ 标记重要备忘录
- ✅ 按创建时间排序
- ✅ 筛选重要备忘录
- ✅ 响应式设计，支持移动端
- ✅ 优美的现代化UI界面

## 技术栈

- **后端框架**: Spring Boot 3.2.0
- **模板引擎**: Thymeleaf
- **数据库**: PostgreSQL
- **ORM框架**: Spring Data JPA
- **安全框架**: Spring Security
- **构建工具**: Maven
- **Java版本**: 17

## 安装步骤

### 1. 前置要求

- JDK 17+
- Maven 3.6+
- PostgreSQL 12+

### 2. 数据库配置

创建 PostgreSQL 数据库:

```sql
CREATE DATABASE memodb;
```

### 3. 配置应用

编辑 `src/main/resources/application.properties` 文件，修改数据库连接信息:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/memodb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. 运行应用

```bash
# 使用 Maven 运行
mvn spring-boot:run

# 或者先打包再运行
mvn clean package
java -jar target/memo-app-1.0.0.jar
```

### 5. 访问应用

打开浏览器访问: `http://localhost:8080`

## 项目结构

```
src/
├── main/
│   ├── java/com/memo/app/
│   │   ├── MemoApplication.java          # 主应用程序类
│   │   ├── config/
│   │   │   └── SecurityConfig.java       # Spring Security配置
│   │   ├── controller/
│   │   │   ├── AuthController.java       # 认证控制器
│   │   │   ├── MemoController.java       # 备忘录控制器
│   │   │   └── HomeController.java       # 首页控制器
│   │   ├── entity/
│   │   │   ├── User.java                 # 用户实体
│   │   │   └── Memo.java                 # 备忘录实体
│   │   ├── repository/
│   │   │   ├── UserRepository.java       # 用户数据访问层
│   │   │   └── MemoRepository.java       # 备忘录数据访问层
│   │   └── service/
│   │       ├── UserService.java          # 用户服务
│   │       └── MemoService.java          # 备忘录服务
│   └── resources/
│       ├── application.properties        # 应用配置
│       ├── static/
│       │   └── css/
│       │       └── style.css             # 样式文件
│       └── templates/
│           ├── login.html                # 登录页面
│           ├── register.html             # 注册页面
│           ├── memo-list.html            # 备忘录列表页面
│           └── memo-form.html            # 备忘录表单页面
└── pom.xml                               # Maven配置文件
```

## 使用说明

1. **注册账号**: 首次使用需要在注册页面创建账号
2. **登录**: 使用注册的用户名和密码登录
3. **创建备忘录**: 点击"New Memo"按钮创建新备忘录
4. **编辑备忘录**: 点击备忘录卡片上的"Edit"按钮
5. **删除备忘录**: 点击备忘录卡片上的"Delete"按钮
6. **标记重要**: 点击星标图标标记/取消重要备忘录
7. **筛选查看**: 使用顶部的筛选按钮查看所有或仅重要备忘录

## 安全特性

- 密码使用 BCrypt 加密存储
- 使用 Spring Security 进行身份认证和授权
- 用户只能访问和操作自己的备忘录
- CSRF 保护

## 许可证

本项目仅供学习和参考使用。
