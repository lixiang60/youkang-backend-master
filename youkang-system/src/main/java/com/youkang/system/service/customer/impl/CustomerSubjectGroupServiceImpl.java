package com.youkang.system.service.customer.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.system.domain.CustomerInfo;
import com.youkang.system.domain.SubjectGroupInfo;
import com.youkang.system.domain.req.CustomerUpdateReq;
import com.youkang.system.domain.req.DeleteReq;
import com.youkang.system.domain.req.customer.CustomerSubjectGroupReq;
import com.youkang.system.domain.resp.customer.CustomerSubjectGroupResp;
import com.youkang.system.mapper.CustomerInfoMapper;
import com.youkang.system.mapper.SubjectGroupInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.youkang.system.mapper.CustomerSubjectGroupMapper;
import com.youkang.system.domain.CustomerSubjectGroup;
import com.youkang.system.service.customer.ICustomerSubjectGroupService;
import java.util.List;
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

    @Resource
    private CustomerInfoMapper infoMapper;

    @Resource
    private SubjectGroupInfoMapper subjectInfoMapper;

    @Override
    public void add(CustomerSubjectGroup customerSubjectGroup) {
        save(customerSubjectGroup);
    }

    @Override
    public void update(CustomerUpdateReq req) {
        List<CustomerSubjectGroup> list = this.lambdaQuery().in(CustomerSubjectGroup::getId, req.getIds()).list();
        List<Integer> cutomerIdList = list.stream().map(CustomerSubjectGroup::getCustomerId).toList();
        List<Integer> subjectGroupList = list.stream().map(CustomerSubjectGroup::getSubjectGroupId).toList();
        LambdaUpdateWrapper<CustomerInfo> wrapper1 = new LambdaUpdateWrapper<>();
        LambdaUpdateWrapper<SubjectGroupInfo> wrapper2 = new LambdaUpdateWrapper<>();
        wrapper1.set(CustomerInfo::getRegion,req.getRegion())
                .set(CustomerInfo::getPaymentMethod,req.getPaymentMethod())
                .set(CustomerInfo::getSalesPerson,req.getSalesPerson())
                .set(CustomerInfo::getCustomerUnit,req.getCustomerUnit())
                .in(CustomerInfo::getId,cutomerIdList);
        wrapper2.set(SubjectGroupInfo::getRegion,req.getRegion())
                .set(SubjectGroupInfo::getPaymentMethod,req.getPaymentMethod())
                .set(SubjectGroupInfo::getSalesPerson,req.getSalesPerson())
                .in(SubjectGroupInfo::getId,subjectGroupList);
        infoMapper.update(wrapper1);
        subjectInfoMapper.update(wrapper2);
    }

    @Override
    public void delete(DeleteReq req) {
        mapper.deleteBatchIds(req.getIds());
    }

    @Override
    public Page<CustomerSubjectGroupResp> query(CustomerSubjectGroupReq req) {
        return mapper.query(Page.of(req.getPageNum(),req.getPageSize()),req);
    }
}
