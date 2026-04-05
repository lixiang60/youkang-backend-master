package com.youkang.system.domain.resp.price;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 价格配置响应对象
 *
 * @author youkang
 */
@Data
@Schema(description = "价格配置响应对象")
public class PriceConfigResp {

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "自定义价格ID")
    private Long customId;

    @Schema(description = "课题组ID")
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
    private BigDecimal unitPrice;

    @Schema(description = "计算方式")
    private String calcMethod;

    @Schema(description = "状态（1启用 0禁用）")
    private Integer status;

    @Schema(description = "是否自定义（1是 0否）")
    private Integer isCustom;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "备注")
    private String remark;
}
