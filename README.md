# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 在此代码库中工作时提供指导。

## 项目概述

有康（YouKang）是一个基于 Spring Boot 3.5.4 和 Java 17 的快速开发框架。这是前后端分离架构的后端部分（前端使用 Vue + Element UI）。系统提供企业管理功能，包括用户管理、基于角色的访问控制（RBAC）、定时任务、代码生成和系统监控。

**版本：** 3.9.0
**主入口：** `youkang-admin/src/main/java/com/youkang/YouKangApplication.java`
**默认端口：** 3564

## 构建和运行命令

### 构建项目

```bash
mvn clean package
```

### 运行应用

```bash
# 从根目录运行
mvn spring-boot:run -pl youkang-admin

# 或者运行编译后的 JAR
java -jar youkang-admin/target/youkang-admin.jar
```

### 跳过测试编译

```bash
mvn clean install -DskipTests
```

### 清理构建产物

```bash
mvn clean
```

## 环境变量配置

为了方便本地开发和不同环境的部署，项目已支持通过环境变量覆盖核心配置。你可以通过 `.env` 文件（需要手动加载）或命令行参数进行配置。

### 可用变量

> 提示：以下默认值即为当前代码库中的默认配置。

| 变量名           | 描述             | 默认值                     | 示例                                     |
| :--------------- | :--------------- | :------------------------- | :--------------------------------------- |
| `REDIS_HOST`     | Redis 服务器地址 | `124.222.41.12`            | `localhost`                              |
| `REDIS_PORT`     | Redis 服务器端口 | `6379`                     | `6379`                                   |
| `REDIS_PASSWORD` | Redis 密码       | `redis_bEDNjE`             | `your_secure_password`                   |
| `REDIS_DB`       | Redis 数据库索引 | `0`                        | `1`                                      |
| `DB_URL`         | MySQL 连接地址   | (见 application-druid.yml) | `jdbc:mysql://localhost:3306/youkang...` |
| `DB_USERNAME`    | MySQL 用户名     | `root`                     | `admin`                                  |
| `DB_PASSWORD`    | MySQL 密码       | `mysql_knNCfM`             | `123456`                                 |
| `UPLOAD_PATH`    | 文件上传路径     | `D:/youkang/uploadPath`    | `/home/youkang/files`                    |

### 使用方式

**方式 1：行内环境变量（临时生效）**

适用于单次测试或临时覆盖：

```bash
# 覆盖 Redis 密码并启动应用
REDIS_PASSWORD=123 mvn spring-boot:run -pl youkang-admin

# 或者运行编译后的 JAR
REDIS_PASSWORD=123 java -jar youkang-admin/target/youkang-admin.jar

# 或者使用 ry.sh 脚本
REDIS_PASSWORD=123 ./ry.sh start
```

## 模块架构

这是一个多模块 Maven 项目，具有以下结构：

### youkang-admin

Web 服务入口和主应用模块。包含：

- 按领域组织的 REST 控制器（`system`、`monitor`、`tool`、`common`）
- 包含 Spring Boot 配置的主应用类
- 应用配置文件（`application.yml`、`application-druid.yml`）
- 打包为可执行 JAR

### youkang-framework

核心框架模块，提供基础设施服务：

- **安全：** Spring Security 配置、JWT 认证过滤器、令牌服务
- **AOP 切面：** 日志记录（`LogAspect`）、数据权限过滤（`DataScopeAspect`）、数据源切换（`DataSourceAspect`）、限流（`RateLimiterAspect`）
- **配置：** MyBatis、Druid、Redis、线程池、Kaptcha（验证码）
- **Web 服务：** 认证、权限检查、用户注册
- **异常处理：** 全局异常处理器
- **动态数据源：** 通过 `DynamicDataSource` 实现主从数据库路由

### youkang-system

核心系统功能的业务逻辑和领域层：

- 领域实体：`SysUser`、`SysRole`、`SysMenu`、`SysDept`、`SysPost`、`SysDictData`、`SysDictType`、`SysConfig`、`SysNotice`、`SysOperLog`、`SysLogininfor`
- RBAC 的服务接口和实现
- 数据库操作的 MyBatis 映射器
- 处理用户管理、角色/权限管理、部门层次结构、数据字典、系统配置、审计日志

### youkang-common

共享工具和通用组件：

- **注解：** `@Log`、`@DataScope`、`@DataSource`、`@Excel`、`@RateLimiter`、`@RepeatSubmit`、`@Sensitive`、`@Anonymous`
- **核心领域：** `BaseEntity`、`AjaxResult`、`R`、`TableDataInfo`
- **工具类：** 字符串、日期、HTTP、文件、安全、IP、Bean、Excel/POI、UUID 生成
- **过滤器：** XSS 防护、Referer 验证、可重复请求包装器
- **常量：** 用户、缓存、调度、HTTP 状态码
- **异常：** 业务逻辑的自定义异常层次结构

### youkang-quartz

使用 Quartz Scheduler 的定时任务管理模块：

- 支持 CRUD 操作的动态任务调度
- 任务执行日志和监控
- 支持 cron 表达式实现灵活调度

### youkang-generator

用于快速 CRUD 开发的代码生成器：

- 生成 Java 实体、映射器、服务、控制器代码
- 生成 MyBatis XML 映射器
- 使用 Velocity 模板进行代码生成
- 可以生成前端 Vue 代码（下载为 ZIP）
- 检查数据库表以生成样板代码

## 关键配置

### 数据库配置

位于 `youkang-admin/src/main/resources/application-druid.yml`：

- 支持主从数据源配置
- 使用 Druid 连接池
- MySQL 8.x，时区 GMT+8
- Druid 监控控制台：`/druid/*`（用户名：`youkang`，密码：`123456`）

### 安全与认证

- 基于 JWT 的认证（请求头：`Authorization`）
- 令牌过期时间：30 分钟（可在 `application.yml` 中配置）
- 密码重试限制：5 次尝试，锁定 10 分钟
- 使用 Spring Security 进行授权

### Redis 配置

- 默认：`localhost:6379`，数据库 0
- 用于缓存用户会话、令牌、权限、字典数据

### MyBatis

- 实体别名包：`com.youkang.**.domain`
- 映射器位置：`classpath*:mapper/**/*Mapper.xml`
- 配置：`mybatis/mybatis-config.xml`
- 分页：使用 MySQL 方言的 PageHelper

### API 文档

- 使用 SpringDoc（OpenAPI 3.0）
- Swagger UI：`http://localhost:3564/swagger-ui.html`
- API 文档 JSON：`http://localhost:3564/v3/api-docs`

## 开发模式

### 控制器模式

控制器继承 `BaseController` 并使用标准响应类型：

- `AjaxResult.success()` / `AjaxResult.error()` 用于单个操作
- `TableDataInfo` 用于分页列表响应
- 使用 `@PreAuthorize("@ss.hasPermi('...')")` 进行权限检查
- 使用 `@Log(title = "...", businessType = BusinessType.XXX)` 进行审计日志记录

### 服务层模式

- 服务实现接口（例如 `ISysUserService`）
- 使用 `@DataScope` 注解进行数据权限过滤
- 事务由 Spring `@Transactional` 管理

### 动态数据源

使用 `@DataSource(DataSourceType.SLAVE)` 将查询路由到从库（配置后）。

### 代码生成

通过系统菜单"系统工具 > 代码生成"访问：

1. 导入数据库表
2. 配置生成参数（模块名、包名等）
3. 生成并下载代码
4. 将生成的文件导入项目

### 权限表达式

框架提供 `PermissionService`（`ss` bean）用于权限检查：

- `@ss.hasPermi('system:user:add')` - 检查权限
- `@ss.lacksPermi('system:user:remove')` - 缺少权限
- `@ss.hasRole('admin')` - 检查角色
- `@ss.hasAnyRoles('admin,editor')` - 检查任意角色

## 常用注解

- `@Log`：记录操作日志（标题、业务类型、操作人类型）
- `@DataScope`：根据用户的数据权限范围过滤数据
- `@DataSource`：在主从数据源之间切换
- `@RateLimiter`：API 端点的限流
- `@RepeatSubmit`：防止重复表单提交
- `@Anonymous`：绕过公共端点的身份验证
- `@Excel`：标记用于 Excel 导入/导出的字段

## 文件上传

- 上传路径在 `application.yml` 中配置：`youkang.profile`
- 默认：`D:/youkang/uploadPath`（Windows），`/home/youkang/uploadPath`（Linux）
- 最大文件大小：10MB
- 最大请求大小：20MB

## 重要说明

### 安全注意事项

- 数据库凭据暴露在 `application-druid.yml` 中 - 生产环境使用环境变量
- 生产部署应更改令牌密钥
- XSS 过滤默认启用（`xss.enabled: true`）
- Referer 验证可用但默认禁用

### 数据权限过滤

`@DataScope` 注解基于用户的部门和角色启用行级安全。这在 `DataScopeAspect` 中实现，并根据以下条件注入 SQL 条件：

- 全部数据权限（1）
- 自定义部门权限（2）
- 部门数据权限（3）
- 部门及以下数据权限（4）
- 仅本人数据权限（5）

### 日志记录

- 操作日志存储在 `sys_oper_log` 表中
- 登录日志存储在 `sys_logininfor` 表中
- 通过 `AsyncManager` 和 `AsyncFactory` 进行异步日志记录

## 无测试套件

此项目不包括测试套件。通过应用程序 UI 和 API 端点进行手动测试。
