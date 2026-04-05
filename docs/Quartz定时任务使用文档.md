# Quartz 定时任务使用文档

本项目的定时任务基于 Quartz 框架实现，采用数据库存储任务配置的方式管理定时任务。

---

## 一、整体架构

### 核心组件

| 组件 | 路径 | 职责 |
|------|------|------|
| `SysJob` | `quartz/domain/SysJob.java` | 任务实体，对应 `sys_job` 表 |
| `SysJobLog` | `quartz/domain/SysJobLog.java` | 执行日志实体，对应 `sys_job_log` 表 |
| `SysJobController` | `quartz/controller/SysJobController.java` | 任务管理 API（增删改查、暂停恢复、立即执行） |
| `SysJobServiceImpl` | `quartz/service/impl/SysJobServiceImpl.java` | 任务调度核心逻辑，启动时从数据库加载所有任务 |
| `AbstractQuartzJob` | `quartz/util/AbstractQuartzJob.java` | 任务执行基类，封装了执行前后的日志记录 |
| `ScheduleUtils` | `quartz/util/ScheduleUtils.java` | Quartz 调度工具类 |
| `JobInvokeUtil` | `quartz/util/JobInvokeUtil.java` | 通过反射调用目标 Bean 方法 |

### 执行流程

```
Quartz 触发
  → AbstractQuartzJob.execute()
    → before()     记录开始时间
    → doExecute()  调用 JobInvokeUtil.invokeMethod()
      → 解析 invokeTarget 字符串（beanName.methodName(params)）
      → 从 Spring 容器获取 Bean
      → 反射调用方法
    → after()      记录执行日志到 sys_job_log 表
```

### 启动加载

```java
// SysJobServiceImpl.java
@PostConstruct
public void init() {
    scheduler.clear();
    List<SysJob> jobList = jobMapper.selectJobAll();
    for (SysJob job : jobList) {
        ScheduleUtils.createScheduleJob(scheduler, job);
    }
}
```

项目启动时自动从 `sys_job` 表加载所有任务注册到 Quartz 调度器中。

---

## 二、任务配置表（sys_job）

| 字段 | 类型 | 说明 |
|------|------|------|
| `job_id` | BIGINT | 任务ID（自增主键） |
| `job_name` | VARCHAR(64) | 任务名称 |
| `job_group` | VARCHAR(64) | 任务组名（默认 `DEFAULT`） |
| `invoke_target` | VARCHAR(500) | 调用目标字符串，格式：`beanName.methodName(params)` |
| `cron_expression` | VARCHAR(255) | Cron 表达式 |
| `misfire_policy` | VARCHAR(1) | 计划策略：0-默认 / 1-立即触发 / 2-触发一次 / 3-不触发 |
| `concurrent` | VARCHAR(1) | 是否并发：0-允许 / 1-禁止 |
| `status` | CHAR(1) | 状态：0-正常（运行中）/ 1-暂停 |

---

## 三、如何新增一个定时任务

### 步骤1：编写任务类

在 `com.youkang.quartz.task` 包下创建类，使用 `@Component` 注册为 Spring Bean：

```java
package com.youkang.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("myTask")       // bean 名称，invokeTarget 中要用到
@Slf4j
public class MyTask {

    /**
     * 无参方法
     */
    public void executeNoParams() {
        log.info("执行无参定时任务");
    }

    /**
     * 带参方法
     * 支持的参数类型：String、Boolean、Long、Double、Integer
     */
    public void executeWithParams(String name, Integer count) {
        log.info("执行带参任务：name={}, count={}", name, count);
    }
}
```

**注意：任务类必须在 `com.youkang.quartz.task` 包下**，否则白名单校验不通过。

### 步骤2：注册任务到数据库

**方式一：通过 SQL 插入（推荐用于初始化）**

```sql
INSERT INTO sys_job (job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time)
VALUES ('我的任务', 'DEFAULT', 'myTask.executeNoParams()', '0 0/5 * * * ?', '1', '1', '0', 'admin', NOW());
```

**方式二：通过 API 接口新增**

```
POST /monitor/job
```

```json
{
  "jobName": "我的任务",
  "jobGroup": "DEFAULT",
  "invokeTarget": "myTask.executeNoParams()",
  "cronExpression": "0 0/5 * * * ?",
  "misfirePolicy": "1",
  "concurrent": "1",
  "status": "0"
}
```

### 步骤3：重启服务（SQL方式需要）

通过 SQL 插入的任务需要重启服务才会加载。通过 API 新增的任务会立即生效。

---

## 四、invokeTarget 调用目标格式

### 格式规则

```
beanName.methodName()           // 无参
beanName.methodName(param1, param2)   // 有参
```

### 参数类型说明

| 写法 | 类型 | 示例 |
|------|------|------|
| `'hello'` 或 `"hello"` | String | `myTask.run('hello')` |
| `true` / `false` | Boolean | `myTask.run(true)` |
| `100L` | Long | `myTask.run(100L)` |
| `3.14D` | Double | `myTask.run(3.14D)` |
| `123` | Integer | `myTask.run(123)` |

### 示例

```
orderStatusTask.checkOrderOutbound()                        // 无参调用
ryTask.ryParams('hello')                                     // String 参数
ryTask.ryMultipleParams('test', true, 100L, 3.14D, 42)      // 多参数
```

---

## 五、Cron 表达式速查

格式：`秒 分 时 日 月 周 [年]`

### 常用表达式

| 表达式 | 说明 |
|--------|------|
| `0 0/5 * * * ?` | 每5分钟执行 |
| `0 0/10 * * * ?` | 每10分钟执行 |
| `0 0 * * * ?` | 每小时整点执行 |
| `0 0 2 * * ?` | 每天凌晨2点执行 |
| `0 0 0 * * ?` | 每天零点执行 |
| `0 0 10,14,16 * * ?` | 每天10点、14点、16点执行 |
| `0 0/30 9-17 * * ?` | 工作时间每30分钟执行 |
| `0 0 12 ? * WED` | 每周三中午12点执行 |

### 特殊字符

| 字符 | 说明 |
|------|------|
| `*` | 任意值 |
| `?` | 不指定（日和周互斥时使用） |
| `-` | 范围，如 `1-5` |
| `,` | 列举，如 `1,3,5` |
| `/` | 步长，如 `0/5` 表示从0开始每5单位 |
| `L` | 最后（日/周） |
| `W` | 最近工作日 |

---

## 六、管理接口

### 基础路径

```
/monitor/job
```

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 查询任务列表 | GET | `/monitor/job/list` | 权限：`monitor:job:list` |
| 查询任务详情 | GET | `/monitor/job/{jobId}` | 权限：`monitor:job:query` |
| 新增任务 | POST | `/monitor/job` | 权限：`monitor:job:add` |
| 修改任务 | PUT | `/monitor/job` | 权限：`monitor:job:edit` |
| 删除任务 | DELETE | `/monitor/job/{jobIds}` | 权限：`monitor:job:remove` |
| 修改状态 | PUT | `/monitor/job/changeStatus` | 权限：`monitor:job:changeStatus` |
| 立即执行一次 | PUT | `/monitor/job/run` | 权限：`monitor:job:changeStatus` |

### 任务状态变更

```json
// 暂停任务
PUT /monitor/job/changeStatus
{
  "jobId": 1,
  "status": "1"
}

// 恢复任务
PUT /monitor/job/changeStatus
{
  "jobId": 1,
  "status": "0"
}
```

### 立即执行一次

```json
PUT /monitor/job/run
{
  "jobId": 1,
  "jobGroup": "DEFAULT"
}
```

---

## 七、执行日志

所有任务的执行记录保存在 `sys_job_log` 表中，可通过接口查询：

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 查询日志列表 | GET | `/monitor/jobLog/list` | 权限：`monitor:job:list` |
| 查询日志详情 | GET | `/monitor/jobLog/{jobLogId}` | 权限：`monitor:job:query` |
| 删除日志 | DELETE | `/monitor/jobLog/{jobLogIds}` | 权限：`monitor:job:remove` |
| 清空日志 | DELETE | `/monitor/jobLog/clean` | 权限：`monitor:job:remove` |
| 导出日志 | POST | `/monitor/jobLog/export` | 权限：`monitor:job:export` |

---

## 八、现有任务清单

| Bean名称 | 任务类 | 方法 | 说明 |
|----------|--------|------|------|
| `ryTask` | `RyTask` | `ryNoParams()` | 示例任务（无参） |
| `ryTask` | `RyTask` | `ryParams(String)` | 示例任务（有参） |
| `ryTask` | `RyTask` | `ryMultipleParams(...)` | 示例任务（多参） |
| `templateFailedEmailTask` | `TemplateFailedEmailTask` | `sendTemplateFailedEmail()` | 模板失败邮件通知 |
| `orderStatusTask` | `OrderStatusTask` | `checkOrderOutbound()` | 订单状态出库检查 |
| `orderStatusTask` | `OrderStatusTask` | `checkOrderComplete()` | 订单自动完成检查 |

---

## 九、注意事项

1. **包路径白名单**：任务类必须在 `com.youkang.quartz.task` 包下，其他包路径会被拦截。

2. **并发控制**：`concurrent` 设为 `1`（禁止）时，如果上一次执行还未结束，新的触发会被跳过。建议生产环境设为 `1`。

3. **任务方法必须是 public**：反射调用要求方法可见性为 public。

4. **事务边界**：任务方法不自带事务，如果需要事务请在方法上加 `@Transactional`。

5. **异常处理**：任务方法内部异常会被 `AbstractQuartzJob` 捕获并记录到 `sys_job_log`，不会影响调度器的后续触发。建议在任务方法内部自行 try-catch 处理业务异常。

6. **修改数据库后需重启**：直接通过 SQL 修改 `sys_job` 表后，需要重启服务才能生效。推荐使用管理接口操作。
