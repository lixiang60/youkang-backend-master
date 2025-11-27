package com.youkang.common.core.domain;

import lombok.Data;

/**
 * 通用选择器查询请求
 *
 * @author youkang
 */
@Data
public class SelectorQueryReq {

    /**
     * 选择器类型
     */
    private String type;

    /**
     * 模糊查询关键字
     */
    private String keyword;

    /**
     * 额外参数（可选）
     * 例如：字典类型需要传入dictType
     */
    private String param;

    /**
     * 分页大小（默认20）
     */
    private Integer pageSize = 20;
}
