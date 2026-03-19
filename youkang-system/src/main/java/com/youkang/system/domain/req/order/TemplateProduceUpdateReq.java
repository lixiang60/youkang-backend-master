package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TemplateProduceUpdateReq {

    @Schema(description = "订单号")
    private List<Long> produceIdList;

    @Schema(description = "返回状态")
    private String returnState;

    @Schema(description = "备注")
    private String remark;
}
