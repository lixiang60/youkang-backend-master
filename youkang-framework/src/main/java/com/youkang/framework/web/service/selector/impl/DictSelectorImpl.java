package com.youkang.framework.web.service.selector.impl;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.common.core.domain.entity.SysDictData;
import com.youkang.common.enums.SelectorTypeEnum;
import com.youkang.common.utils.StringUtils;
import com.youkang.framework.web.service.selector.ICommonSelector;
import com.youkang.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典选择器实现类
 * 需要通过param参数传入dictType字典类型
 *
 * @author youkang
 */
@Service
public class DictSelectorImpl implements ICommonSelector {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Override
    public List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq) {
        // 字典类型必须通过param参数传入
        if (StringUtils.isEmpty(queryReq.getParam())) {
            return new ArrayList<>();
        }

        String dictType = queryReq.getParam();
        List<SysDictData> dictDataList = dictTypeService.selectDictDataByType(dictType);

        if (dictDataList == null) {
            return new ArrayList<>();
        }

        // 支持模糊查询（字典标签）
        if (StringUtils.isNotEmpty(queryReq.getKeyword())) {
            String keyword = queryReq.getKeyword();
            dictDataList = dictDataList.stream()
                    .filter(d -> d.getDictLabel().contains(keyword) || d.getDictValue().contains(keyword))
                    .collect(Collectors.toList());
        }

        // 限制返回数量
        if (queryReq.getPageSize() != null && queryReq.getPageSize() > 0) {
            dictDataList = dictDataList.stream()
                    .limit(queryReq.getPageSize())
                    .collect(Collectors.toList());
        }

        return dictDataList.stream()
                .map(d -> new SelectorDTO(
                        d.getDictLabel(),                   // label: 字典标签
                        d.getDictValue(),                   // value: 字典值
                        d                                   // extra: 完整字典对象
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getSelectorType() {
        return SelectorTypeEnum.DICT.getCode();
    }
}
