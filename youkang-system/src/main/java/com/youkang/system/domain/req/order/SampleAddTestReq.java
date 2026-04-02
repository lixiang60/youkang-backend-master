package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 样品加测请求对象
 *
 * @author youkang
 */
@Data
@Schema(description = "样品加测请求")
public class SampleAddTestReq {

    @Schema(description = "原生产编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long produceId;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderId;

    @Schema(description = "引物名称")
    private String primer;

    @Schema(description = "引物序列")
    private String seq;

    @Schema(description = "引物浓度")
    private String primerConcentration;

    @Schema(description = "备注")
    private String remark;
}
