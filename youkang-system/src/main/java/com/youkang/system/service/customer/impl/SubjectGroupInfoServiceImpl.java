package com.youkang.system.service.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import com.youkang.system.mapper.SubjectGroupInfoMapper;
import com.youkang.system.domain.SubjectGroupInfo;
import com.youkang.system.service.customer.ISubjectGroupInfoService;

/**
 * 课题组信息Service业务层处理
 *
 * ServiceImpl 已经实现了 IService 中的所有方法，包括：
 * - 单条/批量 新增、删除、修改、查询
 * - 分页查询
 * - 条件查询
 * 等常用方法，可直接使用
 *
 * @author youkang
 * @date 2025-01-20
 */
@Service
public class SubjectGroupInfoServiceImpl extends ServiceImpl<SubjectGroupInfoMapper, SubjectGroupInfo> implements ISubjectGroupInfoService
{
    /**
     * 分页查询课题组信息列表
     *
     * @param subjectGroupInfo 查询条件（包含分页参数）
     * @return 分页结果
     */
    @Override
    public IPage<SubjectGroupInfo> queryPage(SubjectGroupInfo subjectGroupInfo)
    {
        // 创建分页对象
        Page<SubjectGroupInfo> page = new Page<>(subjectGroupInfo.getPageNum(), subjectGroupInfo.getPageSize());

        // 构建查询条件（使用 Lambda 表达式，类型安全）
        LambdaQueryWrapper<SubjectGroupInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(subjectGroupInfo.getName()),
                     SubjectGroupInfo::getName, subjectGroupInfo.getName())
               .eq(StringUtils.isNotEmpty(subjectGroupInfo.getRegion()),
                   SubjectGroupInfo::getRegion, subjectGroupInfo.getRegion())
               .like(StringUtils.isNotEmpty(subjectGroupInfo.getSalesPerson()),
                     SubjectGroupInfo::getSalesPerson, subjectGroupInfo.getSalesPerson())
               .eq(StringUtils.isNotEmpty(subjectGroupInfo.getPaymentMethod()),
                   SubjectGroupInfo::getPaymentMethod, subjectGroupInfo.getPaymentMethod())
               .like(StringUtils.isNotEmpty(subjectGroupInfo.getContactPerson()),
                     SubjectGroupInfo::getContactPerson, subjectGroupInfo.getContactPerson())
               .like(StringUtils.isNotEmpty(subjectGroupInfo.getContactPhone()),
                     SubjectGroupInfo::getContactPhone, subjectGroupInfo.getContactPhone())
               .orderByDesc(SubjectGroupInfo::getId);

        // 分页查询
        return this.page(page, wrapper);
    }
}
