package com.youkang.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.youkang.common.annotation.Excel;
import com.youkang.common.core.domain.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 样品信息对象 yk_sample_info
 *
 * @author youkang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yk_sample_info")
public class SampleInfo extends PageReq {

    /** 订单号 */
    @Excel(name = "订单号")
    @TableField("order_id")
    private String orderId;

    /** 历史订单号 */
    @Excel(name = "历史订单号")
    @TableField("order_history")
    private String orderHistory;

    /** 样品编号 */
    @Excel(name = "样品编号")
    @TableId(value = "sample_id", type = IdType.INPUT)
    private String sampleId;

    /** 样品类型 */
    @Excel(name = "样品类型")
    @TableField("sample_type")
    private String sampleType;

    /** 样品位置 */
    @Excel(name = "样品位置")
    @TableField("sample_position")
    private String samplePosition;

    /** 引物 */
    @Excel(name = "引物")
    @TableField("primer")
    private String primer;

    /** 引物类型 */
    @Excel(name = "引物类型")
    @TableField("primer_type")
    private String primerType;

    /** 引物位置 */
    @Excel(name = "引物位置")
    @TableField("primer_position")
    private String primerPosition;

    /** 引物浓度 */
    @Excel(name = "引物浓度")
    @TableField("primer_concentration")
    private String primerConcentration;

    /** 序列 */
    @Excel(name = "序列")
    @TableField("seq")
    private String seq;

    /** 测序项目 */
    @Excel(name = "测序项目")
    @TableField("project")
    private String project;

    /** 载体名称 */
    @Excel(name = "载体名称")
    @TableField("carrier_name")
    private String carrierName;

    /** 抗生素类型 */
    @Excel(name = "抗生素类型")
    @TableField("antibiotic_type")
    private String antibioticType;

    /** 质粒长度 */
    @Excel(name = "质粒长度")
    @TableField("plasmid_length")
    private String plasmidLength;

    /** 片段大小 */
    @Excel(name = "片段大小")
    @TableField("fragment_size")
    private String fragmentSize;

    /** 是否测通 */
    @Excel(name = "是否测通")
    @TableField("test_result")
    private String testResult;

    /** 原浓度 */
    @Excel(name = "原浓度")
    @TableField("origin_concentration")
    private String originConcentration;

    /** 模板板号 */
    @Excel(name = "模板板号")
    @TableField("template_plate_no")
    private String templatePlateNo;

    /** 模板孔号 */
    @Excel(name = "模板孔号")
    @TableField("template_hole_no")
    private String templateHoleNo;

    /** 完成情况 */
    @Excel(name = "完成情况")
    @TableField("performance")
    private String performance;

    /** 返回状态 */
    @Excel(name = "返回状态")
    @TableField("return_state")
    private Integer returnState;

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

    /** 所属公司 */
    @Excel(name = "所属公司")
    @TableField("belong_company")
    private String belongCompany;

    /** 生产公司 */
    @Excel(name = "生产公司")
    @TableField("produce_company")
    private String produceCompany;

    /** 孔号数量 */
    @Excel(name = "孔号数量")
    @TableField("hole_number")
    private Integer holeNumber;

    /** 排版方式 */
    @Excel(name = "排版方式")
    @TableField("layout")
    private String layout;

    /** 创建人 */
    @Excel(name = "创建人")
    @TableField("create_user")
    private String createUser;

    /** 创建时间 */
    @Excel(name = "创建时间")
    @TableField("create_time")
    private String createTime;

    /** 备注 */
    @Excel(name = "备注")
    @TableField("remark")
    private String remark;
}
