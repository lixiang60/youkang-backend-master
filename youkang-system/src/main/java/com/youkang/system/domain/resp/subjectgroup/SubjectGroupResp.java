package com.youkang.system.domain.resp.subjectgroup;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 课题组信息响应
 *
 * @author youkang
 */
@Schema(description = "课题组信息响应")
@Data
public class SubjectGroupResp {

    @Schema(description = "课题组ID", example = "1")
    private Integer id;

    @Schema(description = "课题组名称", example = "课题组A")
    private String name;

    @Schema(description = "地区", example = "北京")
    private String region;

    @Schema(description = "业务员", example = "张三")
    private String salesPerson;

    @Schema(description = "结算方式编码", example = "monthly")
    private String paymentMethod;

    @Schema(description = "结算方式名称", example = "月结")
    private String paymentMethodName;

    @Schema(description = "发票抬头", example = "XX科技有限公司")
    private String invoiceTitle;

    @Schema(description = "所属公司ID", example = "1")
    private String companyId;

    @Schema(description = "所属公司名称", example = "XX公司")
    private String companyName;

    @Schema(description = "联系人", example = "李四")
    private String contactPerson;

    @Schema(description = "联系电话", example = "13888888888")
    private String contactPhone;

    @Schema(description = "联系地址", example = "北京市朝阳区XX路XX号")
    private String contactAddress;
}
