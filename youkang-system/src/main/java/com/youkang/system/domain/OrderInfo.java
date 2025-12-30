package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.youkang.common.annotation.Excel;
import com.youkang.common.core.domain.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 订单信息对象 yk_order_info
 *
 * @author youkang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yk_order_info")
public class OrderInfo extends PageReq {

    /** 订单id */
    @Excel(name = "订单id")
    @TableId(value = "order_id", type = IdType.INPUT)
    private String orderId;

    /** 客户id */
    @Excel(name = "客户id")
    @TableField("customer_id")
    private Integer customerId;

    @Excel(name = "课题组id")
    @TableField("group_id")
    private Integer groupId;

    /** 订单类型 */
    @Excel(name = "订单类型")
    @TableField("order_type")
    private String orderType;

    /** 是否同步（0:同步康为，1:不同步） */
    @Excel(name = "是否同步", readConverterExp = "0=同步康为,1=不同步")
    @TableField("is_async")
    private Integer isAsync;

    /** 测序代数（1代：1，4代：4） */
    @Excel(name = "测序代数", readConverterExp = "1=1代,4=4代")
    @TableField("generation")
    private Integer generation;

    /** 所属公司 */
    @Excel(name = "所属公司")
    @TableField("belong_company")
    private String belongCompany;

    /** 生产公司 */
    @Excel(name = "生产公司")
    @TableField("produce_company")
    private String produceCompany;

    /** 关联基因号 */
    @Excel(name = "关联基因号")
    @TableField("gen_no")
    private String genNo;

    /** 备注 */
    @Excel(name = "备注")
    @TableField("remark")
    private String remark;

    /** 创建者 */
    @Excel(name = "创建者")
    @TableField("create_by")
    private String createBy;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private LocalDateTime createTime;
}
