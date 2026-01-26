package com.youkang.system.domain.resp.order;

import com.youkang.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SampleTemplateResp {
    @Excel(name = "订单号")
    @Schema(description = "订单号")
    private String orderId;

    @Excel(name = "个数")
    @Schema(description = "个数")
    private String templateNumber;

    @Excel(name = "客户姓名")
    @Schema(description = "客户姓名")
    private String customerName;

    @Excel(name = "客户地址")
    @Schema(description = "客户地址")
    private String customerAddress;

    @Excel(name = "样品编号")
    @Schema(description = "样品编号")
    private String sampleId;

    @Excel(name = "样品类型")
    @Schema(description = "样品类型")
    private String sampleType;

    @Excel(name = "引物")
    @Schema(description = "引物")
    private String primer;

    @Excel(name = "引物浓度")
    @Schema(description = "引物浓度")
    private String primerConcentration;

    @Excel(name = "载体名称")
    @Schema(description = "载体名称")
    private String carrierName;

    @Excel(name = "抗生素类型")
    @Schema(description = "抗生素类型")
    private String antibioticType;

    @Excel(name = "片段大小")
    @Schema(description = "片段大小")
    private String fragmentSize;

    @Excel(name = "是否测通")
    @Schema(description = "是否测通")
    private String testResult;

    @Excel(name = "原浓度")
    @Schema(description = "原浓度")
    private String originConcentration;

    @Excel(name = "模板板号")
    @Schema(description = "模板板号")
    private String templatePlateNo;

    @Excel(name = "模板孔号")
    @Schema(description = "模板孔号")
    private String templateHoleNo;

    @Excel(name = "完成情况")
    @Schema(description = "完成情况")
    private String performance;

    @Excel(name = "返回状态")
    @Schema(description = "返回状态")
    private Integer returnState;

    @Excel(name = "流程名称")
    @Schema(description = "流程名称")
    private String flowName;

    @Excel(name = "创建人")
    @Schema(description = "创建人")
    private String createUser;

    @Excel(name = "备注")
    @Schema(description = "备注")
    private String remark;
}
