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
     * 记录流程流转日志
     *
     * @param produceId 生产编号
     * @param operation 操作类型
     * @param flowName 流程名称
     * @param beforeFlowName 流转前流程名称
     * @param templatePlateNo 模板板号
     * @param templateHoleNo 模板孔号
     * @param plateNo 板号
     * @param holeNo 孔号
     * @param layout 排版方式
     * @param holeNumber 孔号数
     * @param originConcentration 原浓度
     * @param returnState 返回状态
     * @param remark 备注
     */
    void recordLog(Long produceId, String operation, String flowName, String beforeFlowName,
                   String templatePlateNo, String templateHoleNo, String plateNo, String holeNo,
                   String layout, Integer holeNumber, String originConcentration,
                   String returnState, String remark);

    /**
     * 批量记录流程流转日志
     *
     * @param produceIds 生产编号列表
     * @param operation 操作类型
     * @param flowName 流程名称
     * @param beforeFlowName 流转前流程名称
     * @param templatePlateNo 模板板号
     * @param templateHoleNo 模板孔号
     * @param plateNo 板号
     * @param holeNo 孔号
     * @param layout 排版方式
     * @param originConcentration 原浓度
     * @param returnState 返回状态
     * @param remark 备注
     */
    void batchRecordLog(List<Long> produceIds, String operation, String flowName, String beforeFlowName,
                        String templatePlateNo, String templateHoleNo, String plateNo, String holeNo,
                        String layout, String originConcentration, String returnState, String remark);
}
