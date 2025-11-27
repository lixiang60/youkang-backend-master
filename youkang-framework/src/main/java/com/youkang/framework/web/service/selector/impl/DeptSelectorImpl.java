package com.youkang.framework.web.service.selector.impl;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.common.core.domain.entity.SysDept;
import com.youkang.common.enums.SelectorTypeEnum;
import com.youkang.common.utils.StringUtils;
import com.youkang.framework.web.service.selector.ICommonSelector;
import com.youkang.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门选择器实现类
 *
 * @author youkang
 */
@Service
public class DeptSelectorImpl implements ICommonSelector {

    @Autowired
    private ISysDeptService deptService;

    @Override
    public List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq) {
        SysDept dept = new SysDept();

        // 支持模糊查询（部门名称）
        if (StringUtils.isNotEmpty(queryReq.getKeyword())) {
            dept.setDeptName(queryReq.getKeyword());
        }

        List<SysDept> deptList = deptService.selectDeptList(dept);

        // 限制返回数量
        if (queryReq.getPageSize() != null && queryReq.getPageSize() > 0) {
            deptList = deptList.stream()
                    .limit(queryReq.getPageSize())
                    .toList();
        }

        return deptList.stream()
                .map(d -> new SelectorDTO(
                        d.getDeptName(),                    // label: 部门名称
                        String.valueOf(d.getDeptId()),      // value: 部门ID
                        d                                   // extra: 完整部门对象
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getSelectorType() {
        return SelectorTypeEnum.DEPT.getCode();
    }
}
