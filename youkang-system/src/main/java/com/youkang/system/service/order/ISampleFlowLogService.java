package com.youkang.system.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.SampleFlowLog;
import com.youkang.system.domain.req.order.SampleFlowLogQueryReq;
import com.youkang.system.domain.resp.order.SampleFlowLogResp;

import java.util.List;

/**
 * 样品流程流转日志Service接口
 *
 * @author youkang
 */
public interface ISampleFlowLogService extends IService<SampleFlowLog> {

    /**
     * 分页查询流程流转日志
     *
     * @param req 查询条件
     * @return 分页结果
     */
    IPage<SampleFlowLogResp> queryPage(SampleFlowLogQueryReq req);

    /**
     * 根据生产编号查询流转历史
     *
     * @param produceId 生产编号
     * @return 流转历史列表
     */
    List<SampleFlowLogResp> queryByProduceId(Long produceId);

    /**
     * 记录流程流转日志（自动查询样品完整信息）
     *
     * @param produceId 生产编号
     * @param operation 操作类型
     * @param beforeFlowName 流转前流程名称（可为空，为空时自动从样品信息获取）
     * @param remark 备注
     */
    void recordLog(Long produceId, String operation, String beforeFlowName, String remark);

    /**
     * 批量记录流程流转日志（自动查询样品完整信息）
     *
     * @param produceIds 生产编号列表
     * @param operation 操作类型
     * @param beforeFlowName 流转前流程名称（可为空，为空时自动从样品信息获取）
     * @param remark 备注
     */
    void batchRecordLog(List<Long> produceIds, String operation, String beforeFlowName, String remark);
}
