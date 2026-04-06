package com.youkang.system.domain.resp.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单价格计算响应
 *
 * @author youkang
 */
@Data
@Schema(description = "订单价格计算响应")
public class OrderPriceCalcResp {

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "生产编号集合")
    private List<Long> produceIds;

    @Schema(description = "测序项目")
    private String project;

    @Schema(description = "样品编号")
    private String sampleId;

    @Schema(description = "样品类型")
    private String sampleType;

    @Schema(description = "片段大小")
    private String fragmentSize;

    @Schema(description = "质粒长度")
    private String plasmidLength;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "单价")
    private BigDecimal unitPrice;

    @Schema(description = "总价")
    private BigDecimal totalPrice;
}
