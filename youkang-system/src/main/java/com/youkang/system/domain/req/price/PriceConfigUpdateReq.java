package com.youkang.system.domain.req.price;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 价格配置修改请求
 *
 * @author youkang
 */
@Data
@Schema(description = "价格配置修改请求")
public class PriceConfigUpdateReq {

    @Schema(description = "主键ID（修改自定义价格时传入）")
    private Long id;

    @Schema(description = "课题组ID")
    @NotNull(message = "课题组ID不能为空")
    private Integer groupId;

    @Schema(description = "类别")
    private String category;

    @Schema(description = "收费名称")
    private String chargeName;

    @Schema(description = "样品类型")
    private String sampleType;

    @Schema(description = "测序项目")
    private String project;

    @Schema(description = "质粒长度下限")
    private Integer plasmidLengthMin;

    @Schema(description = "质粒长度上限")
    private Integer plasmidLengthMax;

    @Schema(description = "片段大小下限")
    private Integer fragmentSizeMin;

    @Schema(description = "片段大小上限")
    private Integer fragmentSizeMax;

    @Schema(description = "单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;

    @Schema(description = "计算方式")
    private String calcMethod;

    @Schema(description = "状态（1启用 0禁用）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
