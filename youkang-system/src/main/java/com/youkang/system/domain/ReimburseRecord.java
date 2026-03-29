package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.youkang.common.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 返还记录对象 yk_reimburse_record
 *
 * @author youkang
 */
@Data
@TableName("yk_reimburse_record")
@Accessors(chain = true)
public class ReimburseRecord {

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    @TableField("customer_name")
    private String customerName;

    /** 订单号 */
    @Excel(name = "订单号")
    @TableField("order_id")
    private String orderId;

    /** 返还安排时间 */
    @Excel(name = "返还安排时间")
    @TableField("schedule_time")
    private LocalDateTime scheduleTime;

    /** 安排人 */
    @Excel(name = "安排人")
    @TableField("scheduler")
    private String scheduler;

    /** 返还类型 */
    @Excel(name = "返还类型")
    @TableField("reimburse_type")
    private String reimburseType;

    /** 返还数量 */
    @Excel(name = "返还数量")
    @TableField("reimburse_count")
    private Integer reimburseCount;

    /** 生产编号集（逗号分隔） */
    @Excel(name = "生产编号集")
    @TableField("produce_ids")
    private String produceIds;

    /** 状态：待返还/已返还 */
    @Excel(name = "状态")
    @TableField("status")
    private String status;

    /** 返还时间 */
    @Excel(name = "返还时间")
    @TableField("reimburse_time")
    private LocalDateTime reimburseTime;

    /** 返还人 */
    @Excel(name = "返还人")
    @TableField("reimburser")
    private String reimburser;

    /** 所属公司 */
    @Excel(name = "所属公司")
    @TableField("belong_company")
    private String belongCompany;

    /** 生产公司 */
    @Excel(name = "生产公司")
    @TableField("produce_company")
    private String produceCompany;

    /** 备注 */
    @Excel(name = "备注")
    @TableField("remark")
    private String remark;

    /** 创建人 */
    @TableField("create_user")
    private String createUser;

    /** 创建时间 */
    @TableField("create_time")
    private LocalDateTime createTime;

    /** 更新人 */
    @TableField("update_user")
    private String updateUser;

    /** 更新时间 */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
