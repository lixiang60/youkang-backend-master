package com.youkang.system.domain.resp.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 样品流程流转日志响应
 *
 * @author youkang
 */
@Data
public class SampleFlowLogResp {

    /** 主键ID */
    private Long id;

    /** 生产编号 */
    private Long produceId;

    /** 操作类型 */
    private String operation;

    /** 流程名称 */
    private String flowName;

    /** 板号 */
    private String plateNo;

    /** 孔号 */
    private String holeNo;

    /** 模板板号 */
    private String templatePlateNo;

    /** 模板孔号 */
    private String templateHoleNo;

    /** 排版方式 */
    private String layout;

    /** 孔号数 */
    private Integer holeNumber;

    /** 原浓度 */
    private String originConcentration;

    /** 返回状态 */
    private String returnState;

    /** 备注 */
    private String remark;

    /** 操作人 */
    private String operator;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;

    /** 流转前流程名称 */
    private String beforeFlowName;
}
