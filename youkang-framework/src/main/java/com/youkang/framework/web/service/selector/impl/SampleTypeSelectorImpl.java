package com.youkang.framework.web.service.selector.impl;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.common.enums.SampleTypeEnum;
import com.youkang.common.enums.SelectorTypeEnum;
import com.youkang.framework.web.service.selector.ICommonSelector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 样本类型选择器
 * 提供固定的样本类型下拉选项
 *
 * @author youkang
 */
@Service
public class SampleTypeSelectorImpl implements ICommonSelector {

    @Override
    public List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq) {
        List<SelectorDTO> selectorDTOS = new ArrayList<>();
        for (SampleTypeEnum sampleType : SampleTypeEnum.values()) {
            SelectorDTO selectorDTO = new SelectorDTO(sampleType.getDesc(), sampleType.getCode(), null);
            selectorDTOS.add(selectorDTO);
        }
        return selectorDTOS;
    }

    @Override
    public String getSelectorType() {
        return SelectorTypeEnum.SAMPLE_TYPE.getCode();
    }
}
