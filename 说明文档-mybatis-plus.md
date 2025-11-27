# MyBatis Plus 集成与使用指南

本文档介绍如何在有康管理系统中集成和使用 MyBatis Plus。

## 📌 一、什么是 MyBatis Plus

MyBatis Plus（简称 MP）是一个 MyBatis 的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

**核心特性：**
- **无侵入**：只做增强不做改变，引入它不会对现有工程产生影响
- **损耗小**：启动即会自动注入基本 CRUD，性能基本无损耗
- **强大的 CRUD 操作**：内置通用 Mapper、通用 Service，仅通过少量配置即可实现单表大部分 CRUD 操作
- **支持 Lambda 形式调用**：通过 Lambda 表达式，方便的编写各类查询条件
- **支持主键自动生成**：支持多达 4 种主键策略
- **内置分页插件**：基于 MyBatis 物理分页，开发者无需关心具体操作
- **内置性能分析插件**：可输出 SQL 语句以及其执行时间

---

## 🔧 二、集成步骤

### 2.1 添加 MyBatis Plus 依赖

#### ① 修改根目录 `pom.xml`

在 `<properties>` 中添加版本号：

```xml
<properties>
    <!-- 现有配置保持不变 -->
    <mybatis-plus.version>3.5.5</mybatis-plus.version>
</properties>
```

在 `<dependencyManagement>` 中添加依赖声明：

```xml
<dependencyManagement>
    <dependencies>
        <!-- 现有依赖保持不变 -->

        <!-- MyBatis Plus -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

#### ② 修改 `youkang-common/pom.xml`

添加 MyBatis Plus 依赖（因为其他模块都依赖 common 模块）：

```xml
<dependencies>
    <!-- 现有依赖保持不变 -->

    <!-- MyBatis Plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
    </dependency>
</dependencies>
```

**⚠️ 注意：** 如果添加了 MyBatis Plus，建议移除原有的 MyBatis 依赖，因为 MyBatis Plus 已经包含了 MyBatis。

```xml
<!-- 可以注释掉或删除 -->
<!--
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
</dependency>
-->
```

### 2.2 配置 MyBatis Plus

#### ① 修改 `application.yml`

```yaml
# MyBatis Plus 配置
mybatis-plus:
  # 搜索指定包别名
  type-aliases-package: com.youkang.**.domain
  # 配置mapper的扫描，找到所有的mapper.xml映射文件
  mapper-locations: classpath*:mapper/**/*Mapper.xml
  # 全局配置
  global-config:
    # 数据库配置
    db-config:
      # 主键类型（AUTO-数据库自增 INPUT-用户输入 ID_WORKER-全局唯一ID）
      id-type: AUTO
      # 逻辑删除字段名
      logic-delete-field: delFlag
      # 逻辑删除值（删除后的值）
      logic-delete-value: 2
      # 逻辑未删除值（正常的值）
      logic-not-delete-value: 0
  # MyBatis 原生配置
  configuration:
    # 驼峰下划线转换
    map-underscore-to-camel-case: true
    # 缓存配置
    cache-enabled: false
    # 打印SQL（开发环境开启，生产环境关闭）
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

#### ② 创建 MyBatis Plus 配置类

创建文件：`youkang-framework/src/main/java/com/youkang/framework/config/MybatisPlusConfig.java`

```java
package com.youkang.framework.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置
 *
 * @author youkang
 */
@Configuration
public class MybatisPlusConfig
{
    /**
     * MyBatis Plus 拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor()
    {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        return interceptor;
    }
}
```

---

## 💻 三、使用方式

MyBatis Plus 提供了两种使用方式：

### 方式一：继承 BaseMapper（推荐）

这是最简单的方式，无需编写 XML，自动拥有基础 CRUD 功能。

#### 1. 实体类继承 Model 或添加注解

```java
package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.youkang.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户信息对象 yk_customer_info
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yk_customer_info")  // 指定表名
public class YkCustomerInfo extends BaseEntity
{
    /** 客户ID */
    @TableId(value = "id", type = IdType.AUTO)  // 主键，自动增长
    private Integer id;

    /** 客户姓名 */
    @TableField("customer_name")  // 指定字段名（可选，自动驼峰转下划线）
    private String customerName;

    /** 地区 */
    private String region;

    /** 地址 */
    private String address;

    /** 电话 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 微信ID */
    private String wechatId;

    /** 等级 */
    private String customerLevel;

    /** 状态 */
    private String status;

    /** 销售员 */
    private String salesPerson;

    /** 客户单位 */
    private String customerUnit;

    /** 结算方式 */
    private String paymentMethod;

    /** 发票种类 */
    private String invoiceType;

    /** 备注 */
    private String remarks;

    /** 所属公司 */
    private String company;

    /** 逻辑删除标志（0正常 2删除） */
    @TableLogic
    private String delFlag;
}
```

#### 2. Mapper 继承 BaseMapper

```java
package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youkang.system.domain.YkCustomerInfo;

/**
 * 客户信息Mapper接口
 */
public interface YkCustomerInfoMapper extends BaseMapper<YkCustomerInfo>
{
    // 继承 BaseMapper 后，自动拥有以下方法：
    // - insert(T entity)：插入一条记录
    // - deleteById(Serializable id)：根据ID删除
    // - deleteBatchIds(Collection<? extends Serializable> idList)：批量删除
    // - updateById(T entity)：根据ID更新
    // - selectById(Serializable id)：根据ID查询
    // - selectBatchIds(Collection<? extends Serializable> idList)：批量查询
    // - selectList(Wrapper<T> queryWrapper)：条件查询列表
    // - selectPage(IPage<T> page, Wrapper<T> queryWrapper)：分页查询
    // 还有很多其他方法...

    // 如果需要自定义SQL，可以继续添加方法
    // List<YkCustomerInfo> selectCustomList(YkCustomerInfo customer);
}
```

#### 3. Service 继承 IService 和 ServiceImpl

**Service 接口：**

```java
package com.youkang.system.service.customer;

import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.YkCustomerInfo;

/**
 * 客户信息Service接口
 */
public interface IYkCustomerInfoService extends IService<YkCustomerInfo>
{
    // 继承 IService 后，自动拥有以下方法：
    // - save(T entity)：插入一条记录
    // - saveBatch(Collection<T> entityList)：批量插入
    // - removeById(Serializable id)：根据ID删除
    // - updateById(T entity)：根据ID更新
    // - getById(Serializable id)：根据ID查询
    // - list()：查询所有
    // - list(Wrapper<T> queryWrapper)：条件查询
    // - page(IPage<T> page)：分页查询
    // - page(IPage<T> page, Wrapper<T> queryWrapper)：条件分页查询
    // 还有很多其他方法...

    // 可以添加自定义方法
}
```

**Service 实现类：**

```java
package com.youkang.system.service.customer.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.youkang.system.mapper.YkCustomerInfoMapper;
import com.youkang.system.domain.YkCustomerInfo;
import com.youkang.system.service.customer.IYkCustomerInfoService;

/**
 * 客户信息Service业务层处理
 */
@Service
public class YkCustomerInfoServiceImpl
    extends ServiceImpl<YkCustomerInfoMapper, YkCustomerInfo>
    implements IYkCustomerInfoService
{
    // ServiceImpl 已经实现了 IService 中的所有方法
    // 如果需要自定义逻辑，可以重写或添加新方法
}
```

#### 4. Controller 中使用

```java
package com.youkang.web.controller.customer;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.youkang.common.annotation.Log;
import com.youkang.common.core.controller.BaseController;
import com.youkang.common.core.domain.AjaxResult;
import com.youkang.common.enums.BusinessType;
import com.youkang.system.domain.YkCustomerInfo;
import com.youkang.system.service.customer.IYkCustomerInfoService;
import com.youkang.common.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 客户信息Controller
 */
@RestController
@RequestMapping("/customer/info")
public class YkCustomerInfoController extends BaseController
{
    @Autowired
    private IYkCustomerInfoService customerInfoService;

    /**
     * 查询客户信息列表（MyBatis Plus 方式）
     */
    @PreAuthorize("@ss.hasPermi('customer:info:list')")
    @GetMapping("/list")
    public AjaxResult list(YkCustomerInfo customer,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize)
    {
        // 方式1：使用 QueryWrapper（不推荐）
        // QueryWrapper<YkCustomerInfo> wrapper = new QueryWrapper<>();
        // wrapper.like(StringUtils.isNotEmpty(customer.getCustomerName()),
        //              "customer_name", customer.getCustomerName());

        // 方式2：使用 LambdaQueryWrapper（推荐，类型安全）
        LambdaQueryWrapper<YkCustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(customer.getCustomerName()),
                     YkCustomerInfo::getCustomerName, customer.getCustomerName())
               .eq(StringUtils.isNotEmpty(customer.getRegion()),
                   YkCustomerInfo::getRegion, customer.getRegion())
               .eq(StringUtils.isNotEmpty(customer.getStatus()),
                   YkCustomerInfo::getStatus, customer.getStatus())
               .like(StringUtils.isNotEmpty(customer.getPhone()),
                     YkCustomerInfo::getPhone, customer.getPhone())
               .orderByDesc(YkCustomerInfo::getCreateTime);

        // 分页查询
        Page<YkCustomerInfo> page = new Page<>(pageNum, pageSize);
        IPage<YkCustomerInfo> result = customerInfoService.page(page, wrapper);

        return AjaxResult.success()
                .put("rows", result.getRecords())
                .put("total", result.getTotal());
    }

    /**
     * 获取客户详情
     */
    @PreAuthorize("@ss.hasPermi('customer:info:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Integer id)
    {
        return success(customerInfoService.getById(id));
    }

    /**
     * 新增客户
     */
    @PreAuthorize("@ss.hasPermi('customer:info:add')")
    @Log(title = "客户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody YkCustomerInfo customer)
    {
        return toAjax(customerInfoService.save(customer));
    }

    /**
     * 修改客户
     */
    @PreAuthorize("@ss.hasPermi('customer:info:edit')")
    @Log(title = "客户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody YkCustomerInfo customer)
    {
        return toAjax(customerInfoService.updateById(customer));
    }

    /**
     * 删除客户
     */
    @PreAuthorize("@ss.hasPermi('customer:info:remove')")
    @Log(title = "客户信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(customerInfoService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 批量新增客户
     */
    @PreAuthorize("@ss.hasPermi('customer:info:add')")
    @Log(title = "批量新增客户", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult batchAdd(@RequestBody List<YkCustomerInfo> customers)
    {
        return toAjax(customerInfoService.saveBatch(customers));
    }
}
```

---

## 🔍 四、常用查询示例

### 4.1 基础查询

```java
// 1. 根据ID查询
YkCustomerInfo customer = customerInfoService.getById(1);

// 2. 查询所有
List<YkCustomerInfo> list = customerInfoService.list();

// 3. 根据IDs批量查询
List<YkCustomerInfo> list = customerInfoService.listByIds(Arrays.asList(1, 2, 3));

// 4. 查询总数
long count = customerInfoService.count();
```

### 4.2 条件查询

```java
// 方式1：使用 LambdaQueryWrapper（推荐）
LambdaQueryWrapper<YkCustomerInfo> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(YkCustomerInfo::getStatus, "正常")                    // 等于
       .like(YkCustomerInfo::getCustomerName, "张三")           // 模糊查询
       .ge(YkCustomerInfo::getCreateTime, "2024-01-01")       // 大于等于
       .le(YkCustomerInfo::getCreateTime, "2024-12-31")       // 小于等于
       .in(YkCustomerInfo::getCustomerLevel, "VIP", "SVIP")   // IN查询
       .isNotNull(YkCustomerInfo::getPhone)                    // 不为空
       .orderByDesc(YkCustomerInfo::getCreateTime);            // 排序

List<YkCustomerInfo> list = customerInfoService.list(wrapper);

// 方式2：使用 QueryWrapper
QueryWrapper<YkCustomerInfo> wrapper = new QueryWrapper<>();
wrapper.eq("status", "正常")
       .like("customer_name", "张三")
       .orderByDesc("create_time");

List<YkCustomerInfo> list = customerInfoService.list(wrapper);

// 方式3：使用链式查询（最简洁）
List<YkCustomerInfo> list = customerInfoService.lambdaQuery()
        .eq(YkCustomerInfo::getStatus, "正常")
        .like(YkCustomerInfo::getCustomerName, "张三")
        .orderByDesc(YkCustomerInfo::getCreateTime)
        .list();
```

### 4.3 分页查询

```java
// 创建分页对象（当前页，每页条数）
Page<YkCustomerInfo> page = new Page<>(1, 10);

// 构建查询条件
LambdaQueryWrapper<YkCustomerInfo> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(YkCustomerInfo::getStatus, "正常")
       .orderByDesc(YkCustomerInfo::getCreateTime);

// 分页查询
IPage<YkCustomerInfo> result = customerInfoService.page(page, wrapper);

// 获取结果
List<YkCustomerInfo> records = result.getRecords();  // 数据列表
long total = result.getTotal();                       // 总记录数
long pages = result.getPages();                       // 总页数
long current = result.getCurrent();                   // 当前页
```

### 4.4 复杂查询

```java
// 1. OR 查询
LambdaQueryWrapper<YkCustomerInfo> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(YkCustomerInfo::getStatus, "正常")
       .and(w -> w.eq(YkCustomerInfo::getCustomerLevel, "VIP")
                  .or()
                  .eq(YkCustomerInfo::getCustomerLevel, "SVIP"));

// 2. 分组查询
QueryWrapper<YkCustomerInfo> wrapper = new QueryWrapper<>();
wrapper.select("region", "count(*) as count")
       .groupBy("region")
       .having("count > {0}", 5);

// 3. 子查询
wrapper.inSql(YkCustomerInfo::getRegion,
              "select region from yk_region where status = '正常'");

// 4. 多条件动态查询
wrapper.eq(StringUtils.isNotEmpty(customer.getStatus()),
           YkCustomerInfo::getStatus, customer.getStatus())
       .like(StringUtils.isNotEmpty(customer.getCustomerName()),
             YkCustomerInfo::getCustomerName, customer.getCustomerName())
       .between(customer.getStartTime() != null && customer.getEndTime() != null,
                YkCustomerInfo::getCreateTime, customer.getStartTime(), customer.getEndTime());
```

### 4.5 更新操作

```java
// 1. 根据ID更新
YkCustomerInfo customer = new YkCustomerInfo();
customer.setId(1);
customer.setStatus("禁用");
customerInfoService.updateById(customer);

// 2. 条件更新
LambdaUpdateWrapper<YkCustomerInfo> wrapper = new LambdaUpdateWrapper<>();
wrapper.set(YkCustomerInfo::getStatus, "正常")
       .eq(YkCustomerInfo::getCustomerLevel, "VIP");
customerInfoService.update(wrapper);

// 3. 链式更新
customerInfoService.lambdaUpdate()
        .set(YkCustomerInfo::getStatus, "正常")
        .eq(YkCustomerInfo::getId, 1)
        .update();
```

### 4.6 删除操作

```java
// 1. 根据ID删除
customerInfoService.removeById(1);

// 2. 批量删除
customerInfoService.removeByIds(Arrays.asList(1, 2, 3));

// 3. 条件删除
LambdaQueryWrapper<YkCustomerInfo> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(YkCustomerInfo::getStatus, "禁用");
customerInfoService.remove(wrapper);

// 4. 逻辑删除（需要配置 @TableLogic 注解）
// 实际执行的是 UPDATE 语句，将 del_flag 设置为删除标志
customerInfoService.removeById(1);
```

---

## 🎯 五、高级功能

### 5.1 自动填充

在实体类中添加注解：

```java
public class YkCustomerInfo extends BaseEntity
{
    /** 创建者 */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新者 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
```

创建填充处理器：

```java
package com.youkang.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.youkang.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyBatis Plus 字段自动填充
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler
{
    @Override
    public void insertFill(MetaObject metaObject)
    {
        // 插入时自动填充
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "createBy", String.class, getUsername());
    }

    @Override
    public void updateFill(MetaObject metaObject)
    {
        // 更新时自动填充
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        this.strictUpdateFill(metaObject, "updateBy", String.class, getUsername());
    }

    /**
     * 获取当前登录用户名
     */
    private String getUsername()
    {
        try
        {
            return SecurityUtils.getUsername();
        }
        catch (Exception e)
        {
            return "system";
        }
    }
}
```

### 5.2 乐观锁

在实体类中添加版本号字段：

```java
public class YkCustomerInfo extends BaseEntity
{
    /** 版本号 */
    @Version
    private Integer version;
}
```

使用：

```java
// 查询
YkCustomerInfo customer = customerInfoService.getById(1);
customer.setStatus("正常");

// 更新时会自动比较版本号
// UPDATE yk_customer_info SET status='正常', version=version+1 WHERE id=1 AND version=0
customerInfoService.updateById(customer);
```

### 5.3 逻辑删除

在实体类中添加逻辑删除字段：

```java
public class YkCustomerInfo extends BaseEntity
{
    /** 删除标志（0正常 2删除） */
    @TableLogic(value = "0", delval = "2")
    private String delFlag;
}
```

使用：

```java
// 逻辑删除（实际执行 UPDATE 语句）
customerInfoService.removeById(1);
// SQL: UPDATE yk_customer_info SET del_flag='2' WHERE id=1

// 查询时自动过滤已删除数据
// SQL: SELECT * FROM yk_customer_info WHERE del_flag='0'
List<YkCustomerInfo> list = customerInfoService.list();
```

---

## ⚠️ 六、注意事项

### 6.1 与原有 MyBatis 的兼容性

MyBatis Plus 完全兼容原有的 MyBatis 代码：

```java
public interface YkCustomerInfoMapper extends BaseMapper<YkCustomerInfo>
{
    // MyBatis Plus 提供的方法
    // ...

    // 原有的自定义方法依然可以使用
    List<YkCustomerInfo> selectCustomList(@Param("customer") YkCustomerInfo customer);
}
```

对应的 XML 文件：

```xml
<mapper namespace="com.youkang.system.mapper.YkCustomerInfoMapper">
    <!-- 自定义SQL -->
    <select id="selectCustomList" resultType="YkCustomerInfo">
        SELECT * FROM yk_customer_info
        WHERE customer_name LIKE CONCAT('%', #{customer.customerName}, '%')
    </select>
</mapper>
```

### 6.2 分页插件冲突

如果同时使用了 PageHelper 和 MyBatis Plus 的分页插件，可能会冲突。建议：

**方案1：只使用 MyBatis Plus 分页**

```java
// 使用 MyBatis Plus 的 Page
Page<YkCustomerInfo> page = new Page<>(pageNum, pageSize);
IPage<YkCustomerInfo> result = customerInfoService.page(page);
```

**方案2：混合使用（不推荐）**

如果要继续使用 PageHelper，需要在配置中明确指定分页插件的顺序。

### 6.3 字段名映射

MyBatis Plus 默认使用驼峰转下划线：

- `customerName` → `customer_name`
- `createTime` → `create_time`

如果字段名不符合规则，使用 `@TableField` 注解：

```java
@TableField("wx_id")
private String wechatId;
```

### 6.4 性能优化

```java
// 1. 只查询需要的字段
customerInfoService.lambdaQuery()
        .select(YkCustomerInfo::getId, YkCustomerInfo::getCustomerName)
        .list();

// 2. 批量操作使用 saveBatch
customerInfoService.saveBatch(list, 1000);  // 每1000条一批

// 3. 使用 exists 代替 count
boolean exists = customerInfoService.lambdaQuery()
        .eq(YkCustomerInfo::getPhone, "13800138000")
        .exists();
```

---

## 📚 七、推荐资源

- **官方文档**：https://baomidou.com/
- **代码生成器**：https://baomidou.com/pages/779a6e/
- **常见问题**：https://baomidou.com/pages/f9a237/

---

## 🎓 八、总结

**推荐使用场景：**

✅ **适合使用 MyBatis Plus：**
- 单表 CRUD 操作
- 简单的条件查询
- 分页查询
- 批量操作
- 逻辑删除

✅ **适合使用原生 MyBatis：**
- 复杂的多表关联查询
- 复杂的SQL逻辑
- 存储过程调用
- 特殊的数据库操作

**最佳实践：**

```java
// Service 中混合使用
@Service
public class YkCustomerInfoServiceImpl
    extends ServiceImpl<YkCustomerInfoMapper, YkCustomerInfo>
    implements IYkCustomerInfoService
{
    @Autowired
    private YkCustomerInfoMapper mapper;

    // 简单查询使用 MyBatis Plus
    public List<YkCustomerInfo> getByStatus(String status)
    {
        return this.lambdaQuery()
                .eq(YkCustomerInfo::getStatus, status)
                .list();
    }

    // 复杂查询使用自定义 SQL
    public List<YkCustomerInfo> getCustomReport(Map<String, Object> params)
    {
        return mapper.selectCustomReport(params);
    }
}
```

---

**文档版本：** v1.0
**最后更新：** 2025-01-20
**维护者：** 有康开发团队
