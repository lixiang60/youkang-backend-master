package com.youkang.framework.web.service.selector.impl;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.common.enums.AntibioticTypeEnum;
import com.youkang.common.enums.SelectorTypeEnum;
import com.youkang.framework.web.service.selector.ICommonSelector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 抗生素类型选择器
 * 提供固定的抗生素类型下拉选项
 *
 * @author youkang
 */
@Service
public class AntibioticTypeSelectorImpl implements ICommonSelector {

    @Override
    public List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq) {
        List<SelectorDTO> selectorDTOS = new ArrayList<>();
        for (AntibioticTypeEnum antibioticType : AntibioticTypeEnum.values()) {
            SelectorDTO selectorDTO = new SelectorDTO(antibioticType.getDesc(), antibioticType.getCode(), null);
            selectorDTOS.add(selectorDTO);
        }
        return selectorDTOS;
    }

    @Override
    public String getSelectorType() {
        return SelectorTypeEnum.ANTIBIOTIC_TYPE.getCode();
    }
}
