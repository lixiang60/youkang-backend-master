package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "添加单个样品信息")
public class SampleAddReq {

    @Schema(description = "订单ID")
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    @Schema(description = "样品编号")
    @NotBlank(message = "样品编号不能为空")
    private String sampleId;

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
