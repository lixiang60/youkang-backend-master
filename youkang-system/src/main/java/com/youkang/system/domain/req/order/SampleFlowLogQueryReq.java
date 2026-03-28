package com.youkang.system.domain.req.order;

import lombok.Data;

/**
 * 样品流程流转日志查询请求
 *
 * @author youkang
 */
@Data
public class SampleFlowLogQueryReq {

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页数量 */
    private Integer pageSize = 10;

    /** 生产编号 */
    private Long produceId;

    /** 操作类型 */
    private String operation;

    /** 流程名称 */
    private String flowName;

    /** 板号 */
    private String plateNo;

    /** 模板板号 */
    private String templatePlateNo;

    /** 操作人 */
    private String operator;

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;
}
