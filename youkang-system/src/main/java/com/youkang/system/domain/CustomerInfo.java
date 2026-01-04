package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.youkang.common.annotation.Excel;
import com.youkang.common.core.domain.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 客户信息对象 yk_customer_info
 *
 * @author youkang
 * @date 2025-11-20
 */
@Data
@TableName("yk_customer_info")
public class CustomerInfo {
    /** 客户ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    @TableField("customer_name")
    private String customerName;
        /** 课题组id */
    @Excel(name = "课题组id")
    @TableField("subject_group_id")
    private Integer subjectGroupId;

    /** 地区 */
    @Excel(name = "地区")
    @TableField("region")
    private String region;

    /** 地址 */
    @Excel(name = "地址")
    @TableField("address")
    private String address;

    /** 电话 */
    @Excel(name = "电话")
    @TableField("phone")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    @TableField("email")
    private String email;

    /** 微信ID */
    @Excel(name = "微信ID")
    @TableField("wechat_id")
    private String wechatId;

    /** 等级 */
    @Excel(name = "客户等级")
    @TableField("customer_level")
    private String customerLevel;

    /** 状态 */
    @Excel(name = "状态")
    @TableField("status")
    private String status;

    /** 销售员 */
    @Excel(name = "销售员")
    @TableField("sales_person")
    private String salesPerson;

    /** 客户单位 */
    @Excel(name = "客户单位")
    @TableField("customer_unit")
    private String customerUnit;

    /** 结算方式 */
    @Excel(name = "结算方式")
    @TableField("payment_method")
    private String paymentMethod;

    /** 发票种类 */
    @Excel(name = "发票种类")
    @TableField("invoice_type")
    private String invoiceType;

    /** 备注 */
    @Excel(name = "备注")
    @TableField("remarks")
    private String remarks;

    /** 所属公司 */
    @Excel(name = "所属公司")
    @TableField("company")
    private String company;

    /** 创建者 */
    @TableField("create_by")
    private String createBy;

    /** 创建时间 */
    @TableField("create_time")
    private LocalDateTime createTime;

    /** 更新者 */
    @TableField("update_by")
    private String updateBy;

    /** 更新时间 */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
