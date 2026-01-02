package com.youkang.common.enums;

import lombok.Getter;

@Getter
public enum PayMentMethodEnum {
    CASH("cash", "现金结算"),
    CHECK("check", "支票结算"),
    EFT("transfer", "转账汇款"),
    MONTH_PAY("monthly", "月结"),
    ADVANCE("prepaid", "预付款"),
    INTERNAL("internal", "内部结算");
    private final String code;
    private final String desc;
    PayMentMethodEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public static String getDescByCode(String code) {
        for (PayMentMethodEnum paymentEnums : PayMentMethodEnum.values()) {
            if (paymentEnums.getCode().equals(code)) {
                return paymentEnums.getDesc();
            }
        }
        return null;
    }
    public static String getCodeByDesc(String desc) {
        for (PayMentMethodEnum paymentEnums : PayMentMethodEnum.values()) {
            if (paymentEnums.getDesc().equals(desc)) {
                return paymentEnums.getCode();
            }
        }
        return null;
    }
}
