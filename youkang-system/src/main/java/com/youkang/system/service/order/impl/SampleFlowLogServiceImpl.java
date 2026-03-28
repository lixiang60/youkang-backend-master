package com.youkang.system.service.order.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.system.domain.SampleFlowLog;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.SampleFlowLogQueryReq;
import com.youkang.system.domain.resp.order.SampleFlowLogResp;
import com.youkang.system.mapper.SampleFlowLogMapper;
import com.youkang.system.mapper.SampleInfoMapper;
import com.youkang.system.service.order.ISampleFlowLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private SampleInfoMapper sampleInfoMapper;

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
    public void recordLog(Long produceId, String operation, String beforeFlowName, String remark) {
        if (produceId == null) {
            return;
        }

        // 查询样品完整信息
        SampleInfo sampleInfo = sampleInfoMapper.selectById(produceId);
        if (sampleInfo == null) {
            return;
        }

        // 构建日志对象（直接使用样品表中已存储的holeNumber）
        SampleFlowLog log = buildFlowLog(sampleInfo, operation, beforeFlowName, remark);
        this.save(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRecordLog(List<Long> produceIds, String operation, String beforeFlowName, String remark) {
        if (produceIds == null || produceIds.isEmpty()) {
            return;
        }

        String operator = SecurityUtils.getUsername();
        LocalDateTime now = LocalDateTime.now();
        List<SampleFlowLog> logs = new ArrayList<>();

        for (Long produceId : produceIds) {
            // 查询样品完整信息
            SampleInfo sampleInfo = sampleInfoMapper.selectById(produceId);
            if (sampleInfo == null) {
                continue;
            }

            // 构建日志对象（直接使用样品表中已存储的holeNumber）
            SampleFlowLog log = buildFlowLog(sampleInfo, operation, beforeFlowName, remark);
            log.setOperator(operator);
            log.setOperateTime(now);
            logs.add(log);
        }

        if (!logs.isEmpty()) {
            this.saveBatch(logs);
        }
    }

    /**
     * 构建流程日志对象
     */
    private SampleFlowLog buildFlowLog(SampleInfo sampleInfo, String operation, String beforeFlowName, String remark) {
        SampleFlowLog log = new SampleFlowLog();
        log.setProduceId(sampleInfo.getProduceId());
        log.setOperation(operation);
        log.setFlowName(sampleInfo.getFlowName());
        log.setBeforeFlowName(beforeFlowName);
        log.setTemplatePlateNo(sampleInfo.getTemplatePlateNo());
        log.setTemplateHoleNo(sampleInfo.getTemplateHoleNo());
        log.setPlateNo(sampleInfo.getPlateNo());
        log.setHoleNo(sampleInfo.getHoleNo());
        log.setLayout(sampleInfo.getLayout());
        log.setHoleNumber(sampleInfo.getHoleNumber());
        log.setOriginConcentration(sampleInfo.getOriginConcentration());
        log.setReturnState(sampleInfo.getReturnState());
        log.setRemark(remark);
        log.setOperator(SecurityUtils.getUsername());
        log.setOperateTime(LocalDateTime.now());
        return log;
    }
}
