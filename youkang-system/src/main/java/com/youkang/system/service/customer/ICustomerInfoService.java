package com.youkang.system.service.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.CustomerInfo;
import com.youkang.system.domain.req.customer.CustomerQueryReq;
import com.youkang.system.domain.resp.customer.CustomerResp;
import com.youkang.system.domain.resp.customer.CustomerSelectorResp;

/**
 * 客户信息Service接口
 *
 * @author youkang
 * @date 2025-11-20
 */
public interface ICustomerInfoService extends IService<CustomerInfo> {

    /**
     * 分页查询客户信息列表
     *
     * @param queryReq 查询条件
     * @return 分页结果
     */
    IPage<CustomerResp> queryPage(CustomerQueryReq queryReq);

    /**
     * 获取客户详情
     *
     * @param id 客户ID
     * @return 客户详情
     */
    CustomerResp getDetail(Integer id);

    /**
     * 客户选择器
     *
     * @param queryString 查询字符串
     * @return 选择器数据
     */
    Page<CustomerSelectorResp> customerSelector(String queryString);
}
