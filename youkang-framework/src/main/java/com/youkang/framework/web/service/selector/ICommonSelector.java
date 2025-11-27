package com.youkang.framework.web.service.selector;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;

import java.util.List;

/**
 * 通用选择器接口
 * 所有选择器实现类都需要实现此接口
 *
 * @author youkang
 */
public interface ICommonSelector {

    /**
     * 获取选择器数据列表
     *
     * @param queryReq 查询请求
     * @return 选择器数据列表
     */
    List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq);

    /**
     * 获取选择器类型
     *
     * @return 选择器类型代码
     */
    String getSelectorType();
}
