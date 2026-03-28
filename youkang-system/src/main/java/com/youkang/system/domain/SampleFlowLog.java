package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.youkang.common.annotation.Excel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 样品流程流转日志对象 yk_sample_flow_log
 *
 * @author youkang
 */
@Data
@TableName("yk_sample_flow_log")
public class SampleFlowLog {

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 生产编号 */
    @Excel(name = "生产编号")
    @TableField("produce_id")
    private Long produceId;

    /** 操作类型 */
    @Excel(name = "操作类型")
    @TableField("operation")
    private String operation;

    /** 流程名称 */
    @Excel(name = "流程名称")
    @TableField("flow_name")
    private String flowName;

    /** 板号 */
    @Excel(name = "板号")
    @TableField("plate_no")
    private String plateNo;

    /** 孔号 */
    @Excel(name = "孔号")
    @TableField("hole_no")
    private String holeNo;

    /** 模板板号 */
    @Excel(name = "模板板号")
    @TableField("template_plate_no")
    private String templatePlateNo;

    /** 模板孔号 */
    @Excel(name = "模板孔号")
    @TableField("template_hole_no")
    private String templateHoleNo;

    /** 排版方式 */
    @Excel(name = "排版方式")
    @TableField("layout")
    private String layout;

    /** 孔号数 */
    @Excel(name = "孔号数")
    @TableField("hole_number")
    private Integer holeNumber;

    /** 原浓度 */
    @Excel(name = "原浓度")
    @TableField("origin_concentration")
    private String originConcentration;

    /** 返回状态 */
    @Excel(name = "返回状态")
    @TableField("return_state")
    private String returnState;

    /** 备注 */
    @Excel(name = "备注")
    @TableField("remark")
    private String remark;

    /** 操作人 */
    @Excel(name = "操作人")
    @TableField("operator")
    private String operator;

    /** 操作时间 */
    @Excel(name = "操作时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("operate_time")
    private LocalDateTime operateTime;

    /** 流转前流程名称 */
    @Excel(name = "流转前流程")
    @TableField("before_flow_name")
    private String beforeFlowName;
}
