package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OriginConcentrationUpdateReq {

    @Schema(description = "生产编号")
    private List<Long> produceIdList;

    @Schema(description = "原浓度")
    private String originConcentration;

    @Schema(description = "排版方式-横排；竖排")
    private String templateStype;

    @Schema(description = "板号")
    private String plateNo;

    @Schema(description = "备注")
    private String remark;
}
