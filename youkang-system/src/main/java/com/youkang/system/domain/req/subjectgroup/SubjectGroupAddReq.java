package com.youkang.system.domain.req.subjectgroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 课题组信息新增请求
 *
 * @author youkang
 */
@Schema(description = "课题组信息新增请求")
@Data
public class SubjectGroupAddReq {

    @Schema(description = "课题组名称", example = "课题组A", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "课题组名称不能为空")
    private String name;

    @Schema(description = "地区", example = "北京")
    private String region;

    @Schema(description = "业务员", example = "张三")
    private String salesPerson;

    @Schema(description = "结算方式", example = "monthly")
    private String paymentMethod;

    @Schema(description = "发票抬头", example = "XX科技有限公司")
    private String invoiceTitle;

    @Schema(description = "所属公司ID", example = "1")
    private String companyId;

    @Schema(description = "联系人", example = "李四")
    private String contactPerson;

    @Schema(description = "联系电话", example = "13888888888")
    private String contactPhone;

    @Schema(description = "联系地址", example = "北京市朝阳区XX路XX号")
    private String contactAddress;
}
