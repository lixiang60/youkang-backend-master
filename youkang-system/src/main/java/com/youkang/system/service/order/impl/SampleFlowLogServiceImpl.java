package com.youkang.system.service.order.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.system.domain.SampleFlowLog;
import com.youkang.system.domain.req.order.SampleFlowLogQueryReq;
import com.youkang.system.domain.resp.order.SampleFlowLogResp;
import com.youkang.system.mapper.SampleFlowLogMapper;
import com.youkang.system.service.order.ISampleFlowLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 样品流程流转日志Service业务层处理
 *
 * @author youkang
 */
@Service
public class SampleFlowLogServiceImpl extends ServiceImpl<SampleFlowLogMapper, SampleFlowLog> implements ISampleFlowLogService {

    @Autowired
    private SampleFlowLogMapper sampleFlowLogMapper;

    @Override
    public IPage<SampleFlowLogResp> queryPage(SampleFlowLogQueryReq req) {
        Page<SampleFlowLogResp> page = new Page<>(req.getPageNum(), req.getPageSize());
        return sampleFlowLogMapper.queryPage(page, req);
    }

    @Override
    public List<SampleFlowLogResp> queryByProduceId(Long produceId) {
        return sampleFlowLogMapper.queryByProduceId(produceId);
    }

    @Override
    public void recordLog(Long produceId, String operation, String flowName, String beforeFlowName,
                          String templatePlateNo, String templateHoleNo, String plateNo, String holeNo,
                          String layout, Integer holeNumber, String originConcentration,
                          String returnState, String remark) {
        SampleFlowLog log = new SampleFlowLog();
        log.setProduceId(produceId);
        log.setOperation(operation);
        log.setFlowName(flowName);
        log.setBeforeFlowName(beforeFlowName);
        log.setTemplatePlateNo(templatePlateNo);
        log.setTemplateHoleNo(templateHoleNo);
        log.setPlateNo(plateNo);
        log.setHoleNo(holeNo);
        log.setLayout(layout);
        log.setHoleNumber(holeNumber);
        log.setOriginConcentration(originConcentration);
        log.setReturnState(returnState);
        log.setRemark(remark);
        log.setOperator(SecurityUtils.getUsername());
        log.setOperateTime(LocalDateTime.now());
        this.save(log);
    }

    @Override
    public void batchRecordLog(List<Long> produceIds, String operation, String flowName, String beforeFlowName,
                               String templatePlateNo, String templateHoleNo, String plateNo, String holeNo,
                               String layout, String originConcentration, String returnState, String remark) {
        if (produceIds == null || produceIds.isEmpty()) {
            return;
        }
        String operator = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();
        List<SampleFlowLog> logs = new ArrayList<>();
        for (Long produceId : produceIds) {
            SampleFlowLog log = new SampleFlowLog();
            log.setProduceId(produceId);
            log.setOperation(operation);
            log.setFlowName(flowName);
            log.setBeforeFlowName(beforeFlowName);
            log.setTemplatePlateNo(templatePlateNo);
            log.setTemplateHoleNo(templateHoleNo);
            log.setPlateNo(plateNo);
            log.setHoleNo(holeNo);
            log.setLayout(layout);
            log.setOriginConcentration(originConcentration);
            log.setReturnState(returnState);
            log.setRemark(remark);
            log.setOperator(operator);
            log.setOperateTime(now);
            logs.add(log);
        }
        this.saveBatch(logs);
    }
}
