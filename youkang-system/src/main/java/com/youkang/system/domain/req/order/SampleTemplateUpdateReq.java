package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SampleTemplateUpdateReq {

    @Schema(description = "模板ID")
    private List<TemplateInfoReq> templateInfo;

    @Schema(description = "排版方式-横排；竖排")
    private String templateStype;

    @Schema(description = "模板板号")
    private String templatePlateNo;

    @Schema(description = "模板孔号")
    private String templateHoleNo;

    @Schema(description = "备注")
    private String remark;

}
