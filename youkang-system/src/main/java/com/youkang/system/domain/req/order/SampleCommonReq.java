package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SampleCommonReq {

    @Schema(description = "生产id列表")
    private List<Long> produceIdList;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "备注")
    private String remark;
}
