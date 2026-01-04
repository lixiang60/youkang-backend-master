package com.youkang.common.enums;

import lombok.Getter;

/**
 * 样本类型枚举
 *
 * @author youkang
 */
@Getter
public enum SampleTypeEnum {

    BACTERIA("bacteria", "菌"),
    PLATE_BACTERIA("plateBacteria", "板菌"),
    P_UNFINISHED("pUnfinished", "P未"),
    PLASMID("plasmid", "质粒"),
    P_FINISHED("pFinished", "P已"),
    CLONE("clone", "克隆"),
    GEL_BLOCK("gelBlock", "胶块"),
    DIRECT_BACTERIA("directBacteria", "直提菌"),
    SEDIMENT_BACTERIA("sedimentBacteria", "沉菌"),
    TUBE_BACTERIA("tubeBacteria", "管菌"),
    WELL_96_PLATE_BACTERIA("well96PlateBacteria", "96孔板菌液"),
    STREAK_BACTERIA("streakBacteria", "划线菌"),
    LOW_COPY_BACTERIA("lowCopyBacteria", "低拷贝菌"),
    AGROBACTERIUM("agrobacterium", "农杆菌");

    private final String code;
    private final String desc;

    SampleTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(String code) {
        for (SampleTypeEnum sampleType : SampleTypeEnum.values()) {
            if (sampleType.getCode().equals(code)) {
                return sampleType.getDesc();
            }
        }
        return null;
    }

    public static String getCodeByDesc(String desc) {
        for (SampleTypeEnum sampleType : SampleTypeEnum.values()) {
            if (sampleType.getDesc().equals(desc)) {
                return sampleType.getCode();
            }
        }
        return null;
    }
}
