package com.youkang.common.utils.mail;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;

/**
 * 邮件发送工具类
 * <p>
 * 提供两种发送方式：
 * 1. 发送纯文本/HTML邮件（不带附件）
 * 2. 发送带附件的邮件
 * <p>
 * 支持使用 MailTemplate 生成固定格式的邮件内容
 *
 * @author youkang
 * @date 2025-11-25
 */
@Component
public class MailUtils {

    private static final Logger log = LoggerFactory.getLogger(MailUtils.class);

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送简单文本邮件（不带附件）
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容（纯文本）
     * @return 是否发送成功
     */
    public boolean sendSimpleText(String to, String subject, String content) {
        return sendSimpleText(new String[]{to}, subject, content);
    }

    /**
     * 发送简单文本邮件给多个收件人（不带附件）
     *
     * @param toArray 收件人邮箱数组
     * @param subject 邮件主题
     * @param content 邮件内容（纯文本）
     * @return 是否发送成功
     */
    public boolean sendSimpleText(String[] toArray, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(toArray);
            message.setSubject(subject);
            message.setText(content);

            mailSender.send(message);
            log.info("简单文本邮件发送成功！收件人：{}", Arrays.toString(toArray));
            return true;
        } catch (Exception e) {
            log.error("简单文本邮件发送失败！收件人：{}, 错误信息：{}", Arrays.toString(toArray), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 使用模板发送纯文本邮件（不带附件）
     *
     * @param to       收件人邮箱
     * @param subject  邮件主题
     * @param template 邮件模板
     * @return 是否发送成功
     */
    public boolean sendTextByTemplate(String to, String subject, MailTemplate template) {
        return sendSimpleText(to, subject, template.generateTextContent());
    }

    /**
     * 发送HTML格式邮件（不带附件）
     *
     * @param to          收件人邮箱
     * @param subject     邮件主题
     * @param htmlContent HTML格式的邮件内容
     * @return 是否发送成功
     */
    public boolean sendHtml(String to, String subject, String htmlContent) {
        return sendHtml(new String[]{to}, subject, htmlContent);
    }

    /**
     * 发送HTML格式邮件给多个收件人（不带附件）
     *
     * @param toArray     收件人邮箱数组
     * @param subject     邮件主题
     * @param htmlContent HTML格式的邮件内容
     * @return 是否发送成功
     */
    public boolean sendHtml(String[] toArray, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(from);
            helper.setTo(toArray);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true表示HTML格式

            mailSender.send(message);
            log.info("HTML邮件发送成功！收件人：{}", Arrays.toString(toArray));
            return true;
        } catch (MessagingException e) {
            log.error("HTML邮件发送失败！收件人：{}, 错误信息：{}", Arrays.toString(toArray), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 使用模板发送HTML格式邮件（不带附件）
     *
     * @param to       收件人邮箱
     * @param subject  邮件主题
     * @param template 邮件模板
     * @return 是否发送成功
     */
    public boolean sendHtmlByTemplate(String to, String subject, MailTemplate template) {
        return sendHtml(to, subject, template.generateHtmlContent());
    }

    /**
     * 使用模板发送HTML格式邮件给多个收件人（不带附件）
     *
     * @param toArray  收件人邮箱数组
     * @param subject  邮件主题
     * @param template 邮件模板
     * @return 是否发送成功
     */
    public boolean sendHtmlByTemplate(String[] toArray, String subject, MailTemplate template) {
        return sendHtml(toArray, subject, template.generateHtmlContent());
    }

    /**
     * 发送带附件的邮件
     *
     * @param to          收件人邮箱
     * @param subject     邮件主题
     * @param htmlContent HTML格式的邮件内容
     * @param attachments 附件文件数组
     * @return 是否发送成功
     */
    public boolean sendWithAttachment(String to, String subject, String htmlContent, File... attachments) {
        return sendWithAttachment(new String[]{to}, subject, htmlContent, attachments);
    }

    /**
     * 发送带附件的邮件给多个收件人
     *
     * @param toArray     收件人邮箱数组
     * @param subject     邮件主题
     * @param htmlContent HTML格式的邮件内容
     * @param attachments 附件文件数组
     * @return 是否发送成功
     */
    public boolean sendWithAttachment(String[] toArray, String subject, String htmlContent, File... attachments) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // true表示支持多部分（multipart），即支持附件
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(toArray);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true表示HTML格式

            // 添加附件
            if (attachments != null && attachments.length > 0) {
                for (File file : attachments) {
                    if (file != null && file.exists()) {
                        helper.addAttachment(file.getName(), file);
                        log.info("添加附件：{}", file.getName());
                    } else {
                        log.warn("附件不存在或为空：{}", file != null ? file.getName() : "null");
                    }
                }
            }

            mailSender.send(message);
            log.info("带附件邮件发送成功！收件人：{}", Arrays.toString(toArray));
            return true;
        } catch (MessagingException e) {
            log.error("带附件邮件发送失败！收件人：{}, 错误信息：{}", Arrays.toString(toArray), e.getMessage(), e);
            return false;
        }
    }

    /**
     * 使用模板发送带附件的邮件
     *
     * @param to          收件人邮箱
     * @param subject     邮件主题
     * @param template    邮件模板
     * @param attachments 附件文件数组
     * @return 是否发送成功
     */
    public boolean sendWithAttachmentByTemplate(String to, String subject, MailTemplate template, File... attachments) {
        return sendWithAttachment(to, subject, template.generateHtmlContent(), attachments);
    }

    /**
     * 使用模板发送带附件的邮件给多个收件人
     *
     * @param toArray     收件人邮箱数组
     * @param subject     邮件主题
     * @param template    邮件模板
     * @param attachments 附件文件数组
     * @return 是否发送成功
     */
    public boolean sendWithAttachmentByTemplate(String[] toArray, String subject, MailTemplate template, File... attachments) {
        return sendWithAttachment(toArray, subject, template.generateHtmlContent(), attachments);
    }

    /**
     * 发送带内嵌图片的HTML邮件
     *
     * @param to          收件人邮箱
     * @param subject     邮件主题
     * @param htmlContent HTML内容（使用cid引用图片，如：<img src='cid:imageId'>）
     * @param imageId     图片ID（对应HTML中的cid）
     * @param imageFile   图片文件
     * @return 是否发送成功
     */
    public boolean sendWithInlineImage(String to, String subject, String htmlContent, String imageId, File imageFile) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // 添加内嵌图片
            if (imageFile != null && imageFile.exists()) {
                helper.addInline(imageId, imageFile);
            }

            mailSender.send(message);
            log.info("带内嵌图片的邮件发送成功！收件人：{}", to);
            return true;
        } catch (MessagingException e) {
            log.error("带内嵌图片的邮件发送失败！收件人：{}, 错误信息：{}", to, e.getMessage(), e);
            return false;
        }
    }
}
