package com.youkang.system.domain.resp.order;

import com.youkang.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 返还记录响应
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "返还记录响应")
public class ReimburseRecordResp {

    @Excel(name = "ID")
    @Schema(description = "记录ID")
    private Long id;

    @Excel(name = "客户姓名")
    @Schema(description = "客户姓名")
    private String customerName;

    @Excel(name = "订单号")
    @Schema(description = "订单号")
    private String orderId;

    @Excel(name = "返还安排时间")
    @Schema(description = "返还安排时间")
    private LocalDateTime scheduleTime;

    @Excel(name = "安排人")
    @Schema(description = "安排人")
    private String scheduler;

    @Excel(name = "返还类型")
    @Schema(description = "返还类型")
    private String reimburseType;

    @Excel(name = "返还数量")
    @Schema(description = "返还数量")
    private Integer reimburseCount;

    @Excel(name = "生产编号集")
    @Schema(description = "生产编号集（逗号分隔）")
    private String produceIds;

    @Excel(name = "状态")
    @Schema(description = "状态：待返还/已返还")
    private String status;

    @Excel(name = "返还时间")
    @Schema(description = "返还时间")
    private LocalDateTime reimburseTime;

    @Excel(name = "返还人")
    @Schema(description = "返还人")
    private String reimburser;

    @Excel(name = "所属公司")
    @Schema(description = "所属公司")
    private String belongCompany;

    @Excel(name = "生产公司")
    @Schema(description = "生产公司")
    private String produceCompany;

    @Excel(name = "备注")
    @Schema(description = "备注")
    private String remark;

    @Excel(name = "创建人")
    @Schema(description = "创建人")
    private String createUser;

    @Excel(name = "创建时间")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
