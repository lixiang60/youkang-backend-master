package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 订单范围查询请求
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "订单范围查询请求")
public class OrderRangeQueryReq {

    @Schema(description = "开始订单号")
    private String startOrderId;

    @Schema(description = "结束订单号")
    private String endOrderId;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建开始时间")
    private LocalDateTime startTime;

    @Schema(description = "创建结束时间")
    private LocalDateTime endTime;

    @Schema(description = "所属公司")
    private String belongCompany;

    @Schema(description = "生产公司")
    private String produceCompany;
}
