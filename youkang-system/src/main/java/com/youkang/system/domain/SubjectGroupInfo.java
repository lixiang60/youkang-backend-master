package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.youkang.common.annotation.Excel;
import com.youkang.common.core.domain.BaseEntity;
import com.youkang.common.core.domain.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 课题组信息对象 yk_subject_group_info
 *
 * @author youkang
 * @date 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yk_subject_group_info")
public class SubjectGroupInfo extends PageReq {

    /** 课题组ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 名称 */
    @Excel(name = "课题组名称")
    @TableField("name")
    private String name;

    /** 地区 */
    @Excel(name = "地区")
    @TableField("region")
    private String region;

    /** 业务员 */
    @Excel(name = "业务员")
    @TableField("sales_person")
    private String salesPerson;

    /** 结算方式 */
    @Excel(name = "结算方式")
    @TableField("payment_method")
    private String paymentMethod;

    /** 发票抬头 */
    @Excel(name = "发票抬头")
    @TableField("invoice_title")
    private String invoiceTitle;

    /** 所属公司ID */
    @Excel(name = "所属公司ID")
    @TableField("company_id")
    private String companyId;

    /** 联系人 */
    @Excel(name = "联系人")
    @TableField("contact_person")
    private String contactPerson;

    /** 联系电话 */
    @Excel(name = "联系电话")
    @TableField("contact_phone")
    private String contactPhone;

    /** 联系地址 */
    @Excel(name = "联系地址")
    @TableField("contact_address")
    private String contactAddress;
}
