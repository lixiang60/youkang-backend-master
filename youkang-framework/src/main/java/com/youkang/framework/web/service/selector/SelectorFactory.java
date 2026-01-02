package com.youkang.framework.web.service.selector;

import com.youkang.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 选择器工厂类
 * 根据类型获取对应的选择器实现
 *
 * @author youkang
 */
@Component
public class SelectorFactory {

    @Autowired
    private List<ICommonSelector> selectorList;

    /**
     * 选择器映射表
     * key: 选择器类型
     * value: 选择器实现
     */
    private final Map<String, ICommonSelector> selectorMap = new ConcurrentHashMap<>();

     /**
     * 初始化选择器映射表
     */
    @PostConstruct
    public void init() {
        if (selectorList != null) {
            for (ICommonSelector selector : selectorList) {
                selectorMap.put(selector.getSelectorType(), selector);
            }
        }
    }

    /**
     * 根据类型获取选择器
     *
     * @param type 选择器类型
     * @return 选择器实现
     */
    public ICommonSelector getSelector(String type) {
        ICommonSelector selector = selectorMap.get(type);
        if (selector == null) {
            throw new ServiceException("不支持的选择器类型: " + type);
        }
        return selector;
    }

    /**
     * 获取所有支持的选择器类型
     *
     * @return 选择器类型列表
     */
    public List<String> getSupportedTypes() {
            return selectorMap.keySet().stream().sorted().toList();
    }
}
