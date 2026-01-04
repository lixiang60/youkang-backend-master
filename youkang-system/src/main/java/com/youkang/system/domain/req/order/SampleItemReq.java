package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 样品项DTO（用于订单中的样品列表）
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "样品项信息")
public class SampleItemReq {

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "历史订单号")
    private String orderHistory;

    @Schema(description = "样品编号")
    private String sampleId;

    @Schema(description = "样品类型")
    private String sampleType;

    @Schema(description = "样品位置")
    private String samplePosition;

    @Schema(description = "引物")
    private String primer;

    @Schema(description = "引物类型")
    private String primerType;

    @Schema(description = "引物位置")
    private String primerPosition;

    @Schema(description = "引物浓度")
    private String primerConcentration;

    @Schema(description = "序列")
    private String seq;

    @Schema(description = "测序项目")
    private String project;

    @Schema(description = "载体名称")
    private String carrierName;

    @Schema(description = "抗生素类型")
    private String antibioticType;

    @Schema(description = "质粒长度")
    private String plasmidLength;

    @Schema(description = "片段大小")
    private String fragmentSize;

    @Schema(description = "是否测通")
    private String testResult;

    @Schema(description = "原浓度")
    private String originConcentration;

    @Schema(description = "模板板号")
    private String templatePlateNo;

    @Schema(description = "模板孔号")
    private String templateHoleNo;

    @Schema(description = "完成情况")
    private String performance;

    @Schema(description = "返回状态")
    private Integer returnState;

    @Schema(description = "流程名称")
    private String flowName;

    @Schema(description = "板号")
    private String plateNo;

    @Schema(description = "孔号")
    private String holeNo;

    @Schema(description = "所属公司")
    private String belongCompany;

    @Schema(description = "生产公司")
    private String produceCompany;

    @Schema(description = "孔号数量")
    private Integer holeNumber;

    @Schema(description = "排版方式")
    private String layout;

    @Schema(description = "备注")
    private String remark;
}
