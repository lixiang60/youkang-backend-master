package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 反应生产-设置原浓度请求
 */
@Getter
@Setter
@Schema(description = "反应生产-设置原浓度请求")
public class ReactionProduceOriginConcentrationReq {

    @Schema(description = "生产编号列表")
    private List<Long> produceIdList;

    @Schema(description = "原浓度")
    private String originConcentration;
}
