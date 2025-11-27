package com.youkang.framework.web.service.selector.impl;

import com.youkang.common.core.domain.SelectorDTO;
import com.youkang.common.core.domain.SelectorQueryReq;
import com.youkang.common.enums.SelectorTypeEnum;
import com.youkang.common.utils.StringUtils;
import com.youkang.framework.web.service.selector.ICommonSelector;
import com.youkang.system.domain.SysPost;
import com.youkang.system.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位选择器实现类
 *
 * @author youkang
 */
@Service
public class PostSelectorImpl implements ICommonSelector {

    @Autowired
    private ISysPostService postService;

    @Override
    public List<SelectorDTO> getSelectorList(SelectorQueryReq queryReq) {
        SysPost post = new SysPost();

        // 支持模糊查询（岗位名称）
        if (StringUtils.isNotEmpty(queryReq.getKeyword())) {
            post.setPostName(queryReq.getKeyword());
        }

        List<SysPost> postList = postService.selectPostList(post);

        // 限制返回数量
        if (queryReq.getPageSize() != null && queryReq.getPageSize() > 0) {
            postList = postList.stream()
                    .limit(queryReq.getPageSize())
                    .collect(Collectors.toList());
        }

        return postList.stream()
                .map(p -> new SelectorDTO(
                        p.getPostName(),                    // label: 岗位名称
                        String.valueOf(p.getPostId()),      // value: 岗位ID
                        p                                   // extra: 完整岗位对象
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String getSelectorType() {
        return SelectorTypeEnum.POST.getCode();
    }
}
