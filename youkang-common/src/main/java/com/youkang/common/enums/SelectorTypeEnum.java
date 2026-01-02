package com.youkang.common.enums;

import lombok.Getter;

/**
 * 选择器类型枚举
 *
 * @author youkang
 */
@Getter
public enum SelectorTypeEnum {

    USER("user", "用户选择器"),
    DEPT("dept", "部门选择器"),
    ROLE("role", "角色选择器"),
    POST("post", "岗位选择器"),
    DICT("dict", "字典选择器"),
    PAYMENT("paymentMethod", "结算方式选择器"),


    ;


    private final String code;
    private final String desc;

    SelectorTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code获取枚举
     */
    public static SelectorTypeEnum getByCode(String code) {
        for (SelectorTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
