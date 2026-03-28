package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 反应生产-单个添加孔号请求
 */
@Getter
@Setter
@Schema(description = "反应生产-单个添加孔号请求")
public class ReactionProduceHoleNoReq {

    @Schema(description = "生产编号")
    private Long produceId;

    @Schema(description = "板号")
    private String plateNo;

    @Schema(description = "孔号")
    private String holeNo;

    @Schema(description = "备注")
    private String remark;
}
