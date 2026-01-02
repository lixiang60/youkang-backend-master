package com.youkang.system.service.customer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.SubjectGroupInfo;
import com.youkang.system.domain.req.subjectgroup.BatchUpdateRep;
import com.youkang.system.domain.req.subjectgroup.SubjectGroupQueryReq;
import com.youkang.system.domain.resp.customer.SubjectGroupSelectorResp;
import com.youkang.system.domain.resp.subjectgroup.SubjectGroupResp;

/**
 * 课题组信息Service接口
 *
 * @author youkang
 * @date 2025-01-20
 */
public interface ISubjectGroupInfoService extends IService<SubjectGroupInfo> {

    /**
     * 分页查询课题组信息列表
     *
     * @param queryReq 查询条件
     * @return 分页结果
     */
    IPage<SubjectGroupResp> queryPage(SubjectGroupQueryReq queryReq);

    /**
     * 获取课题组详情
     *
     * @param id 课题组ID
     * @return 课题组详情
     */
    SubjectGroupResp getDetail(Integer id);

    /**
     * 获取课题组选择器数据
     *
     * @param queryString 查询字符串
     * @return 选择器数据
     */
    Page<SubjectGroupSelectorResp> getSubjectGroupSelector(String queryString);

    boolean saveSubjectGroup(SubjectGroupInfo entity);

    void editBatch(BatchUpdateRep updateReq);
}
