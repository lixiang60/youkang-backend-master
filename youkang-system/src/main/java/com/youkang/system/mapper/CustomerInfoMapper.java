package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.system.domain.CustomerInfo;
import com.youkang.system.domain.resp.customer.CustomerSelectorResp;
import org.apache.ibatis.annotations.Param;

public interface CustomerInfoMapper extends BaseMapper<CustomerInfo> {

    Page<CustomerSelectorResp> customerSelector(Page<CustomerSelectorResp> page,@Param("queryString") String queryString);
}
