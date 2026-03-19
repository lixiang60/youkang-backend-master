package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 重抽样品查询请求
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "重抽样品查询请求")
public class ResampleQueryReq {

    @Schema(description = "模板板号列表，多个用逗号分隔")
    private String templatePlateNos;

    @Schema(description = "所属实验室")
    private String belongLab;
}
