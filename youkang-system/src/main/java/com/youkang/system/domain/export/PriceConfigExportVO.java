package com.youkang.system.domain.export;

import com.youkang.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 价格配置导出对象
 *
 * @author youkang
 */
@Data
public class PriceConfigExportVO {

    @Excel(name = "类别")
    private String category;

    @Excel(name = "收费名称")
    private String chargeName;

    @Excel(name = "样品类型")
    private String sampleType;

    @Excel(name = "测序项目")
    private String project;

    @Excel(name = "质粒长度下限")
    private Integer plasmidLengthMin;

    @Excel(name = "质粒长度上限")
    private Integer plasmidLengthMax;

    @Excel(name = "片段大小下限")
    private Integer fragmentSizeMin;

    @Excel(name = "片段大小上限")
    private Integer fragmentSizeMax;

    @Excel(name = "单价")
    private BigDecimal unitPrice;

    @Excel(name = "计算方式")
    private String calcMethod;

    @Excel(name = "状态", readConverterExp = "1=启用,0=禁用")
    private Integer status;

    @Excel(name = "是否自定义", readConverterExp = "1=是,0=否")
    private Integer isCustom;

    @Excel(name = "备注")
    private String remark;
}
