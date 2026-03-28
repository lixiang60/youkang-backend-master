package com.youkang.system.domain.enums;

import lombok.Getter;

/**
 * 样品流程操作类型枚举
 *
 * @author youkang
 */
@Getter
public enum SampleFlowOperation {

    /** 添加模板板号 */
    ADD_TEMPLATE_PLATE_NO("添加模板板号"),

    /** 添加模板孔号 */
    ADD_TEMPLATE_HOLE_NO("添加模板孔号"),

    /** 流程流转 */
    FLOW("流程流转"),

    /** 设置状态 */
    SET_STATUS("设置状态"),

    /** 设置原浓度 */
    SET_ORIGIN_CONCENTRATION("设置原浓度"),

    /** 添加反应板号 */
    ADD_REACTION_PLATE_NO("添加反应板号"),

    /** 添加反应孔号 */
    ADD_REACTION_HOLE_NO("添加反应孔号"),

    /** 退回 */
    SEND_BACK("退回"),

    /** 忽略 */
    IGNORE("忽略"),

    /** 反应停止 */
    REACTION_STOP("反应停止"),

    /** 样品不足 */
    SAMPLE_INSUFFICIENT("样品不足"),

    REACTION_PRE("反应预做"),

    REACTION_PRE_SEND_BACK("反应预做退回"),


    ;

    private final String description;

    SampleFlowOperation(String description) {
        this.description = description;
    }

}
