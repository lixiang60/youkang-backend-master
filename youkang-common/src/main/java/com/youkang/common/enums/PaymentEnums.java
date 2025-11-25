package com.youkang.common.enums;

import lombok.Getter;

@Getter
public enum PaymentEnums {
    CASH(0,"现金结算"),
    CHECK(1,"支票结算"),
    EFT(2,"转账汇款"),
    MONTH_PAY(3,"月结"),
    ADVANCE(4,"预付款"),
    INTERNAL(5,"内部结算");

    private final Integer code;

    private final String desc;

    PaymentEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code) {
        for (PaymentEnums paymentEnums : PaymentEnums.values()) {
            if (paymentEnums.getCode().equals(code)) {
                return paymentEnums.getDesc();
            }
        }
        return null;
    }
    public static Integer getCodeByDesc(String desc) {
        for (PaymentEnums paymentEnums : PaymentEnums.values()) {
            if (paymentEnums.getDesc().equals(desc)) {
                return paymentEnums.getCode();
            }
        }
        return null;
    }

}
