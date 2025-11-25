package com.youkang.common.utils.mail;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 邮件工具类使用示例
 * <p>
 * 此类展示了如何使用 MailUtils 和 MailTemplate 发送企业邮件
 * 注意：这是一个示例类，实际使用时可以在任何需要发送邮件的地方注入 MailUtils
 *
 * @author youkang
 * @date 2025-11-25
 */
@Component
public class MailUsageExample {

    @Resource
    private MailUtils mailUtils;

    /**
     * 示例1：发送简单文本邮件（不带附件）
     */
    public void example1_SendSimpleText() {
        boolean success = mailUtils.sendSimpleText(
                "recipient@example.com",
                "测试邮件",
                "这是一封简单的测试邮件"
        );
        System.out.println("发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 示例2：使用模板发送纯文本邮件（不带附件）
     */
    public void example2_SendTextByTemplate() {
        MailTemplate template = MailTemplate.builder()
                .title("系统通知")
                .recipientName("张三")
                .content("您的账号已成功激活，现在可以登录系统了。\n\n如有任何问题，请联系我们。")
                .senderName("有康科技有限公司")
                .contactPhone("400-123-4567")
                .contactEmail("support@youkang.com")
                .includeTimestamp(true)
                .build();

        boolean success = mailUtils.sendTextByTemplate(
                "zhangsan@example.com",
                "账号激活通知",
                template
        );
        System.out.println("发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 示例3：使用模板发送HTML格式邮件（不带附件）
     */
    public void example3_SendHtmlByTemplate() {
        MailTemplate template = MailTemplate.builder()
                .title("订单确认")
                .recipientName("李四")
                .content("感谢您的购买！\n\n" +
                        "订单号：YK20251125001\n" +
                        "订单金额：￥1,299.00\n" +
                        "预计发货时间：2025-11-26\n\n" +
                        "您可以登录系统查看订单详情。")
                .senderName("有康电商平台")
                .contactPhone("400-888-6666")
                .contactEmail("order@youkang.com")
                .includeTimestamp(true)
                .build();

        boolean success = mailUtils.sendHtmlByTemplate(
                "lisi@example.com",
                "【有康】订单确认通知",
                template
        );
        System.out.println("发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 示例4：发送HTML格式邮件给多个收件人（不带附件）
     */
    public void example4_SendHtmlToMultipleRecipients() {
        MailTemplate template = MailTemplate.builder()
                .title("会议通知")
                .recipientName("全体员工")
                .content("定于明天上午9:00在会议室召开月度总结会议。\n\n" +
                        "会议主题：11月工作总结与12月工作计划\n" +
                        "参会人员：全体员工\n" +
                        "会议地点：3楼大会议室\n\n" +
                        "请准时参加，谢谢！")
                .senderName("行政部")
                .contactPhone("内线：8001")
                .includeTimestamp(true)
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
        System.out.println("发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 示例5：使用模板发送带附件的邮件（单个收件人）
     */
    public void example5_SendWithAttachmentByTemplate() {
        MailTemplate template = MailTemplate.builder()
                .title("合同文件")
                .recipientName("王总")
                .content("您好！\n\n" +
                        "附件是本次合作的合同文件，请查收。\n\n" +
                        "如有任何问题，欢迎随时联系我们。")
                .senderName("有康法务部")
                .contactPhone("010-12345678")
                .contactEmail("legal@youkang.com")
                .includeTimestamp(true)
                .build();

        // 准备附件
        File contract = new File("D:/contracts/contract_2025_001.pdf");
        File annex = new File("D:/contracts/annex_001.pdf");

        boolean success = mailUtils.sendWithAttachmentByTemplate(
                "wangzong@example.com",
                "【有康】合作合同",
                template,
                contract,
                annex  // 可以添加多个附件
        );
        System.out.println("发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 示例6：使用模板发送带附件的邮件给多个收件人
     */
    public void example6_SendWithAttachmentToMultipleRecipients() {
        MailTemplate template = MailTemplate.builder()
                .title("月度报表")
                .recipientName("各部门负责人")
                .content("您好！\n\n" +
                        "附件是11月份的月度经营报表，请各位查阅。\n\n" +
                        "如有疑问，请及时反馈。")
                .senderName("财务部")
                .contactPhone("内线：8002")
                .contactEmail("finance@youkang.com")
                .includeTimestamp(true)
                .build();

        String[] recipients = {
                "dept1@youkang.com",
                "dept2@youkang.com",
                "dept3@youkang.com"
        };

        File report = new File("D:/reports/2025-11-monthly-report.xlsx");

        boolean success = mailUtils.sendWithAttachmentByTemplate(
                recipients,
                "【财务】11月月度报表",
                template,
                report
        );
        System.out.println("发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 示例7：发送自定义HTML内容的邮件（不使用模板，不带附件）
     */
    public void example7_SendCustomHtml() {
        String customHtml = "<html><body>" +
                "<h2 style='color: blue;'>自定义HTML邮件</h2>" +
                "<p>这是一封使用自定义HTML格式的邮件。</p>" +
                "<ul>" +
                "<li>支持各种HTML标签</li>" +
                "<li>可以自定义样式</li>" +
                "<li>灵活性更高</li>" +
                "</ul>" +
                "</body></html>";

        boolean success = mailUtils.sendHtml(
                "custom@example.com",
                "自定义HTML邮件",
                customHtml
        );
        System.out.println("发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 示例8：发送带内嵌图片的邮件
     */
    public void example8_SendWithInlineImage() {
        String htmlWithImage = "<html><body>" +
                "<h2>产品介绍</h2>" +
                "<p>以下是我们的新产品：</p>" +
                "<img src='cid:productImage' width='400'/>" +
                "<p>欢迎咨询！</p>" +
                "</body></html>";

        File imageFile = new File("D:/images/product.jpg");

        boolean success = mailUtils.sendWithInlineImage(
                "customer@example.com",
                "新产品介绍",
                htmlWithImage,
                "productImage",  // 对应HTML中的cid
                imageFile
        );
        System.out.println("发送结果：" + (success ? "成功" : "失败"));
    }

    /**
     * 示例9：在实际业务中使用（比如在Service层）
     *
     * 假设这是一个用户注册后发送欢迎邮件的场景
     */
    public void example9_RealWorldUsage(String userEmail, String userName) {
        MailTemplate template = MailTemplate.builder()
                .title("欢迎加入有康")
                .recipientName(userName)
                .content("恭喜您成功注册有康账号！\n\n" +
                        "现在您可以：\n" +
                        "- 浏览和购买商品\n" +
                        "- 管理个人信息\n" +
                        "- 查看订单历史\n\n" +
                        "如需帮助，请随时联系我们的客服团队。\n\n" +
                        "祝您购物愉快！")
                .senderName("有康客服团队")
                .contactPhone("400-999-8888")
                .contactEmail("service@youkang.com")
                .includeTimestamp(true)
                .build();

        mailUtils.sendHtmlByTemplate(
                userEmail,
                "【有康】欢迎您的加入",
                template
        );
    }
}
