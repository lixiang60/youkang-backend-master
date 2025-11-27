package com.youkang.common.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用选择器DTO
 * 用于前端下拉框组件
 *
 * @author youkang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectorDTO {

    /**
     * 显示标签
     */
    private String label;

    /**
     * 实际值
     */
    private String value;

    /**
     * 扩展信息（可选）
     */
    private Object extra;
}
