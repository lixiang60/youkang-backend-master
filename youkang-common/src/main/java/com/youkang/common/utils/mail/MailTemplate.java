package com.youkang.common.utils.mail;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 邮件模板类 - 用于生成固定格式的邮件内容
 *
 * @author youkang
 * @date 2025-11-25
 */
@Data
@Builder
public class MailTemplate {

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 收件人姓名
     */
    private String recipientName;

    /**
     * 邮件主要内容
     */
    private String content;

    /**
     * 发件人姓名/公司名称
     */
    private String senderName;

    /**
     * 联系电话（可选）
     */
    private String contactPhone;

    /**
     * 联系邮箱（可选）
     */
    private String contactEmail;

    /**
     * 是否包含时间戳（默认true）
     */
    @Builder.Default
    private Boolean includeTimestamp = true;

    /**
     * 生成HTML格式的邮件内容
     *
     * @return HTML格式的邮件内容
     */
    public String generateHtmlContent() {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: 'Microsoft YaHei', Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f9f9f9; }");
        html.append(".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }");
        html.append(".content { background-color: white; padding: 30px; border-radius: 0 0 5px 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        html.append(".greeting { font-size: 16px; margin-bottom: 20px; }");
        html.append(".main-content { font-size: 14px; line-height: 1.8; margin-bottom: 30px; white-space: pre-wrap; }");
        html.append(".footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; font-size: 12px; color: #666; }");
        html.append(".contact-info { margin-top: 15px; }");
        html.append(".timestamp { margin-top: 20px; font-size: 12px; color: #999; font-style: italic; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        // 邮件头部
        html.append("<div class='header'>");
        html.append("<h2 style='margin: 0;'>").append(title != null ? title : "有康企业邮件").append("</h2>");
        html.append("</div>");

        // 邮件内容
        html.append("<div class='content'>");

        // 问候语
        if (recipientName != null && !recipientName.trim().isEmpty()) {
            html.append("<div class='greeting'>");
            html.append("尊敬的 <strong>").append(recipientName).append("</strong>：");
            html.append("</div>");
        }

        // 主要内容
        html.append("<div class='main-content'>");
        html.append(content != null ? content : "");
        html.append("</div>");

        // 页脚 - 发件人信息
        html.append("<div class='footer'>");
        if (senderName != null && !senderName.trim().isEmpty()) {
            html.append("<div><strong>").append(senderName).append("</strong></div>");
        }

        if (contactPhone != null && !contactPhone.trim().isEmpty() ||
            contactEmail != null && !contactEmail.trim().isEmpty()) {
            html.append("<div class='contact-info'>");
            if (contactPhone != null && !contactPhone.trim().isEmpty()) {
                html.append("<div>联系电话：").append(contactPhone).append("</div>");
            }
            if (contactEmail != null && !contactEmail.trim().isEmpty()) {
                html.append("<div>联系邮箱：").append(contactEmail).append("</div>");
            }
            html.append("</div>");
        }

        // 时间戳
        if (includeTimestamp) {
            html.append("<div class='timestamp'>");
            html.append("发送时间：").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            html.append("</div>");
        }

        html.append("</div>"); // footer
        html.append("</div>"); // content
        html.append("</div>"); // container
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    /**
     * 生成纯文本格式的邮件内容
     *
     * @return 纯文本格式的邮件内容
     */
    public String generateTextContent() {
        StringBuilder text = new StringBuilder();

        // 标题
        if (title != null && !title.trim().isEmpty()) {
            text.append("【").append(title).append("】\n\n");
        }

        // 问候语
        if (recipientName != null && !recipientName.trim().isEmpty()) {
            text.append("尊敬的 ").append(recipientName).append("：\n\n");
        }

        // 主要内容
        if (content != null) {
            text.append(content).append("\n\n");
        }

        // 分隔线
        text.append("----------------------------------------\n\n");

        // 发件人信息
        if (senderName != null && !senderName.trim().isEmpty()) {
            text.append(senderName).append("\n");
        }

        if (contactPhone != null && !contactPhone.trim().isEmpty()) {
            text.append("联系电话：").append(contactPhone).append("\n");
        }

        if (contactEmail != null && !contactEmail.trim().isEmpty()) {
            text.append("联系邮箱：").append(contactEmail).append("\n");
        }

        // 时间戳
        if (includeTimestamp) {
            text.append("\n发送时间：").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        return text.toString();
    }
}
