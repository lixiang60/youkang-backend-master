package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.youkang.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 价格配置对象 yk_price_config
 *
 * @author youkang
 */
@Data
@TableName("yk_price_config")
public class PriceConfig {

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 课题组ID（NULL = 基础模板） */
    @Excel(name = "课题组ID")
    @TableField("group_id")
    private Integer groupId;

    /** 类别 */
    @Excel(name = "类别")
    @TableField("category")
    private String category;

    /** 收费名称 */
    @Excel(name = "收费名称")
    @TableField("charge_name")
    private String chargeName;

    /** 样品类型 */
    @Excel(name = "样品类型")
    @TableField("sample_type")
    private String sampleType;

    /** 测序项目 */
    @Excel(name = "测序项目")
    @TableField("project")
    private String project;

    /** 质粒长度下限 */
    @Excel(name = "质粒长度下限")
    @TableField("plasmid_length_min")
    private Integer plasmidLengthMin;

    /** 质粒长度上限（NULL = 无上限） */
    @Excel(name = "质粒长度上限")
    @TableField("plasmid_length_max")
    private Integer plasmidLengthMax;

    /** 片段大小下限 */
    @Excel(name = "片段大小下限")
    @TableField("fragment_size_min")
    private Integer fragmentSizeMin;

    /** 片段大小上限（NULL = 无上限） */
    @Excel(name = "片段大小上限")
    @TableField("fragment_size_max")
    private Integer fragmentSizeMax;

    /** 单价 */
    @Excel(name = "单价")
    @TableField("unit_price")
    private BigDecimal unitPrice;

    /** 计算方式 */
    @Excel(name = "计算方式")
    @TableField("calc_method")
    private String calcMethod;

    /** 状态（1启用 0禁用） */
    @Excel(name = "状态")
    @TableField("status")
    private Integer status;

    /** 创建人 */
    @TableField("create_by")
    private String createBy;

    /** 创建时间 */
    @TableField("create_time")
    private LocalDateTime createTime;

    /** 更新人 */
    @TableField("update_by")
    private String updateBy;

    /** 更新时间 */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /** 备注 */
    @Excel(name = "备注")
    @TableField("remark")
    private String remark;
}
