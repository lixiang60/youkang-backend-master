package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SampleReturnReq {

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "生产编号列表")
    private List<Long> produceIdList;

    @Schema(description = "返还类型")
    private String reimburseType;
}
