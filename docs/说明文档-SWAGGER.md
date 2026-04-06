# SpringDoc OpenAPI 3.0（Swagger 3）使用说明

## 一、项目集成情况

✅ 你的项目已经成功集成了 **SpringDoc OpenAPI 3.0**（Swagger 3 的官方推荐实现）

### 当前版本信息
- **SpringDoc 版本**：2.8.9（最新稳定版）
- **OpenAPI 规范**：3.0
- **Spring Boot 版本**：3.5.4

### 依赖配置位置
- **父 POM**：`pom.xml` 中定义版本 `<springdoc.version>2.8.9</springdoc.version>`
- **模块依赖**：`youkang-admin/pom.xml` 中引入依赖

---

## 二、版本说明：Springfox vs SpringDoc

### 你可能习惯使用的旧版注解

如果你之前使用过 `@Api`、`@ApiOperation`、`@ApiModelProperty` 这些注解，那是 **Springfox Swagger 2.x**（旧版）。

#### Springfox Swagger 2.x（旧版，已停止维护）

| 特性 | 说明 |
|-----|------|
| **核心依赖** | `springfox-swagger2` + `springfox-swagger-ui` |
| **最后版本** | 2.9.2（2020年停止维护） |
| **规范** | Swagger 2.0 |
| **注解包** | `io.swagger.annotations.*` |
| **Spring Boot 兼容** | ⚠️ 只支持 Spring Boot 2.x |
| **状态** | ❌ 已停止维护，不支持 Spring Boot 3.x |

**旧版常用注解：**
```java
import io.swagger.annotations.*;

@Api(tags = "用户管理")                          // Controller 类
@ApiOperation(value = "获取用户列表")              // 接口方法
@ApiModel(description = "用户实体")              // 实体类
@ApiModelProperty(value = "用户ID")             // 实体字段
@ApiParam(value = "用户ID")                     // 方法参数
@ApiImplicitParam                               // 隐式参数
@ApiImplicitParams                              // 多个隐式参数
@ApiIgnore                                      // 隐藏接口
```

---

#### SpringDoc OpenAPI 3.x（新版，当前使用）

| 特性 | 说明 |
|-----|------|
| **核心依赖** | `springdoc-openapi-starter-webmvc-ui` |
| **当前版本** | 2.8.9（持续更新中） |
| **规范** | OpenAPI 3.0 |
| **注解包** | `io.swagger.v3.oas.annotations.*` |
| **Spring Boot 兼容** | ✅ 完美支持 Spring Boot 3.x |
| **状态** | ✅ 持续维护，Spring 官方推荐 |

**新版常用注解：**
```java
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "用户管理")                          // Controller 类
@Operation(summary = "获取用户列表")              // 接口方法
@Schema(description = "用户实体")                // 实体类
@Schema(description = "用户ID")                  // 实体字段
@Parameter(description = "用户ID")               // 方法参数
@Parameters                                      // 多个参数
@Hidden                                          // 隐藏接口
```

---

### 为什么项目使用 SpringDoc？

**核心原因**：你的项目使用 **Spring Boot 3.5.4**，而 Springfox 不支持 Spring Boot 3.x。

Spring Boot 3.x 的重大变化：
- 从 `javax.*` 迁移到 `jakarta.*`
- Springfox 没有适配这个变化（已停止维护）
- SpringDoc 是 Spring 官方推荐的替代方案

**SpringDoc 的优势**：
- ✅ 完整支持 OpenAPI 3.0 规范
- ✅ 原生支持 Spring Boot 3.x
- ✅ 注解更简洁易用
- ✅ 持续更新维护
- ✅ 性能更好
- ✅ 功能更强大

---

### 完整注解对照表

| 用途 | Springfox (旧) | SpringDoc (新) |
|-----|---------------|---------------|
| **Controller 类** | `@Api(tags = "xxx")` | `@Tag(name = "xxx")` |
| **接口方法** | `@ApiOperation(value = "xxx")` | `@Operation(summary = "xxx")` |
| **接口方法描述** | `@ApiOperation(value = "标题", notes = "详情")` | `@Operation(summary = "标题", description = "详情")` |
| **实体类** | `@ApiModel(description = "xxx")` | `@Schema(description = "xxx")` |
| **实体字段** | `@ApiModelProperty(value = "xxx")` | `@Schema(description = "xxx")` |
| **字段必填** | `@ApiModelProperty(required = true)` | `@Schema(required = true)` |
| **字段示例** | `@ApiModelProperty(example = "xxx")` | `@Schema(example = "xxx")` |
| **方法参数** | `@ApiParam(value = "xxx")` | `@Parameter(description = "xxx")` |
| **隐式参数（单个）** | `@ApiImplicitParam` | `@Parameter` |
| **隐式参数（多个）** | `@ApiImplicitParams` | `@Parameters` |
| **请求体** | `@ApiParam` + `@RequestBody` | `@RequestBody` |
| **隐藏接口/参数** | `@ApiIgnore` | `@Hidden` |
| **响应描述** | `@ApiResponse` | `@ApiResponse`（相同） |
| **多响应描述** | `@ApiResponses` | `@ApiResponses`（相同） |

---

### 代码对比示例

#### Controller 对比

**旧版 Springfox 写法：**
```java
import io.swagger.annotations.*;

@Api(tags = "客户管理", description = "客户信息管理接口")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @ApiOperation(value = "查询客户列表", notes = "分页查询客户信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "pageSize", value = "每页数量", dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "customerName", value = "客户名称", dataType = "string", paramType = "query")
    })
    @GetMapping("/list")
    public TableDataInfo list(CustomerInfo customerInfo) {
        // ...
    }

    @ApiOperation(value = "获取客户详情", notes = "根据客户ID查询详细信息")
    @ApiImplicitParam(name = "id", value = "客户ID", required = true, dataType = "int", paramType = "path")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        // ...
    }

    @ApiOperation(value = "新增客户", notes = "添加新的客户信息")
    @PostMapping
    public AjaxResult add(@RequestBody CustomerInfo customerInfo) {
        // ...
    }
}
```

**新版 SpringDoc 写法：**
```java
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "客户管理", description = "客户信息管理接口")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Operation(summary = "查询客户列表", description = "分页查询客户信息")
    @Parameters({
        @Parameter(name = "pageNum", description = "页码"),
        @Parameter(name = "pageSize", description = "每页数量"),
        @Parameter(name = "customerName", description = "客户名称")
    })
    @GetMapping("/list")
    public TableDataInfo list(CustomerInfo customerInfo) {
        // ...
    }

    @Operation(summary = "获取客户详情", description = "根据客户ID查询详细信息")
    @Parameter(name = "id", description = "客户ID", required = true)
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Integer id) {
        // ...
    }

    @Operation(summary = "新增客户", description = "添加新的客户信息")
    @PostMapping
    public AjaxResult add(@RequestBody CustomerInfo customerInfo) {
        // ...
    }
}
```

---

#### 实体类对比

**旧版 Springfox 写法：**
```java
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "客户信息实体")
public class CustomerInfo {

    @ApiModelProperty(value = "客户ID", example = "1")
    private Integer id;

    @ApiModelProperty(value = "客户名称", required = true, example = "张三公司")
    private String customerName;

    @ApiModelProperty(value = "联系电话", example = "13888888888")
    private String phone;

    @ApiModelProperty(value = "客户等级", example = "A", allowableValues = "A,B,C")
    private String customerLevel;

    @ApiModelProperty(value = "状态", example = "0", allowableValues = "0,1")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remarks;

    // getter/setter...
}
```

**新版 SpringDoc 写法：**
```java
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "客户信息实体")
public class CustomerInfo {

    @Schema(description = "客户ID", example = "1")
    private Integer id;

    @Schema(description = "客户名称", required = true, example = "张三公司")
    private String customerName;

    @Schema(description = "联系电话", example = "13888888888")
    private String phone;

    @Schema(description = "客户等级", example = "A", allowableValues = {"A", "B", "C"})
    private String customerLevel;

    @Schema(description = "状态", example = "0", allowableValues = {"0", "1"})
    private String status;

    @Schema(description = "备注")
    private String remarks;

    // getter/setter...
}
```

---

### 快速迁移指南

如果你要从旧项目迁移代码到新项目，按照以下步骤替换：

#### 1. 替换 Import 包

```java
// ❌ 删除旧的 import
import io.swagger.annotations.*;

// ✅ 添加新的 import
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
```

#### 2. 替换 Controller 类注解

```java
// ❌ 旧的
@Api(tags = "用户管理", description = "用户相关接口")

// ✅ 新的
@Tag(name = "用户管理", description = "用户相关接口")
```

#### 3. 替换方法注解

```java
// ❌ 旧的
@ApiOperation(value = "获取用户", notes = "根据ID获取用户信息")

// ✅ 新的
@Operation(summary = "获取用户", description = "根据ID获取用户信息")
```

#### 4. 替换实体类注解

```java
// ❌ 旧的
@ApiModel(description = "用户实体")
public class User {
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "用户名", required = true)
    private String name;
}

// ✅ 新的
@Schema(description = "用户实体")
public class User {
    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", required = true)
    private String name;
}
```

#### 5. 替换参数注解

```java
// ❌ 旧的
@ApiParam(value = "用户ID", required = true, example = "1")
@PathVariable Long id

// ✅ 新的
@Parameter(description = "用户ID", required = true, example = "1")
@PathVariable Long id
```

#### 6. 替换隐式参数注解

```java
// ❌ 旧的
@ApiImplicitParams({
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "long"),
    @ApiImplicitParam(name = "name", value = "用户名", dataType = "string")
})

// ✅ 新的
@Parameters({
    @Parameter(name = "id", description = "用户ID", required = true),
    @Parameter(name = "name", description = "用户名")
})
```

#### 7. 替换隐藏注解

```java
// ❌ 旧的
@ApiIgnore

// ✅ 新的
@Hidden
```

---

### 迁移小技巧

**批量替换（适用于 IDEA 或 VSCode）：**

1. **Controller 类**：`@Api(tags =` → `@Tag(name =`
2. **方法**：`@ApiOperation(value =` → `@Operation(summary =`
3. **方法描述**：`, notes =` → `, description =`
4. **实体类**：`@ApiModel(` → `@Schema(`
5. **字段**：`@ApiModelProperty(value =` → `@Schema(description =`
6. **参数**：`@ApiParam(value =` → `@Parameter(description =`
7. **隐藏**：`@ApiIgnore` → `@Hidden`
8. **Import**：`io.swagger.annotations` → `io.swagger.v3.oas.annotations`

---

## 三、访问 Swagger UI

### 1. 启动应用
```bash
mvn spring-boot:run -pl youkang-admin
```

### 2. 访问地址
启动后访问以下地址：

| 功能 | 访问地址 | 说明 |
|-----|---------|------|
| **Swagger UI** | http://localhost:3564/swagger-ui.html | API 文档可视化界面（推荐） |
| **Swagger UI（备用）** | http://localhost:3564/swagger-ui/index.html | 另一个访问路径 |
| **OpenAPI JSON** | http://localhost:3564/v3/api-docs | API 文档 JSON 格式 |

> **注意**：端口号根据 `application.yml` 中的 `server.port` 配置，默认是 3564

### 3. 认证方式
项目已配置 JWT Token 认证：
1. 打开 Swagger UI
2. 点击右上角的 **Authorize** 按钮
3. 输入 JWT Token（格式：`Bearer your-token-here` 或直接输入 `your-token-here`）
4. 点击 **Authorize** 按钮完成认证
5. 现在可以测试需要认证的接口了

---

## 四、配置说明

### 1. application.yml 配置

位置：`youkang-admin/src/main/resources/application.yml`

```yaml
# Springdoc配置
springdoc:
  api-docs:
    # OpenAPI JSON 文档路径
    path: /v3/api-docs
  swagger-ui:
    # 是否启用 Swagger UI
    enabled: true
    # Swagger UI 访问路径
    path: /swagger-ui.html
    # 标签排序方式：alpha（字母排序）
    tags-sorter: alpha
  # 分组配置
  group-configs:
    - group: 'default'
      display-name: '测试模块'
      paths-to-match: '/**'
      packages-to-scan: com.youkang.web.controller.tool
```

### 2. SwaggerConfig.java 配置类

位置：`youkang-admin/src/main/java/com/youkang/web/core/config/SwaggerConfig.java`

```java
@Configuration
public class SwaggerConfig {

    @Autowired
    private YouKangConfig youkangConfig;

    /**
     * 自定义的 OpenAPI 对象
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
            .components(new Components()
                // 设置认证的请求头
                .addSecuritySchemes("apikey", securityScheme()))
            .addSecurityItem(new SecurityRequirement().addList("apikey"))
            .info(getApiInfo());
    }

    @Bean
    public SecurityScheme securityScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .name("Authorization")
            .in(SecurityScheme.In.HEADER)
            .scheme("Bearer");
    }

    /**
     * 添加摘要信息
     */
    public Info getApiInfo() {
        return new Info()
            .title("标题：有康管理系统_接口文档")
            .description("描述：用于管理集团旗下公司的人员信息,具体包括XXX,XXX模块...")
            .contact(new Contact().name(youkangConfig.getName()))
            .version("版本号:" + youkangConfig.getVersion());
    }
}
```

### 3. 配置多个 API 分组（可选）

如果你想将 API 按模块分组显示，可以修改 `application.yml`：

```yaml
springdoc:
  group-configs:
    - group: '系统管理'
      display-name: '系统管理模块'
      paths-to-match: '/system/**'
      packages-to-scan: com.youkang.web.controller.system

    - group: '客户管理'
      display-name: '客户管理模块'
      paths-to-match: '/customer/**'
      packages-to-scan: com.youkang.web.controller.customer

    - group: '监控管理'
      display-name: '监控管理模块'
      paths-to-match: '/monitor/**'
      packages-to-scan: com.youkang.web.controller.monitor

    - group: '工具模块'
      display-name: '工具测试模块'
      paths-to-match: '/tool/**'
      packages-to-scan: com.youkang.web.controller.tool
```

---

## 五、注解使用说明

### 1. Controller 类级别注解

#### @Tag - 标记 Controller 分组

```java
@Tag(name = "用户管理", description = "用户信息的增删改查接口")
@RestController
@RequestMapping("/system/user")
public class UserController {
    // ...
}
```

**参数说明**：
- `name`：API 分组名称（必填）
- `description`：分组描述（可选）

### 2. 方法级别注解

#### @Operation - 描述接口方法

```java
@Operation(summary = "获取用户列表", description = "分页查询用户信息列表")
@GetMapping("/list")
public TableDataInfo list(SysUser user) {
    // ...
}
```

**参数说明**：
- `summary`：接口简短描述（必填，显示在列表）
- `description`：接口详细描述（可选）
- `tags`：接口所属标签（可选）

#### @Parameters 和 @Parameter - 描述请求参数

```java
@Operation(summary = "根据ID查询用户")
@GetMapping("/{userId}")
public AjaxResult getInfo(
    @Parameter(description = "用户ID", required = true, example = "1")
    @PathVariable("userId") Long userId
) {
    // ...
}
```

**@Parameter 参数说明**：
- `name`：参数名称
- `description`：参数描述
- `required`：是否必填（true/false）
- `example`：参数示例值
- `schema`：参数类型定义

#### 多个参数示例

```java
@Operation(summary = "用户登录")
@Parameters({
    @Parameter(name = "username", description = "用户名", required = true, example = "admin"),
    @Parameter(name = "password", description = "密码", required = true, example = "admin123")
})
@PostMapping("/login")
public AjaxResult login(
    @RequestParam String username,
    @RequestParam String password
) {
    // ...
}
```

#### @ApiResponse/@ApiResponses - 描述响应结果

```java
@Operation(summary = "新增用户")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "操作成功"),
    @ApiResponse(responseCode = "400", description = "参数错误"),
    @ApiResponse(responseCode = "500", description = "服务器错误")
})
@PostMapping
public AjaxResult add(@RequestBody SysUser user) {
    // ...
}
```

### 3. 实体类注解

#### @Schema - 描述实体类和字段

**类级别**：
```java
@Schema(description = "用户实体")
public class SysUser {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户名", example = "admin", required = true)
    private String userName;

    @Schema(description = "用户昵称", example = "管理员")
    private String nickName;

    @Schema(description = "邮箱", example = "admin@youkang.com")
    private String email;

    @Schema(description = "手机号", example = "13888888888")
    private String phonenumber;

    @Schema(description = "用户状态", example = "0", allowableValues = {"0", "1"})
    private String status;

    // getter/setter...
}
```

**@Schema 参数说明**：
- `description`：字段描述
- `example`：示例值
- `required`：是否必填
- `allowableValues`：允许的值（枚举）
- `hidden`：是否在文档中隐藏
- `title`：标题（同 description）
- `defaultValue`：默认值

#### @Schema 高级用法

```java
@Schema(description = "用户查询对象")
public class UserQuery {

    @Schema(description = "用户名（支持模糊查询）", example = "admin")
    private String userName;

    @Schema(description = "状态", example = "0",
            allowableValues = {"0", "1"},
            defaultValue = "0")
    private String status;

    @Schema(description = "创建时间范围-开始", example = "2025-01-01",
            type = "string", format = "date")
    private Date beginTime;

    @Schema(description = "创建时间范围-结束", example = "2025-12-31",
            type = "string", format = "date")
    private Date endTime;

    // getter/setter...
}
```

### 4. 其他常用注解

#### @Hidden - 隐藏接口或参数

```java
// 隐藏整个接口（不在 Swagger 中显示）
@Hidden
@GetMapping("/internal")
public AjaxResult internalApi() {
    // ...
}

// 隐藏某个参数
public AjaxResult query(
    @Hidden @RequestParam String internalParam,
    @RequestParam String publicParam
) {
    // ...
}
```

#### @RequestBody - 标记请求体参数

```java
@Operation(summary = "新增用户")
@PostMapping
public AjaxResult add(
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "用户信息",
        required = true
    )
    @RequestBody SysUser user
) {
    // ...
}
```

---

## 六、完整示例

### 示例 1：简单的 CRUD 接口

```java
package com.youkang.web.controller.customer;

import com.youkang.common.core.controller.BaseController;
import com.youkang.common.core.domain.AjaxResult;
import com.youkang.common.core.page.TableDataInfo;
import com.youkang.system.domain.CustomerInfo;
import com.youkang.system.service.customer.ICustomerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 客户管理 Controller
 */
@Tag(name = "客户管理", description = "客户信息的增删改查接口")
@RestController
@RequestMapping("/customer/info")
public class CustomerInfoController extends BaseController {

    @Resource
    private ICustomerInfoService customerInfoService;

    /**
     * 查询客户列表
     */
    @Operation(summary = "查询客户列表", description = "分页查询客户信息列表")
    @GetMapping("/list")
    public TableDataInfo list(CustomerInfo customerInfo) {
        startPage();
        return getDataTable(customerInfoService.queryPage(customerInfo).getRecords());
    }

    /**
     * 获取客户详细信息
     */
    @Operation(summary = "获取客户详细信息", description = "根据客户ID查询详细信息")
    @Parameter(name = "id", description = "客户ID", required = true, example = "1")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return success(customerInfoService.getById(id));
    }

    /**
     * 新增客户
     */
    @Operation(summary = "新增客户", description = "添加新的客户信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "500", description = "操作失败")
    })
    @PostMapping
    public AjaxResult add(@RequestBody CustomerInfo customerInfo) {
        return toAjax(customerInfoService.save(customerInfo));
    }

    /**
     * 修改客户
     */
    @Operation(summary = "修改客户", description = "更新客户信息")
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerInfo customerInfo) {
        return toAjax(customerInfoService.updateById(customerInfo));
    }

    /**
     * 删除客户
     */
    @Operation(summary = "删除客户", description = "根据ID删除客户")
    @Parameters({
        @Parameter(name = "ids", description = "客户ID数组", required = true, example = "[1,2,3]")
    })
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(customerInfoService.removeBatchByIds(Arrays.asList(ids)));
    }
}
```

### 示例 2：带查询条件的接口

```java
@Tag(name = "课题组管理", description = "课题组信息管理接口")
@RestController
@RequestMapping("/customer/subject-group")
public class SubjectGroupController extends BaseController {

    @Resource
    private ISubjectGroupInfoService subjectGroupInfoService;

    /**
     * 课题组选择器（搜索下拉）
     */
    @Operation(
        summary = "课题组选择器",
        description = "用于下拉选择框，支持按ID或名称模糊搜索"
    )
    @Parameter(
        name = "queryString",
        description = "搜索关键字（可搜索ID或名称）",
        required = false,
        example = "测试"
    )
    @GetMapping("/selector")
    public AjaxResult selector(@RequestParam(required = false) String queryString) {
        return success(subjectGroupInfoService.getSubjectGroupSelector(queryString));
    }

    /**
     * 分页查询课题组列表
     */
    @Operation(summary = "分页查询课题组列表")
    @GetMapping("/list")
    public TableDataInfo list(SubjectGroupInfo subjectGroupInfo) {
        startPage();
        return getDataTable(subjectGroupInfoService.queryPage(subjectGroupInfo).getRecords());
    }
}
```

### 示例 3：文件上传接口

```java
@Tag(name = "文件管理", description = "文件上传下载接口")
@RestController
@RequestMapping("/common/file")
public class FileController {

    @Operation(summary = "文件上传", description = "上传单个文件")
    @Parameters({
        @Parameter(name = "file", description = "上传的文件", required = true)
    })
    @PostMapping("/upload")
    public AjaxResult upload(
        @Parameter(description = "文件", required = true)
        @RequestParam("file") MultipartFile file
    ) {
        // 文件上传逻辑
        return AjaxResult.success("上传成功");
    }
}
```

### 示例 4：复杂请求对象

```java
@Schema(description = "客户查询条件")
public class CustomerQuery extends BaseEntity {

    @Schema(description = "客户名称（模糊查询）", example = "张三")
    private String customerName;

    @Schema(description = "地区", example = "北京", allowableValues = {"北京", "上海", "广州", "深圳"})
    private String region;

    @Schema(description = "客户等级", example = "A", allowableValues = {"A", "B", "C"})
    private String customerLevel;

    @Schema(description = "状态", example = "0", allowableValues = {"0", "1"}, defaultValue = "0")
    private String status;

    @Schema(description = "开始日期", example = "2025-01-01", type = "string", format = "date")
    private Date beginTime;

    @Schema(description = "结束日期", example = "2025-12-31", type = "string", format = "date")
    private Date endTime;

    // getter/setter...
}
```

---

## 七、最佳实践

### 1. Controller 规范

```java
@Tag(name = "模块名称", description = "模块功能描述")
@RestController
@RequestMapping("/api/path")
public class YourController extends BaseController {

    @Operation(summary = "接口简述", description = "接口详细描述")
    @GetMapping("/path")
    public AjaxResult method() {
        // ...
    }
}
```

### 2. 实体类规范

```java
@Schema(description = "实体类描述")
public class YourEntity {

    @Schema(description = "字段描述", example = "示例值", required = true/false)
    private String field;

    // getter/setter...
}
```

### 3. 参数规范

- **路径参数**：使用 `@PathVariable` + `@Parameter`
- **查询参数**：使用 `@RequestParam` + `@Parameter`
- **请求体**：使用 `@RequestBody` + 实体类上的 `@Schema`
- **表单参数**：使用 `@RequestParam` + `@Parameter`

### 4. 响应规范

统一使用项目的响应类：
- `AjaxResult`：单个对象返回
- `TableDataInfo`：分页列表返回
- `R`：通用响应返回

---

## 八、常见问题

### Q1: Swagger UI 无法访问？
**A**: 检查以下几点：
1. 确认应用已启动
2. 检查端口号是否正确（默认3564）
3. 确认 `springdoc.swagger-ui.enabled` 为 `true`
4. 检查防火墙设置

### Q2: 接口没有显示在 Swagger 中？
**A**: 可能原因：
1. Controller 不在扫描包路径下（检查 `packages-to-scan` 配置）
2. 方法上使用了 `@Hidden` 注解
3. 接口路径不匹配 `paths-to-match` 配置

### Q3: 如何隐藏某些接口？
**A**: 使用 `@Hidden` 注解：
```java
@Hidden  // 整个 Controller 隐藏
@RestController
public class InternalController { }

@GetMapping("/internal")
@Hidden  // 单个接口隐藏
public AjaxResult internalMethod() { }
```

### Q4: 如何配置全局请求头？
**A**: 在 `SwaggerConfig.java` 中已配置 JWT 认证，如需添加其他请求头：
```java
@Bean
public OpenAPI customOpenApi() {
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("Authorization", new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization"))
            .addSecuritySchemes("X-Custom-Header", new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-Custom-Header")))
        .addSecurityItem(new SecurityRequirement()
            .addList("Authorization")
            .addList("X-Custom-Header"));
}
```

### Q5: 如何在生产环境禁用 Swagger？
**A**: 在 `application-prod.yml` 中配置：
```yaml
springdoc:
  swagger-ui:
    enabled: false
  api-docs:
    enabled: false
```

或者在配置类中：
```java
@Configuration
@Profile("!prod")  // 非生产环境才启用
public class SwaggerConfig {
    // ...
}
```

### Q6: 如何导出 API 文档？
**A**: 访问 `http://localhost:3564/v3/api-docs` 获取 JSON 格式文档，可以：
1. 保存为 JSON 文件
2. 导入到 Postman、Apifox 等工具
3. 使用 swagger-codegen 生成客户端代码

---

## 九、升级说明

### 当前版本：SpringDoc 2.8.9

这是目前（2025年1月）的最新稳定版本，**无需升级**。

### 如何升级（如果将来需要）

1. 修改 `pom.xml` 中的版本号：
```xml
<properties>
    <springdoc.version>新版本号</springdoc.version>
</properties>
```

2. 重新构建项目：
```bash
mvn clean install
```

3. 重启应用

---

## 十、相关链接

- **SpringDoc 官方文档**：https://springdoc.org/
- **OpenAPI 3.0 规范**：https://swagger.io/specification/
- **Swagger UI 官网**：https://swagger.io/tools/swagger-ui/
- **项目示例 Controller**：`youkang-admin/src/main/java/com/youkang/web/controller/tool/TestController.java`

---

## 十一、快速开始

### 1分钟快速上手

1. **启动项目**
```bash
mvn spring-boot:run -pl youkang-admin
```

2. **访问 Swagger UI**
```
http://localhost:3564/swagger-ui.html
```

3. **测试接口**
   - 点击接口展开详情
   - 点击 "Try it out"
   - 填写参数
   - 点击 "Execute" 执行
   - 查看返回结果

4. **添加认证**（如果需要）
   - 点击右上角 "Authorize"
   - 输入 JWT Token
   - 点击 "Authorize"
   - 现在可以测试需要认证的接口

---

## 结语

你的项目已经完整集成了 SpringDoc OpenAPI 3.0，所有配置都已就绪！

现在你可以：
- ✅ 在 Controller 中使用 `@Tag`、`@Operation` 等注解
- ✅ 在实体类中使用 `@Schema` 注解
- ✅ 访问 Swagger UI 查看和测试接口
- ✅ 导出 OpenAPI JSON 文档

建议参考 `TestController.java` 的示例代码，开始为你的接口添加文档注解！
