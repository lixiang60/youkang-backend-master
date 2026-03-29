package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 报告状态修改请求
 *
 * @author youkang
 */
@Data
@Schema(description = "报告状态修改请求")
public class ReportStatusUpdateReq {

    @NotEmpty(message = "生产编号列表不能为空")
    @Schema(description = "生产编号列表")
    private List<Long> produceIdList;

    @Schema(description = "原浓度")
    private String originConcentration;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "报告异常原因")
    private String reportErrorReason;

    @NotEmpty(message = "报告状态不能为空")
    @Schema(description = "报告状态：报告成功/报告取消/报告重做/报告重抽")
    private String reportStatus;
}
