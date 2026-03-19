package com.youkang.system.domain.resp.order;

import com.youkang.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 模板失败样品响应
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "模板失败样品响应")
public class TemplateFailedResp {

    @Excel(name = "客户ID")
    @Schema(description = "客户ID")
    private Integer customerId;

    @Excel(name = "客户姓名")
    @Schema(description = "客户姓名")
    private String customerName;

    @Excel(name = "客户邮箱")
    @Schema(description = "客户邮箱")
    private String customerEmail;

    @Excel(name = "送样日期")
    @Schema(description = "送样日期")
    private LocalDateTime createTime;

    @Excel(name = "订单号")
    @Schema(description = "订单号")
    private String orderId;

    @Excel(name = "样品编号")
    @Schema(description = "样品编号")
    private String sampleId;

    @Excel(name = "模板状态")
    @Schema(description = "模板状态")
    private String returnState;

    @Excel(name = "失败原因")
    @Schema(description = "失败原因")
    private String remark;
}
