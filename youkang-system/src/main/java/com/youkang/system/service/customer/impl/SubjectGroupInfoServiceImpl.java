package com.youkang.system.service.customer.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.enums.PayMentMethodEnum;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.StringUtils;
import com.youkang.system.domain.SubjectGroupInfo;
import com.youkang.system.domain.req.subjectgroup.BatchUpdateRep;
import com.youkang.system.domain.req.subjectgroup.SubjectGroupQueryReq;
import com.youkang.system.domain.resp.customer.SubjectGroupSelectorResp;
import com.youkang.system.domain.resp.subjectgroup.SubjectGroupResp;
import com.youkang.system.mapper.SubjectGroupInfoMapper;
import com.youkang.system.service.customer.ISubjectGroupInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课题组信息Service业务层处理
 *
 * @author youkang
 * @date 2025-01-20
 */
@Service
public class SubjectGroupInfoServiceImpl extends ServiceImpl<SubjectGroupInfoMapper, SubjectGroupInfo> implements ISubjectGroupInfoService {

    @Override
    public IPage<SubjectGroupResp> queryPage(SubjectGroupQueryReq queryReq) {
        // 创建分页对象
        Page<SubjectGroupInfo> page = new Page<>(queryReq.getPageNum(), queryReq.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<SubjectGroupInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(queryReq.getName()),
                        SubjectGroupInfo::getName, queryReq.getName())
                .eq(StringUtils.isNotEmpty(queryReq.getRegion()),
                        SubjectGroupInfo::getRegion, queryReq.getRegion())
                .like(StringUtils.isNotEmpty(queryReq.getSalesPerson()),
                        SubjectGroupInfo::getSalesPerson, queryReq.getSalesPerson())
                .eq(StringUtils.isNotEmpty(queryReq.getPaymentMethod()),
                        SubjectGroupInfo::getPaymentMethod, queryReq.getPaymentMethod())
                .like(StringUtils.isNotEmpty(queryReq.getContactPerson()),
                        SubjectGroupInfo::getContactPerson, queryReq.getContactPerson())
                .like(StringUtils.isNotEmpty(queryReq.getContactPhone()),
                        SubjectGroupInfo::getContactPhone, queryReq.getContactPhone())
                .orderByDesc(SubjectGroupInfo::getId);

        // 分页查询
        IPage<SubjectGroupInfo> entityPage = this.page(page, wrapper);

        // 转换为响应对象
        Page<SubjectGroupResp> respPage = new Page<>(entityPage.getCurrent(), entityPage.getSize(), entityPage.getTotal());
        List<SubjectGroupResp> respList = entityPage.getRecords().stream()
                .map(this::convertToResp)
                .toList();
        respPage.setRecords(respList);

        return respPage;
    }

    @Override
    public SubjectGroupResp getDetail(Integer id) {
        SubjectGroupInfo entity = this.getById(id);
        if (entity == null) {
            return null;
        }
        return convertToResp(entity);
    }

    @Override
    public Page<SubjectGroupSelectorResp> getSubjectGroupSelector(String queryString) {
        // 查询 SubjectGroupInfo
        LambdaQueryWrapper<SubjectGroupInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryString)) {
            wrapper.and(w -> w.like(SubjectGroupInfo::getId, queryString)
                    .or()
                    .like(SubjectGroupInfo::getName, queryString));
        }

        Page<SubjectGroupInfo> subjectGroupPage = this.page(Page.of(1, 20), wrapper);

        // 转换为 SubjectGroupSelectorResp
        Page<SubjectGroupSelectorResp> respPage = new Page<>(subjectGroupPage.getCurrent(), subjectGroupPage.getSize(), subjectGroupPage.getTotal());
        List<SubjectGroupSelectorResp> respList = subjectGroupPage.getRecords().stream()
                .map(info -> {
                    SubjectGroupSelectorResp resp = new SubjectGroupSelectorResp();
                    resp.setId(info.getId());
                    resp.setName(info.getName());
                    resp.setContactAddress(info.getContactAddress());
                    return resp;
                }).toList();
        respPage.setRecords(respList);
        return respPage;
    }

    @Override
    public boolean saveSubjectGroup(SubjectGroupInfo entity) {
        String username = SecurityUtils.getUsername();
        entity.setCreateBy(username);
        entity.setCreateTime(LocalDateTime.now());
        return this.save( entity);
    }

    @Override
    public void editBatch(BatchUpdateRep updateReq) {
        this.lambdaUpdate()
                .in(SubjectGroupInfo::getId, updateReq.getIds())
                .set(StringUtils.isNotBlank(updateReq.getPaymentMethod()),SubjectGroupInfo::getPaymentMethod, updateReq.getPaymentMethod())
                .set(StringUtils.isNotBlank(updateReq.getRegion()),SubjectGroupInfo::getRegion, updateReq.getRegion())
                .set(StringUtils.isNotBlank(updateReq.getSalesPerson()),SubjectGroupInfo::getSalesPerson, updateReq.getSalesPerson())
                .set(StringUtils.isNotBlank(updateReq.getInvoiceTitle()),SubjectGroupInfo::getInvoiceTitle, updateReq.getInvoiceTitle())
                .update();
    }

    /**
     * 实体转响应对象
     */
    private SubjectGroupResp convertToResp(SubjectGroupInfo entity) {
        SubjectGroupResp resp = new SubjectGroupResp();
        BeanUtils.copyProperties(entity, resp);

        // 转换结算方式名称
        if (StringUtils.isNotEmpty(entity.getPaymentMethod())) {
            String methodName = PayMentMethodEnum.getDescByCode(entity.getPaymentMethod());
            resp.setPaymentMethodName(methodName);
        }

        // TODO: 转换公司名称（根据 companyId 查询）
        // resp.setCompanyName(...);

        return resp;
    }
}
