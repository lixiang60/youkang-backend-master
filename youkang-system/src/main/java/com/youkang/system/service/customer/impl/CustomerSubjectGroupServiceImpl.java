package com.youkang.system.service.customer.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.system.domain.req.customer.CustomerSubjectGroupReq;
import com.youkang.system.domain.resp.customer.CustomerSubjectGroupResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.youkang.system.mapper.CustomerSubjectGroupMapper;
import com.youkang.system.domain.CustomerSubjectGroup;
import com.youkang.system.service.customer.ICustomerSubjectGroupService;

/**
 * 客户科目组关系Service业务层处理
 * <p>
 * ServiceImpl 已经实现了 IService 中的所有方法，包括：
 * - 单条/批量 新增、删除、修改、查询
 * - 分页查询
 * - 条件查询
 * 等常用方法，可直接使用
 *
 * @author youkang
 * @date 2025-11-25
 */
@Service
public class CustomerSubjectGroupServiceImpl extends ServiceImpl<CustomerSubjectGroupMapper, CustomerSubjectGroup> implements ICustomerSubjectGroupService {

    @Resource
    private CustomerSubjectGroupMapper mapper;

    @Override
    public void add(CustomerSubjectGroup customerSubjectGroup) {

    }

    @Override
    public void update(CustomerSubjectGroup customerSubjectGroup) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public Page<CustomerSubjectGroupResp> query(CustomerSubjectGroupReq req) {
        return null;
    }
}
