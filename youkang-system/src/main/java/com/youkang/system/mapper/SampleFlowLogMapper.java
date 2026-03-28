package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.system.domain.SampleFlowLog;
import com.youkang.system.domain.req.order.SampleFlowLogQueryReq;
import com.youkang.system.domain.resp.order.SampleFlowLogResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 样品流程流转日志Mapper接口
 *
 * @author youkang
 */
public interface SampleFlowLogMapper extends BaseMapper<SampleFlowLog> {

    /**
     * 分页查询流程流转日志
     *
     * @param page 分页参数
     * @param query 查询条件
     * @return 流程流转日志分页结果
     */
    Page<SampleFlowLogResp> queryPage(Page<SampleFlowLogResp> page, @Param("query") SampleFlowLogQueryReq query);

    /**
     * 根据生产编号查询流转历史
     *
     * @param produceId 生产编号
     * @return 流转历史列表
     */
    List<SampleFlowLogResp> queryByProduceId(@Param("produceId") Long produceId);
}
