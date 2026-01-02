# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

有康（YouKang）是一个基于 Spring Boot 3.5.4 和 Java 17 的快速开发框架。这是前后端分离架构的后端部分（前端使用 Vue + Element UI）。系统提供企业管理功能，包括用户管理、基于角色的访问控制（RBAC）、定时任务、代码生成和系统监控。

**版本：** 3.9.0
**主入口：** `youkang-admin/src/main/java/com/youkang/YouKangApplication.java`
**默认端口：** 3564

## 构建和运行命令

```bash
# 构建项目
mvn clean package

# 运行应用
mvn spring-boot:run -pl youkang-admin

# 或者运行编译后的 JAR
java -jar youkang-admin/target/youkang-admin.jar

# 跳过测试编译
mvn clean install -DskipTests

# 单模块快速编译（开发时使用）
mvn compile -pl youkang-admin -am
```

## 环境变量配置

| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| `REDIS_HOST` | Redis 服务器地址 | `124.222.41.12` |
| `REDIS_PORT` | Redis 服务器端口 | `6379` |
| `REDIS_PASSWORD` | Redis 密码 | (见 application.yml) |
| `REDIS_DB` | Redis 数据库索引 | `0` |
| `DB_URL` | MySQL 连接地址 | (见 application-druid.yml) |
| `DB_USERNAME` | MySQL 用户名 | `root` |
| `DB_PASSWORD` | MySQL 密码 | (见 application-druid.yml) |
| `UPLOAD_PATH` | 文件上传路径 | `D:/youkang/uploadPath` |

使用示例：`REDIS_PASSWORD=xxx mvn spring-boot:run -pl youkang-admin`

## 数据库初始化

SQL 脚本位于 `/sql` 目录：
- `youkang.sql` - 主系统表结构和初始数据
- `quartz.sql` - Quartz 定时任务表
- `sys_file_info.sql` - 文件信息表

## 模块架构

多模块 Maven 项目，模块依赖关系：`youkang-admin` → `youkang-framework` → `youkang-system` → `youkang-common`

| 模块 | 职责 |
|------|------|
| **youkang-admin** | Web 入口，REST 控制器（按 `system`/`monitor`/`tool`/`common` 领域组织），应用配置 |
| **youkang-framework** | 安全（JWT、Spring Security）、AOP 切面（日志/数据权限/数据源/限流）、全局异常处理、动态数据源 |
| **youkang-system** | 业务逻辑层，领域实体（SysUser/SysRole/SysMenu/SysDept 等），MyBatis 映射器 |
| **youkang-common** | 共享注解、工具类、BaseEntity、AjaxResult、TableDataInfo、自定义异常 |
| **youkang-quartz** | Quartz 定时任务调度和执行日志 |
| **youkang-generator** | 代码生成器（Velocity 模板），生成 CRUD 代码和 Vue 前端 |

## 关键配置

配置文件位于 `youkang-admin/src/main/resources/`：
- `application.yml` - 主配置（端口 3564、Redis、MyBatis、token）
- `application-druid.yml` - 数据源配置（MySQL 8.x、Druid 连接池）

| 配置项 | 说明 |
|--------|------|
| JWT 认证 | 请求头 `Authorization`，有效期 30 分钟 |
| Redis | 缓存会话/令牌/权限 |
| MyBatis 映射器 | `classpath*:mapper/**/*Mapper.xml` |
| Swagger UI | `http://localhost:3564/swagger-ui.html` |
| Druid 监控 | `/druid/*`（用户名：`youkang`，密码：`123456`）|

## 开发模式

### 控制器模式
控制器继承 `BaseController`：
```java
@RestController
@RequestMapping("/system/xxx")
public class XxxController extends BaseController {
    @PreAuthorize("@ss.hasPermi('system:xxx:list')")  // 权限检查
    @Log(title = "Xxx管理", businessType = BusinessType.QUERY)  // 审计日志
    @GetMapping("/list")
    public TableDataInfo list(Xxx xxx) {
        startPage();  // 分页
        List<Xxx> list = xxxService.selectXxxList(xxx);
        return getDataTable(list);  // 返回分页数据
    }

    @PostMapping
    public AjaxResult add(@RequestBody Xxx xxx) {
        return toAjax(xxxService.insertXxx(xxx));  // 返回操作结果
    }
}
```

### 权限表达式（`ss` bean）
- `@ss.hasPermi('system:user:add')` - 检查权限
- `@ss.hasRole('admin')` - 检查角色
- `@ss.hasAnyRoles('admin,editor')` - 任意角色

### 常用注解
| 注解 | 用途 |
|------|------|
| `@Log` | 记录操作日志 |
| `@DataScope` | 数据权限过滤（行级安全）|
| `@DataSource` | 切换主从数据源 |
| `@RateLimiter` | API 限流 |
| `@RepeatSubmit` | 防重复提交 |
| `@Anonymous` | 跳过认证 |
| `@Excel` | Excel 导入导出字段标记 |

## 重要说明

### 数据权限过滤
`@DataScope` 注解在 `DataScopeAspect` 中实现行级安全，根据角色数据权限范围（1-全部/2-自定义部门/3-本部门/4-本部门及以下/5-仅本人）自动注入 SQL 条件。

### 日志记录
- 操作日志 → `sys_oper_log` 表
- 登录日志 → `sys_logininfor` 表
- 异步记录：`AsyncManager` + `AsyncFactory`

### 文件上传
路径配置：`youkang.profile`（默认 `D:/youkang/uploadPath`），最大 10MB/文件。

## 无测试套件
此项目不包括测试套件。通过 API 端点进行手动测试。
