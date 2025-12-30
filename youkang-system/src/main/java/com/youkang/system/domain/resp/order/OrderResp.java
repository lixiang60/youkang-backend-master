package com.youkang.system.domain.resp.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.youkang.common.annotation.Excel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OrderResp {
    /** 订单id */
    @Excel(name = "订单id")
    @Schema(description = "订单id")
    private String orderId;

    /** 客户id */
    @Excel(name = "客户id")
    @Schema(description = "客户id")
    private Integer customerId;

    @Excel(name = "客户姓名")
    @Schema(description = "客户姓名")
    private String customerName;

    @Excel(name = "客户地址")
    @Schema(description = "客户地址")
    private String customerAddress;


    @Excel(name = "课题组id")
    @Schema(description = "课题组id")
    private Integer groupId;

    @Excel(name = "课题组")
    @Schema(description = "课题组")
    private String groupName;

    /** 测序代数（1代：1，4代：4） */
    @Excel(name = "测序代数", readConverterExp = "1=1代,4=4代")
    @Schema(description = "测序代数")
    private Integer generation;

    /** 订单类型 */
    @Excel(name = "订单类型")
    @Schema(description = "订单类型")
    private String orderType;

    /** 是否同步（0:同步康为，1:不同步） */
    @Excel(name = "是否同步", readConverterExp = "0=同步康为,1=不同步")
    @Schema(description = "是否同步" )
    private Integer isAsync;

    /** 所属公司 */
    @Excel(name = "所属公司")
    @Schema(description = "所属公司" )
    private String belongCompany;

    /** 生产公司 */
    @Excel(name = "生产公司")
    @Schema(description = "生产公司" )
    private String produceCompany;

    /** 关联基因号 */
    @Excel(name = "关联基因号")
    @Schema(description = "关联基因号" )
    private String genNo;

    /** 关联基因号 */
    @Excel(name = "备注")
    @Schema(description = "备注" )
    private String remark;

    /** 创建者 */
    @Excel(name = "创建者")
    @Schema(description = "创建者" )
    private String createBy;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间" )
    private Date createTime;
}
