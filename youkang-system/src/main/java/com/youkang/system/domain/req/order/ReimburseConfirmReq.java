package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


/**
 * 返还确认请求
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "返还管理-安排返还请求")
public class ReimburseConfirmReq {
    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "生产编号集")
    private String produceIds;

    @Schema(description = "返还类型")
    private String reimburseType;
}
