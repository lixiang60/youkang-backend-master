package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 测序BDT请求
 */
@Getter
@Setter
@Schema(description = "测序BDT请求")
public class SequencingBDTReq {

    @Schema(description = "板号")
    private String plateNo;
}
