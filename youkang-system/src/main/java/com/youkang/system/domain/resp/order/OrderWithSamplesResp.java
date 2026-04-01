package com.youkang.system.domain.resp.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 订单及样品信息响应
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "订单及样品信息响应")
public class OrderWithSamplesResp {

    @Schema(description = "订单信息")
    private OrderResp orderInfo;

    @Schema(description = "样品信息列表")
    private List<SampleResp> sampleList;
}
