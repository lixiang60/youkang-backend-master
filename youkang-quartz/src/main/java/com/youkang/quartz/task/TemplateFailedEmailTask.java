package com.youkang.quartz.task;

import com.youkang.common.utils.mail.MailUtils;
import com.youkang.system.domain.resp.order.TemplateFailedResp;
import com.youkang.system.service.order.ISampleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模板失败邮件通知定时任务
 *
 * @author youkang
 */
@Component("templateFailedEmailTask")
@Slf4j
public class TemplateFailedEmailTask {

    @Autowired
    private ISampleInfoService sampleInfoService;

    @Autowired
    private MailUtils mailUtils;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    /**
     * 发送模板失败邮件通知
     * 此方法可配置为定时任务，定期检查并发送邮件
     */
    public void sendTemplateFailedEmail() {
        log.info("开始执行模板失败邮件通知定时任务...");

        try {
            // 1. 查询所有流程在模板邮件的样品
            List<TemplateFailedResp> failedList = sampleInfoService.queryTemplateFailedList();

            if (failedList == null || failedList.isEmpty()) {
                log.info("没有需要发送的模板失败通知邮件");
                return;
            }

            // 2. 按客户ID分组
            Map<Integer, List<TemplateFailedResp>> customerGroupMap = failedList.stream()
                    .collect(Collectors.groupingBy(TemplateFailedResp::getCustomerId));

            // 3. 为每个客户发送一封邮件
            int successCount = 0;
            int failCount = 0;

            for (Map.Entry<Integer, List<TemplateFailedResp>> entry : customerGroupMap.entrySet()) {
                List<TemplateFailedResp> customerSamples = entry.getValue();
                TemplateFailedResp firstSample = customerSamples.get(0);

                String customerName = firstSample.getCustomerName();
                String customerEmail = firstSample.getCustomerEmail();

                if (customerEmail == null || customerEmail.trim().isEmpty()) {
                    log.warn("客户[{}]邮箱为空，跳过发送", customerName);
                    failCount++;
                    continue;
                }

                // 获取第一个样品的送样日期
                String sampleDate = firstSample.getCreateTime() != null
                        ? firstSample.getCreateTime().format(DATE_TIME_FORMATTER)
                        : "";

                // 4. 构建邮件内容
                String emailContent = buildEmailContent(customerName, sampleDate, customerSamples);

                // 5. 发送邮件
                String subject = "模板制作失败通知 - " + customerName;
                boolean result = mailUtils.sendHtml(customerEmail, subject, emailContent);

                if (result) {
                    successCount++;
                    log.info("成功发送邮件给客户[{}]，邮箱：{}", customerName, customerEmail);
                } else {
                    failCount++;
                    log.error("发送邮件给客户[{}]失败，邮箱：{}", customerName, customerEmail);
                }
            }

            log.info("模板失败邮件通知定时任务执行完成，成功：{}，失败：{}", successCount, failCount);

        } catch (Exception e) {
            log.error("模板失败邮件通知定时任务执行异常", e);
        }
    }

    /**
     * 构建邮件内容
     *
     * @param customerName 客户姓名
     * @param sampleDate   送样日期
     * @param sampleList   失败样品列表
     * @return HTML格式的邮件内容
     */
    private String buildEmailContent(String customerName, String sampleDate, List<TemplateFailedResp> sampleList) {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: 'Microsoft YaHei', Arial, sans-serif; line-height: 1.6; color: #333; }");
        html.append(".container { max-width: 800px; margin: 0 auto; padding: 20px; }");
        html.append(".header { background-color: #f56c6c; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }");
        html.append(".content { background-color: #f9f9f9; padding: 30px; border: 1px solid #eee; }");
        html.append(".greeting { font-size: 16px; margin-bottom: 20px; }");
        html.append(".notice { background-color: #fef0f0; border: 1px solid #fde2e2; padding: 15px; border-radius: 5px; margin-bottom: 20px; color: #f56c6c; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        html.append("th { background-color: #f56c6c; color: white; }");
        html.append("tr:nth-child(even) { background-color: #f9f9f9; }");
        html.append(".footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; font-size: 12px; color: #666; text-align: center; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        // 邮件头部
        html.append("<div class='header'>");
        html.append("<h2 style='margin: 0;'>模板制作失败通知</h2>");
        html.append("</div>");

        // 邮件内容
        html.append("<div class='content'>");

        // 问候语
        html.append("<div class='greeting'>");
        html.append("尊敬的 <strong>").append(customerName).append("</strong> 老师，你好！");
        html.append("</div>");

        // 通知内容
        html.append("<div class='notice'>");
        html.append("您 <strong>").append(sampleDate).append("</strong> 的样品有制作模板失败，无法进行测序实验，需要重新送样。");
        html.append("<br>具体明细和原因见以下表格：");
        html.append("</div>");

        // 明细表格
        html.append("<table>");
        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>送样日期</th>");
        html.append("<th>订单号</th>");
        html.append("<th>样品编号</th>");
        html.append("<th>模板状态</th>");
        html.append("<th>失败原因</th>");
        html.append("</tr>");
        html.append("</thead>");
        html.append("<tbody>");

        for (TemplateFailedResp sample : sampleList) {
            html.append("<tr>");
            html.append("<td>").append(sample.getCreateTime() != null ? sample.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "").append("</td>");
            html.append("<td>").append(sample.getOrderId() != null ? sample.getOrderId() : "").append("</td>");
            html.append("<td>").append(sample.getSampleId() != null ? sample.getSampleId() : "").append("</td>");
            html.append("<td>").append(sample.getReturnState() != null ? sample.getReturnState() : "").append("</td>");
            html.append("<td>").append(sample.getRemark() != null ? sample.getRemark() : "").append("</td>");
            html.append("</tr>");
        }

        html.append("</tbody>");
        html.append("</table>");

        // 页脚
        html.append("<div class='footer'>");
        html.append("此邮件为系统自动发送，请勿直接回复。<br>");
        html.append("如有疑问，请联系客服。");
        html.append("</div>");

        html.append("</div>"); // content
        html.append("</div>"); // container
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
