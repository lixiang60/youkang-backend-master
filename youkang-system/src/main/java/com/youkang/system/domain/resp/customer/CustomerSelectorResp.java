package com.youkang.system.domain.resp.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "客户选择器响应")
@Getter
@Setter
public class CustomerSelectorResp {

    @Schema(description = "客户ID", example = "1")
    private Integer customerId;

    @Schema(description = "客户名称", example = "北京有康科技有限公司")
    private String customerName;

    @Schema(description = "科目组ID", example = "1")
    private Integer subjectGroupId;

    @Schema(description = "科目组名称", example = "科目组A")
    private String subjectGroupName;

    @Schema(description = "客户地址", example = "北京市朝阳区")
    private String address;

    @Schema(description = "电子邮箱", example = "contact@youkang.com")
    private String email;

    @Schema(description = "联系电话", example = "13888888888")
    private String phone;

    @Schema(description = "客户单位", example = "北京有康科技有限公司")
    private String customerUnit;

    @Schema(description = "结果字符串")
    private String resultString;
}
