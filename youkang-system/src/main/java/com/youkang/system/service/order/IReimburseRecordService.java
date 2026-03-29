package com.youkang.system.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.ReimburseRecord;
import com.youkang.system.domain.req.order.*;
import com.youkang.system.domain.resp.order.ReimburseRecordResp;

import java.util.List;

/**
 * 返还记录Service接口
 *
 * @author youkang
 */
public interface IReimburseRecordService extends IService<ReimburseRecord> {

    /**
     * 分页查询返还记录
     *
     * @param req 查询条件
     * @return 分页结果
     */
    IPage<ReimburseRecordResp> queryPage(ReimburseRecordQueryReq req);

    /**
     * 根据ID查询详情
     *
     * @param id 记录ID
     * @return 返还记录详情
     */
    ReimburseRecordResp queryById(Long id);

    /**
     * 新增返还记录
     *
     * @param req 新增请求
     */
    void addRecord(SampleReturnReq req);

    /**
     * 安排返还
     *
     * @param req 确认请求
     */
    void confirm(ReimburseCommonReq req);

    /**
     * 删除返还记录
     *
     * @param id 记录ID
     */
    void delete(ReimburseCommonReq req);
}
