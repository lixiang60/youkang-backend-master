package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlateNoCommonReq {

    @Schema(description = "板号")
    private String plateNo;

    @Schema(description = "机器号")
    private String machineNo;

    @Schema(description = "备注")
    private String remark;
}
