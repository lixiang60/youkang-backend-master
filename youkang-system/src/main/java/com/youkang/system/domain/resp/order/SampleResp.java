package com.youkang.system.domain.resp.order;

import com.youkang.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 样品信息响应
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "样品信息响应")
public class SampleResp {

    @Excel(name = "订单号")
    @Schema(description = "订单号")
    private String orderId;

    @Excel(name = "历史订单号")
    @Schema(description = "历史订单号")
    private String orderHistory;

    @Excel(name = "样品编号")
    @Schema(description = "样品编号")
    private String sampleId;

    @Excel(name = "样品类型")
    @Schema(description = "样品类型")
    private String sampleType;

    @Excel(name = "样品位置")
    @Schema(description = "样品位置")
    private String samplePosition;

    @Excel(name = "引物")
    @Schema(description = "引物")
    private String primer;

    @Excel(name = "引物类型")
    @Schema(description = "引物类型")
    private String primerType;

    @Excel(name = "引物位置")
    @Schema(description = "引物位置")
    private String primerPosition;

    @Excel(name = "引物浓度")
    @Schema(description = "引物浓度")
    private String primerConcentration;

    @Excel(name = "序列")
    @Schema(description = "序列")
    private String seq;

    @Excel(name = "测序项目")
    @Schema(description = "测序项目")
    private String project;

    @Excel(name = "载体名称")
    @Schema(description = "载体名称")
    private String carrierName;

    @Excel(name = "抗生素类型")
    @Schema(description = "抗生素类型")
    private String antibioticType;

    @Excel(name = "质粒长度")
    @Schema(description = "质粒长度")
    private String plasmidLength;

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

    @Excel(name = "板号")
    @Schema(description = "板号")
    private String plateNo;

    @Excel(name = "孔号")
    @Schema(description = "孔号")
    private String holeNo;

    @Excel(name = "所属公司")
    @Schema(description = "所属公司")
    private String belongCompany;

    @Excel(name = "生产公司")
    @Schema(description = "生产公司")
    private String produceCompany;

    @Excel(name = "孔号数量")
    @Schema(description = "孔号数量")
    private Integer holeNumber;

    @Excel(name = "排版方式")
    @Schema(description = "排版方式")
    private String layout;

    @Excel(name = "创建人")
    @Schema(description = "创建人")
    private String createUser;

    @Excel(name = "创建时间")
    @Schema(description = "创建时间")
    private String createTime;

    @Excel(name = "更新人")
    @Schema(description = "更新人")
    private String updateUser;

    @Excel(name = "更新时间")
    @Schema(description = "更新时间")
    private String updateTime;

    @Excel(name = "备注")
    @Schema(description = "备注")
    private String remark;
}
