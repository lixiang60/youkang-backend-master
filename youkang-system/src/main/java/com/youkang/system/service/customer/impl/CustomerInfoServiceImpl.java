package com.youkang.system.service.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.enums.PayMentMethodEnum;
import com.youkang.common.utils.StringUtils;
import com.youkang.system.domain.CustomerInfo;
import com.youkang.system.domain.SubjectGroupInfo;
import com.youkang.system.domain.req.customer.CustomerQueryReq;
import com.youkang.system.domain.resp.customer.CustomerResp;
import com.youkang.system.domain.resp.customer.CustomerSelectorResp;
import com.youkang.system.mapper.CustomerInfoMapper;
import com.youkang.system.mapper.SubjectGroupInfoMapper;
import com.youkang.system.service.customer.ICustomerInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户信息Service业务层处理
 *
 * @author youkang
 * @date 2025-11-20
 */
@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfo> implements ICustomerInfoService {

    @Resource
    private CustomerInfoMapper mapper;

    @Resource
    private SubjectGroupInfoMapper subjectGroupInfoMapper;

    @Override
    public IPage<CustomerResp> queryPage(CustomerQueryReq queryReq) {
        // 创建分页对象
        Page<CustomerInfo> page = new Page<>(queryReq.getPageNum(), queryReq.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<CustomerInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(queryReq.getCustomerName()),
                        CustomerInfo::getCustomerName, queryReq.getCustomerName())
                .eq(StringUtils.isNotEmpty(queryReq.getRegion()),
                        CustomerInfo::getRegion, queryReq.getRegion())
                .like(StringUtils.isNotEmpty(queryReq.getPhone()),
                        CustomerInfo::getPhone, queryReq.getPhone())
                .like(StringUtils.isNotEmpty(queryReq.getEmail()),
                        CustomerInfo::getEmail, queryReq.getEmail())
                .eq(StringUtils.isNotEmpty(queryReq.getCustomerLevel()),
                        CustomerInfo::getCustomerLevel, queryReq.getCustomerLevel())
                .eq(StringUtils.isNotEmpty(queryReq.getStatus()),
                        CustomerInfo::getStatus, queryReq.getStatus())
                .like(StringUtils.isNotEmpty(queryReq.getSalesPerson()),
                        CustomerInfo::getSalesPerson, queryReq.getSalesPerson())
                .eq(StringUtils.isNotEmpty(queryReq.getPaymentMethod()),
                        CustomerInfo::getPaymentMethod, queryReq.getPaymentMethod())
                .like(StringUtils.isNotEmpty(queryReq.getCompany()),
                        CustomerInfo::getCompany, queryReq.getCompany())
                .eq(queryReq.getSubjectGroupId() != null,
                        CustomerInfo::getSubjectGroupId, queryReq.getSubjectGroupId())
                .orderByDesc(CustomerInfo::getId);

        // 分页查询
        IPage<CustomerInfo> entityPage = this.page(page, wrapper);

        // 转换为响应对象
        Page<CustomerResp> respPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        List<CustomerResp> respList = entityPage.getRecords().stream()
                .map(this::convertToResp)
                .toList();
        respPage.setRecords(respList);

        return respPage;
    }

    @Override
    public CustomerResp getDetail(Integer id) {
        CustomerInfo entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        return convertToResp(entity);
    }

    @Override
    public Page<CustomerSelectorResp> customerSelector(String queryString) {
        return mapper.customerSelector(Page.of(1, 20), queryString);
    }

    /**
     * 实体转响应对象
     */
    private CustomerResp convertToResp(CustomerInfo entity) {
        CustomerResp resp = new CustomerResp();
        BeanUtils.copyProperties(entity, resp);

        // 转换结算方式名称
        if (StringUtils.isNotEmpty(entity.getPaymentMethod())) {
            String methodName = PayMentMethodEnum.getDescByCode(entity.getPaymentMethod());
            resp.setPaymentMethodName(methodName);
        }

        // 转换课题组名称
        if (entity.getSubjectGroupId() != null) {
            SubjectGroupInfo subjectGroup = subjectGroupInfoMapper.selectById(entity.getSubjectGroupId());
            if (subjectGroup != null) {
                resp.setSubjectGroupName(subjectGroup.getName());
            }
        }

        // TODO: 转换客户等级名称（根据枚举）
        // TODO: 转换状态名称（根据枚举）

        return resp;
    }
}
