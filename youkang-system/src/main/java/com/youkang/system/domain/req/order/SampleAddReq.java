package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "添加单个样品信息")
public class SampleAddReq {

    @Schema(description = "订单ID")
    private String orderId;

    @Schema(description = "样品编号")
    private String sampleIds;

    @Schema(description = "测序引物")
    private String primer;

    @Schema(description = "样品类型")
    private String sampleType;

    @Schema(description = "抗生素类型")
    private String antibioticType;

    @Schema(description = "载体名称")
    private String carrierName;

    @Schema(description = "片段大小")
    private String fragmentSize;

    @Schema(description = "是否测通")
    private String testResult;

    @Schema(description = "备注")
    private String remark;
}
