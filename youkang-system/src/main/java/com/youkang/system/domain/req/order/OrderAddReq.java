package com.youkang.system.domain.req.order;

import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.resp.customer.CustomerSelectorResp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Schema(description = "订单添加请求-默认进行模板排版")
public class OrderAddReq {
    @Schema(description = "客户信息")
    private CustomerSelectorResp customerInfo;

    @Schema(description = "订单id")
    private String orderId;

    @Schema(description = "是否发送邮件")
    private Integer isEmail;

    @Schema(description = "所属公司")
    private String belongCompany;

    @Schema(description = "生产公司")
    private String produceCompany;

    @Schema(description = "订单模板类型,1:excel文件;2:集合")
    private Integer templateType;

    @Schema(description = "样品列表")
    private List<SampleInfo> sampleInfoList;

    @Schema(description = "关联基因号")
    private String genNo;

    @Schema(description = "备注")
    private String remark;
}
