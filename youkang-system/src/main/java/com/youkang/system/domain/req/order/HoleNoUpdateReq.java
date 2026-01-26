package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoleNoUpdateReq {

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "样品编号")
    private String sampleId;

    @Schema(description = "模板板号")
    private String templatePlateNo;

    @Schema(description = "模板孔号")
    private String templateHoleNo;
}
