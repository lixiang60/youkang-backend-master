package com.youkang.framework.web.service.selector.impl;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.common.enums.PayMentMethodEnum;
import com.youkang.common.enums.SelectorTypeEnum;
import com.youkang.framework.web.service.selector.ICommonSelector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 结算方式选择器
 * 提供固定的结算方式下拉选项
 *
 * @author youkang
 */
@Service
public class PaymentMethodSelectorImpl implements ICommonSelector {

    @Override
    public List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq) {
        List<SelectorDTO> selectorDTOS = new ArrayList<>();
        for (PayMentMethodEnum paymentEnums : PayMentMethodEnum.values()){
            SelectorDTO selectorDTO = new SelectorDTO(paymentEnums.getDesc(), paymentEnums.getCode(), null);
            selectorDTOS.add(selectorDTO);
        }
        return selectorDTOS;
    }

    @Override
    public String getSelectorType() {
        return SelectorTypeEnum.PAYMENT.getCode();
    }
}
