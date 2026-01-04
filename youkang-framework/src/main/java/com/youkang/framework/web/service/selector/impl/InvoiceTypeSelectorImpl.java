package com.youkang.framework.web.service.selector.impl;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.common.enums.InvoiceTypeEnum;
import com.youkang.common.enums.SelectorTypeEnum;
import com.youkang.framework.web.service.selector.ICommonSelector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 发票种类选择器
 * 提供固定的发票种类下拉选项
 *
 * @author youkang
 */
@Service
public class InvoiceTypeSelectorImpl implements ICommonSelector {

    @Override
    public List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq) {
        List<SelectorDTO> selectorDTOS = new ArrayList<>();
        for (InvoiceTypeEnum invoiceType : InvoiceTypeEnum.values()) {
            SelectorDTO selectorDTO = new SelectorDTO(invoiceType.getDesc(), invoiceType.getCode(), null);
            selectorDTOS.add(selectorDTO);
        }
        return selectorDTOS;
    }

    @Override
    public String getSelectorType() {
        return SelectorTypeEnum.INVOICE_TYPE.getCode();
    }
}
