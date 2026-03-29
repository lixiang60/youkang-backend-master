package com.youkang.system.domain.req.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 返还记录新增请求
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "返还记录新增请求")
public class ReimburseRecordAddReq {

    @Schema(description = "客户姓名")
    private String customerName;

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "返还安排时间")
    private LocalDateTime scheduleTime;

    @Schema(description = "安排人")
    private String scheduler;

    @Schema(description = "返还类型")
    private String reimburseType;

    @NotEmpty(message = "生产编号列表不能为空")
    @Schema(description = "生产编号列表")
    private List<Long> produceIdList;

    @Schema(description = "备注")
    private String remark;
}
