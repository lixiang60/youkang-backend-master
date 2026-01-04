package com.youkang.system.domain.req.order;

import com.youkang.common.core.domain.PageReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 样品查询请求
 *
 * @author youkang
 */
@Getter
@Setter
@Schema(description = "样品查询请求")
public class SampleQueryReq extends PageReq {

    @Schema(description = "订单号")
    private String orderId;

    @Schema(description = "历史订单号")
    private String orderHistory;

    @Schema(description = "样品编号")
    private String sampleId;

    @Schema(description = "样品类型")
    private String sampleType;

    @Schema(description = "引物")
    private String primer;

    @Schema(description = "引物类型")
    private String primerType;

    @Schema(description = "测序项目")
    private String project;

    @Schema(description = "载体名称")
    private String carrierName;

    @Schema(description = "抗生素类型")
    private String antibioticType;

    @Schema(description = "是否测通")
    private String testResult;

    @Schema(description = "完成情况")
    private String performance;

    @Schema(description = "返回状态")
    private Integer returnState;

    @Schema(description = "流程名称")
    private String flowName;

    @Schema(description = "板号")
    private String plateNo;

    @Schema(description = "所属公司")
    private String belongCompany;

    @Schema(description = "生产公司")
    private String produceCompany;

    @Schema(description = "创建人")
    private String createUser;
}
