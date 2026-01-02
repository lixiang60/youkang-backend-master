package com.youkang.system.domain.req.customer;

import com.youkang.common.core.domain.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户信息查询请求
 *
 * @author youkang
 */
@Schema(description = "客户信息查询请求")
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerQueryReq extends PageReq {

    @Schema(description = "客户姓名", example = "张三")
    private String customerName;

    @Schema(description = "课题组ID", example = "1")
    private Integer subjectGroupId;

    @Schema(description = "地区", example = "北京")
    private String region;

    @Schema(description = "电话", example = "13888888888")
    private String phone;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String email;

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

    @Schema(description = "所属公司", example = "有康科技")
    private String company;
}
