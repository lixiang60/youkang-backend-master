package com.youkang.framework.web.service.selector.impl;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.common.core.domain.entity.SysRole;
import com.youkang.common.enums.SelectorTypeEnum;
import com.youkang.common.utils.StringUtils;
import com.youkang.framework.web.service.selector.ICommonSelector;
import com.youkang.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色选择器实现类
 *
 * @author youkang
 */
@Service
public class RoleSelectorImpl implements ICommonSelector {

    @Autowired
    private ISysRoleService roleService;

    @Override
    public List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq) {
        SysRole role = new SysRole();

        // 支持模糊查询（角色名称）
        if (StringUtils.isNotEmpty(queryReq.getKeyword())) {
            role.setRoleName(queryReq.getKeyword());
        }

        List<SysRole> roleList = roleService.selectRoleList(role);

        // 限制返回数量
        if (queryReq.getPageSize() != null && queryReq.getPageSize() > 0) {
            roleList = roleList.stream()
                    .limit(queryReq.getPageSize())
                    .collect(Collectors.toList());
        }

        return roleList.stream()
                .map(r -> new SelectorDTO(
                        r.getRoleName(),                    // label: 角色名称
                        String.valueOf(r.getRoleId()),      // value: 角色ID
                        r                                   // extra: 完整角色对象
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getSelectorType() {
        return SelectorTypeEnum.ROLE.getCode();
    }
}
