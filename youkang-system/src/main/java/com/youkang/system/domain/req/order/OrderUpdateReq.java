package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单更新请求
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "订单更新请求")
public class OrderUpdateReq {

    @Schema(description = "订单id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderId;

    @Schema(description = "客户id")
    private Integer customerId;

    @Schema(description = "课题组id")
    private Integer groupId;

    @Schema(description = "订单类型")
    private String orderType;

    @Schema(description = "是否同步（0:同步康为，1:不同步）")
    private Integer isAsync;

    @Schema(description = "测序代数（1代：1，4代：4）")
    private Integer generation;

    @Schema(description = "所属公司")
    private String belongCompany;

    @Schema(description = "生产公司")
    private String produceCompany;

    @Schema(description = "关联基因号")
    private String genNo;

    @Schema(description = "备注")
    private String remark;
}
