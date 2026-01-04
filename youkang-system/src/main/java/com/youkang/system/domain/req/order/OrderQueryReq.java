package com.youkang.system.domain.req.order;

import com.youkang.common.core.domain.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单查询请求
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "订单查询请求")
public class OrderQueryReq extends PageReq {

    @Schema(description = "订单id")
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

    @Schema(description = "创建者")
    private String createBy;
}
