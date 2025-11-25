# 企业邮件发送工具使用说明

## 一、功能介绍

本邮件工具提供以下功能：
1. **发送纯文本邮件**（不带附件）
2. **发送HTML格式邮件**（不带附件）
3. **发送带附件的邮件**
4. **使用固定模板发送邮件**
5. **支持单个/多个收件人**
6. **支持内嵌图片**

## 二、配置步骤

### 1. 添加邮件配置到 application.yml

在 `youkang-admin/src/main/resources/application.yml` 文件中添加以下配置：

```yaml
# Spring邮件配置
spring:
  mail:
    # SMTP服务器地址
    host: smtp.exmail.qq.com
    # SMTP服务器端口（SSL端口通常为465，TLS端口通常为587）
    port: 465
    # 发件人邮箱账号
    username: your-email@youkang.com
    # 邮箱授权码或密码（不是邮箱登录密码！）
    password: your-email-password-or-auth-code
    # 默认编码
    default-encoding: UTF-8
    # 邮件协议
    protocol: smtp
    # SMTP配置
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          socketFactory:
            class: javax.net.ssl.SSLSocketFactory
          timeout: 25000
```

### 2. 不同邮箱服务商的配置参数

#### QQ企业邮箱
```yaml
spring:
  mail:
    host: smtp.exmail.qq.com
    port: 465
```

#### 腾讯企业邮箱
```yaml
spring:
  mail:
    host: smtp.exmail.qq.com
    port: 465
```

#### 阿里云邮箱
```yaml
spring:
  mail:
    host: smtp.aliyun.com
    port: 465
```

#### 网易企业邮箱
```yaml
spring:
  mail:
    host: smtp.ym.163.com
    port: 465
```

#### 163邮箱
```yaml
spring:
  mail:
    host: smtp.163.com
    port: 465
```

#### Gmail（需科学上网）
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587  # 使用TLS
```

### 3. 获取邮箱授权码

大多数邮箱服务商出于安全考虑，不允许直接使用邮箱登录密码发送邮件，需要使用**授权码**。

#### QQ邮箱/企业邮箱获取授权码：
1. 登录邮箱网页版
2. 进入"设置" → "账户"
3. 找到"POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV服务"
4. 开启"POP3/SMTP服务"或"IMAP/SMTP服务"
5. 按照提示获取授权码（需要手机验证）
6. 将授权码填入 `spring.mail.password` 配置项

#### 其他邮箱类似操作，一般在"设置" → "账户安全" 或 "POP3/SMTP设置"中。

## 三、使用方法

### 1. 在需要发送邮件的地方注入 MailUtils

```java
@Service
public class YourService {

    @Resource
    private MailUtils mailUtils;

    // 你的业务代码...
}
```

### 2. 发送简单文本邮件（不带附件）

```java
boolean success = mailUtils.sendSimpleText(
    "recipient@example.com",
    "邮件主题",
    "邮件内容"
);
```

### 3. 使用模板发送HTML格式邮件（不带附件）

```java
// 构建邮件模板
MailTemplate template = MailTemplate.builder()
    .title("系统通知")                    // 邮件标题（显示在邮件顶部）
    .recipientName("张三")                // 收件人姓名
    .content("您的账号已成功激活...")      // 邮件正文
    .senderName("有康科技有限公司")        // 发件人/公司名称
    .contactPhone("400-123-4567")        // 联系电话（可选）
    .contactEmail("support@youkang.com") // 联系邮箱（可选）
    .includeTimestamp(true)              // 是否包含时间戳（默认true）
    .build();

// 发送邮件
boolean success = mailUtils.sendHtmlByTemplate(
    "zhangsan@example.com",
    "账号激活通知",
    template
);
```

### 4. 发送带附件的邮件（单个收件人）

```java
// 构建邮件模板
MailTemplate template = MailTemplate.builder()
    .title("合同文件")
    .recipientName("王总")
    .content("附件是合作合同，请查收。")
    .senderName("有康法务部")
    .contactPhone("010-12345678")
    .build();

// 准备附件
File file1 = new File("D:/contracts/contract.pdf");
File file2 = new File("D:/contracts/annex.pdf");

// 发送邮件
boolean success = mailUtils.sendWithAttachmentByTemplate(
    "wangzong@example.com",
    "【有康】合作合同",
    template,
    file1,
    file2  // 可以添加多个附件
);
```

### 5. 发送邮件给多个收件人（不带附件）

```java
MailTemplate template = MailTemplate.builder()
    .title("会议通知")
    .recipientName("全体员工")
    .content("明天上午9:00召开月度总结会议...")
    .senderName("行政部")
    .build();

String[] recipients = {
    "employee1@youkang.com",
    "employee2@youkang.com",
    "employee3@youkang.com"
};

boolean success = mailUtils.sendHtmlByTemplate(
    recipients,
    "【通知】月度总结会议",
    template
);
```

### 6. 发送带附件的邮件给多个收件人

```java
MailTemplate template = MailTemplate.builder()
    .title("月度报表")
    .recipientName("各部门负责人")
    .content("附件是11月份月度报表...")
    .senderName("财务部")
    .build();

String[] recipients = {
    "dept1@youkang.com",
    "dept2@youkang.com"
};

File report = new File("D:/reports/monthly-report.xlsx");

boolean success = mailUtils.sendWithAttachmentByTemplate(
    recipients,
    "【财务】11月月度报表",
    template,
    report
);
```

## 四、邮件模板说明

`MailTemplate` 类用于生成固定格式的邮件内容，包含以下字段：

| 字段 | 说明 | 是否必填 |
|-----|------|---------|
| title | 邮件标题（显示在邮件头部） | 可选 |
| recipientName | 收件人姓名 | 可选 |
| content | 邮件正文内容 | 必填 |
| senderName | 发件人/公司名称 | 可选 |
| contactPhone | 联系电话 | 可选 |
| contactEmail | 联系邮箱 | 可选 |
| includeTimestamp | 是否包含发送时间戳 | 可选（默认true） |

### 邮件模板生成的格式效果：

#### HTML格式（推荐）：
- 带有绿色头部背景的标题
- 格式化的问候语："尊敬的 XXX："
- 主要内容区域（支持换行）
- 页脚包含发件人信息和联系方式
- 底部显示发送时间

#### 纯文本格式：
- 简洁的文本格式
- 包含相同的信息结构
- 适合不支持HTML的邮件客户端

## 五、完整示例

详细的使用示例请参考 `MailUsageExample.java` 文件，包含：
- 示例1：发送简单文本邮件
- 示例2：使用模板发送文本邮件
- 示例3：使用模板发送HTML邮件
- 示例4：发送给多个收件人
- 示例5：发送带附件的邮件
- 示例6：给多个收件人发送带附件的邮件
- 示例7：发送自定义HTML邮件
- 示例8：发送带内嵌图片的邮件
- 示例9：实际业务场景示例

## 六、注意事项

1. **授权码 vs 登录密码**：大多数邮箱需要使用授权码，不是邮箱登录密码
2. **端口选择**：SSL端口通常为465，TLS端口通常为587
3. **防火墙**：确保服务器可以访问SMTP服务器的对应端口
4. **发送频率**：避免短时间内发送大量邮件，可能被邮箱服务商限制
5. **错误处理**：工具类方法返回boolean，建议根据返回值做相应处理
6. **日志记录**：工具类已集成日志，发送成功/失败会自动记录
7. **附件大小**：注意附件大小限制，一般邮箱单封邮件不超过20MB
8. **文件路径**：确保附件文件路径正确且文件存在

## 七、常见问题

### Q1: 发送邮件失败，提示"535 Authentication failed"
**A**: 这通常是因为：
- 使用了邮箱登录密码而非授权码
- 授权码输入错误
- 邮箱未开启SMTP服务

### Q2: 发送邮件超时
**A**: 可能原因：
- 服务器无法访问SMTP服务器（检查网络/防火墙）
- SMTP端口配置错误
- 邮箱服务商限制了IP访问

### Q3: 附件发送失败
**A**: 检查：
- 文件路径是否正确
- 文件是否存在
- 文件大小是否超过限制
- 文件是否被占用

### Q4: 邮件被识别为垃圾邮件
**A**: 建议：
- 使用企业邮箱发送
- 避免在内容中使用过多营销性词汇
- 不要频繁发送
- 配置SPF/DKIM记录（需要域名管理权限）

## 八、技术支持

如有问题，请联系技术支持团队或查看项目文档。
