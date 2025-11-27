package com.youkang.system.domain.resp.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "客户科目组关联响应")
public class CustomerSubjectGroupResp {
    @Schema(description = "关联主键ID", example = "1")
    private Integer id;

    @Schema(description = "客户ID", example = "1")
    private Integer customerId;

    @Schema(description = "客户名称", example = "北京有康科技有限公司")
    private String customerName;

    @Schema(description = "区域", example = "华北")
    private String region;

    @Schema(description = "科目组ID", example = "1")
    private Integer subjectGroupId;

    @Schema(description = "科目组名称", example = "科目组A")
    private String subjectGroupName;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "支付方式", example = "月结")
    private String paymentMethod;

    @Schema(description = "销售人员", example = "张三")
    private String salesPerson;

    @Schema(description = "客户地址", example = "北京市朝阳区")
    private String customerAddress;

    @Schema(description = "客户电话", example = "13888888888")
    private String customerPhone;

    @Schema(description = "客户邮箱", example = "contact@youkang.com")
    private String customerEmail;

    @Schema(description = "创建人", example = "admin")
    private String createUser;

    @Schema(description = "创建时间", example = "2024-01-01T12:00:00")
    private LocalDateTime createTime;

    @Schema(description = "公司名称", example = "北京有康科技有限公司")
    private String company;

}
