package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 返还记录查询请求
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "返还记录查询请求")
public class ReimburseRecordQueryReq {

    @Schema(description = "页码", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页数量", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "客户姓名")
    private String customerName;

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "返还类型")
    private String reimburseType;
}
