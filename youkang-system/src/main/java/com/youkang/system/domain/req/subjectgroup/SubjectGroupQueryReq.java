package com.youkang.system.domain.req.subjectgroup;

import com.youkang.common.core.domain.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课题组信息查询请求
 *
 * @author youkang
 */
@Schema(description = "课题组信息查询请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class SubjectGroupQueryReq extends PageReq {

    @Schema(description = "课题组名称", example = "课题组A")
    private String name;

    @Schema(description = "地区", example = "北京")
    private String region;

    @Schema(description = "业务员", example = "张三")
    private String salesPerson;

    @Schema(description = "结算方式", example = "monthly")
    private String paymentMethod;

    @Schema(description = "联系人", example = "李四")
    private String contactPerson;

    @Schema(description = "联系电话", example = "13888888888")
    private String contactPhone;
}
