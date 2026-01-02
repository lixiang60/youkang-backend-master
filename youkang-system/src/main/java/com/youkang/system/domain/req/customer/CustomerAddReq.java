package com.youkang.system.domain.req.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 客户信息新增请求
 *
 * @author youkang
 */
@Schema(description = "客户信息新增请求")
@Data
public class CustomerAddReq {

    @Schema(description = "客户姓名", example = "张三", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "客户姓名不能为空")
    private String customerName;

    @Schema(description = "课题组ID", example = "1")
    private Integer subjectGroupId;

    @Schema(description = "地区", example = "北京")
    private String region;

    @Schema(description = "地址", example = "北京市朝阳区XX路XX号")
    private String address;

    @Schema(description = "电话", example = "13888888888")
    private String phone;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

    @Schema(description = "微信ID", example = "wx_zhangsan")
    private String wechatId;

    @Schema(description = "客户等级", example = "VIP")
    private String customerLevel;

    @Schema(description = "状态", example = "1")
    private String status;

    @Schema(description = "销售员", example = "李四")
    private String salesPerson;

    @Schema(description = "客户单位", example = "XX科技公司")
    private String customerUnit;

    @Schema(description = "结算方式", example = "monthly")
    private String paymentMethod;

    @Schema(description = "发票种类", example = "增值税专用发票")
    private String invoiceType;

    @Schema(description = "备注", example = "重要客户")
    private String remarks;

    @Schema(description = "所属公司", example = "有康科技")
    private String company;
}
