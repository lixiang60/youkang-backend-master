package com.youkang.system.domain.req.order;

import com.youkang.common.core.domain.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SampleTemplateQueryReq extends PageReq {

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "样品编号")
    private String sampleId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "返回状态")
    private Integer returnState;

}
