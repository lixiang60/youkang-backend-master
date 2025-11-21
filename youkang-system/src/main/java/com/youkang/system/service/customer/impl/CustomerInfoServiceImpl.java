package com.youkang.system.service.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import com.youkang.system.mapper.CustomerInfoMapper;
import com.youkang.system.domain.CustomerInfo;
import com.youkang.system.service.customer.ICustomerInfoService;

/**
 * 客户信息Service业务层处理
 *
 * ServiceImpl 已经实现了 IService 中的所有方法，包括：
 * - 单条/批量 新增、删除、修改、查询
 * - 分页查询
 * - 条件查询
 * 等常用方法，可直接使用
 *
 * @author youkang
 * @date 2025-11-20
 */
@Service
public class CustomerInfoServiceImpl
        extends ServiceImpl<CustomerInfoMapper, CustomerInfo>
        implements ICustomerInfoService
{
    /**
     * 分页查询客户信息列表
     *
     * @param customerInfo 查询条件（包含分页参数）
     * @return 分页结果
     */
    @Override
    public IPage<CustomerInfo> queryPage(CustomerInfo customerInfo)
    {
        // 创建分页对象
        Page<CustomerInfo> page = new Page<>(customerInfo.getPageNum(), customerInfo.getPageSize());

        // 构建查询条件（使用 Lambda 表达式，类型安全）
        LambdaQueryWrapper<CustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(customerInfo.getCustomerName()),
                     CustomerInfo::getCustomerName, customerInfo.getCustomerName())
               .eq(StringUtils.isNotEmpty(customerInfo.getRegion()),
                   CustomerInfo::getRegion, customerInfo.getRegion())
               .like(StringUtils.isNotEmpty(customerInfo.getPhone()),
                     CustomerInfo::getPhone, customerInfo.getPhone())
               .like(StringUtils.isNotEmpty(customerInfo.getEmail()),
                     CustomerInfo::getEmail, customerInfo.getEmail())
               .eq(StringUtils.isNotEmpty(customerInfo.getCustomerLevel()),
                   CustomerInfo::getCustomerLevel, customerInfo.getCustomerLevel())
               .eq(StringUtils.isNotEmpty(customerInfo.getStatus()),
                   CustomerInfo::getStatus, customerInfo.getStatus())
               .like(StringUtils.isNotEmpty(customerInfo.getSalesPerson()),
                     CustomerInfo::getSalesPerson, customerInfo.getSalesPerson())
               .eq(StringUtils.isNotEmpty(customerInfo.getPaymentMethod()),
                   CustomerInfo::getPaymentMethod, customerInfo.getPaymentMethod())
               .like(StringUtils.isNotEmpty(customerInfo.getCompany()),
                     CustomerInfo::getCompany, customerInfo.getCompany())
               .orderByDesc(CustomerInfo::getCreateTime);

        // 分页查询
        return this.page(page, wrapper);
    }
}
