package com.youkang.common.enums;

import lombok.Getter;

/**
 * 发票种类枚举
 *
 * @author youkang
 */
@Getter
public enum InvoiceTypeEnum {

    VAT("vat", "增值票"),
    LOCAL_TAX("localTax", "地税"),
    NATIONAL_TAX("nationalTax", "国税票"),
    ORDINARY("ordinary", "普通发票");

    private final String code;
    private final String desc;

    InvoiceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (InvoiceTypeEnum invoiceType : InvoiceTypeEnum.values()) {
            if (invoiceType.getCode().equals(code)) {
                return invoiceType.getDesc();
            }
        }
        return null;
    }

    public static String getCodeByDesc(String desc) {
        for (InvoiceTypeEnum invoiceType : InvoiceTypeEnum.values()) {
            if (invoiceType.getDesc().equals(desc)) {
                return invoiceType.getCode();
            }
        }
        return null;
    }
}
