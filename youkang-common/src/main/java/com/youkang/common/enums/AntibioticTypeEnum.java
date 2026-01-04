package com.youkang.common.enums;

import lombok.Getter;

/**
 * 抗生素类型枚举
 *
 * @author youkang
 */
@Getter
public enum AntibioticTypeEnum {

    AMP_PLUS("ampPlus", "AMP+"),
    KANA("kana", "kana"),
    TETRACYCLINE("tetracycline", "四环素"),
    CHLORAMPHENICOL("chloramphenicol", "氯霉素");

    private final String code;
    private final String desc;

    AntibioticTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (AntibioticTypeEnum antibioticType : AntibioticTypeEnum.values()) {
            if (antibioticType.getCode().equals(code)) {
                return antibioticType.getDesc();
            }
        }
        return null;
    }

    public static String getCodeByDesc(String desc) {
        for (AntibioticTypeEnum antibioticType : AntibioticTypeEnum.values()) {
            if (antibioticType.getDesc().equals(desc)) {
                return antibioticType.getCode();
            }
        }
        return null;
    }
}
