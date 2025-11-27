package com.youkang.framework.web.service.selector.impl;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.common.core.domain.entity.SysUser;
import com.youkang.common.enums.SelectorTypeEnum;
import com.youkang.common.utils.StringUtils;
import com.youkang.framework.web.service.selector.ICommonSelector;
import com.youkang.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户选择器实现类
 *
 * @author youkang
 */
@Service
public class UserSelectorImpl implements ICommonSelector {

    @Autowired
    private ISysUserService userService;

    @Override
    public List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq) {
        SysUser user = new SysUser();

        // 支持模糊查询（用户名或昵称）
        if (StringUtils.isNotEmpty(queryReq.getKeyword())) {
            user.setUserName(queryReq.getKeyword());
        }

        List<SysUser> userList = userService.selectUserList(user);

        // 限制返回数量
        if (queryReq.getPageSize() != null && queryReq.getPageSize() > 0) {
            userList = userList.stream()
                    .limit(queryReq.getPageSize())
                    .toList();
        }

        return userList.stream()
                .map(u -> new SelectorDTO(
                        u.getNickName() + "(" + u.getUserName() + ")",  // label: 昵称(用户名)
                        String.valueOf(u.getUserId()),                   // value: 用户ID
                        u                                                // extra: 完整用户对象
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getSelectorType() {
        return SelectorTypeEnum.USER.getCode();
    }
}
