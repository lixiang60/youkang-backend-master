package com.youkang.system.domain.resp.order;

import com.youkang.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 重抽样品响应
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "重抽样品响应")
public class ResampleResp {

    @Excel(name = "流水号")
    @Schema(description = "流水号/生产编号")
    private Long produceId;

    @Excel(name = "原孔号")
    @Schema(description = "原孔号")
    private String originHoleNo;

    @Excel(name = "客户姓名")
    @Schema(description = "客户姓名")
    private String customerName;

    @Excel(name = "返回状态")
    @Schema(description = "返回状态")
    private String returnState;

    @Excel(name = "样本类型")
    @Schema(description = "样本类型")
    private String sampleType;

    @Excel(name = "样品编号")
    @Schema(description = "样品编号")
    private String sampleId;

    @Excel(name = "载体名称")
    @Schema(description = "载体名称")
    private String carrierName;

    @Excel(name = "抗生素类型")
    @Schema(description = "抗生素类型")
    private String antibioticType;

    @Excel(name = "样品位置")
    @Schema(description = "样品位置")
    private String samplePosition;

    @Excel(name = "样品备注")
    @Schema(description = "样品备注")
    private String remark;
}
