package com.youkang.system.domain.req.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "客户科目组查询请求")
@Getter
@Setter
public class CustomerSubjectGroupReq {

    @Schema(description = "客户名称", example = "张三公司")
    private String customerName;

    @Schema(description = "科目组名称", example = "科目组A")
    private String subjectGroupName;

    @Schema(description = "联系电话", example = "13888888888")
    private String phone;

    @Schema(description = "客户地址", example = "北京市朝阳区")
    private String customerAddress;
}
