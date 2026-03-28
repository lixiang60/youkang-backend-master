package com.youkang.system.domain.req.order;

import com.youkang.common.core.domain.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateProduceQueryReq extends PageReq {

    @Schema(description = "模板板号")
    private String templateNo;

    @Schema(description = "生产id")
    private Long produceId;

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "样品编号")
    private String sampleId;

    @Schema(description = "样品类型")
    private String sampleType;

    @Schema(description = "创建人")
    private String createUser;
}
