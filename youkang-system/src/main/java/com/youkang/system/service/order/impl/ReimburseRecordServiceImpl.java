package com.youkang.system.service.order.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.exception.ServiceException;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.system.domain.ReimburseRecord;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.*;
import com.youkang.system.domain.resp.order.OrderResp;
import com.youkang.system.domain.resp.order.ReimburseRecordResp;
import com.youkang.system.domain.resp.order.SampleResp;
import com.youkang.system.mapper.OrderInfoMapper;
import com.youkang.system.mapper.ReimburseRecordMapper;
import com.youkang.system.mapper.SampleInfoMapper;
import com.youkang.system.service.order.IReimburseRecordService;
import com.youkang.system.service.order.ISampleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 返还记录Service业务层处理
 *
 * @author youkang
 */
@Service
public class ReimburseRecordServiceImpl extends ServiceImpl<ReimburseRecordMapper, ReimburseRecord> implements IReimburseRecordService {

    @Autowired
    private ReimburseRecordMapper reimburseRecordMapper;

    @Autowired
    private ISampleInfoService sampleInfoService;

    @Autowired
    private SampleInfoMapper sampleInfoMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    public IPage<ReimburseRecordResp> queryPage(ReimburseRecordQueryReq req) {
        Page<ReimburseRecordResp> page = new Page<>(req.getPageNum(), req.getPageSize());
        return reimburseRecordMapper.queryPage(page, req);
    }

    @Override
    public ReimburseRecordResp queryById(Long id) {
        return reimburseRecordMapper.queryById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRecord(SampleReturnReq req) {
        String orderId = req.getOrderId();
        String newReimburseType = req.getReimburseType();
        List<Long> newProduceIdList = req.getProduceIdList();

        // 查询该订单是否存在状态不为"返还已处理"且返还类型相同的记录
        ReimburseRecord existingRecord = this.lambdaQuery()
                .eq(ReimburseRecord::getOrderId, orderId)
                .eq(ReimburseRecord::getReimburseType, newReimburseType)
                .ne(ReimburseRecord::getStatus, "返还已处理")
                .one();

        if (existingRecord != null) {
            // 存在相同返还类型的未处理记录，合并 produceIds 并去重
            Set<Long> mergedProduceIdSet = new LinkedHashSet<>(newProduceIdList);
            Arrays.stream(existingRecord.getProduceIds().split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .forEach(mergedProduceIdSet::add);
            List<Long> allProduceIds = new ArrayList<>(mergedProduceIdSet);
            String updatedProduceIds = allProduceIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            // 根据合并后的 produceIds 查询样品数量
            int sampleCount = sampleInfoMapper.getSampleCount(orderId, allProduceIds);

            this.lambdaUpdate()
                    .set(ReimburseRecord::getProduceIds, updatedProduceIds)
                    .set(ReimburseRecord::getReimburseCount, sampleCount)
                    .set(ReimburseRecord::getUpdateUser, SecurityUtils.getUsername())
                    .set(ReimburseRecord::getUpdateTime, LocalDateTime.now())
                    .eq(ReimburseRecord::getId, existingRecord.getId())
                    .update();
        } else {
            // 不存在相同返还类型的未处理记录，新增
            int sampleCount = sampleInfoMapper.getSampleCount(orderId, newProduceIdList);
            OrderResp orderResp = orderInfoMapper.queryById(orderId);
            String username = SecurityUtils.getUsername();
            LocalDateTime now = LocalDateTime.now();

            ReimburseRecord reimburseRecord = new ReimburseRecord();
            reimburseRecord.setCustomerName(orderResp.getCustomerName())
                    .setOrderId(orderId)
                    .setScheduleTime(now)
                    .setScheduler(username)
                    .setReimburseType(newReimburseType)
                    .setReimburseCount(sampleCount)
                    .setProduceIds(newProduceIdList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                    .setStatus("返还单生成")
                    .setCreateUser(username)
                    .setUpdateUser(username)
                    .setCreateTime(now)
                    .setProduceCompany(orderResp.getProduceCompany())
                    .setBelongCompany(orderResp.getBelongCompany());
            this.save(reimburseRecord);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(ReimburseCommonReq req) {
        List<ReimburseConfirmReq> reimburseConfirmReqs = req.getReimburseConfirmReqs();
        List<Long> idList = reimburseConfirmReqs.stream().map(ReimburseConfirmReq::getId).toList();
        List<ReimburseRecord> list = this.lambdaQuery().in(ReimburseRecord::getId, idList).list();
        list.forEach(record -> {
            if ("返还已处理".equals(record.getStatus())){
                throw new ServiceException("存在已处理的返还记录");
            }
        });
        // 更新返还记录状态
        this.lambdaUpdate().in(ReimburseRecord::getId, idList)
                .set(ReimburseRecord::getStatus, "返还已处理")
                .set(ReimburseRecord::getReimburseTime, LocalDateTime.now())
                .set(ReimburseRecord::getReimburser, SecurityUtils.getUsername())
                .update();
        //更新样品返还状态
        reimburseConfirmReqs.forEach(reimburseConfirmReq -> {
            List<Long> produceIdList = Arrays.stream(reimburseConfirmReq.getProduceIds().split(",")).map(Long::parseLong).toList();
            sampleInfoService.updateSampleReimburseStatus(produceIdList,reimburseConfirmReq.getReimburseType());
        });

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(ReimburseCommonReq req) {
        List<ReimburseConfirmReq> reqs = req.getReimburseConfirmReqs();
        reqs.forEach(reimburseConfirmReq -> {
            Long id = reimburseConfirmReq.getId();
            List<Long> produceIdList = Arrays.stream(reimburseConfirmReq.getProduceIds().split(",")).map(Long::parseLong).toList();
            ReimburseRecord record = this.getById(id);
            if (record == null) {
                throw new ServiceException("返还记录不存在");
            }
            if ("返还已处理".equals(record.getStatus())) {
                throw new ServiceException("已返还的记录不能删除");
            }
            this.removeById(id);
            sampleInfoService.removeSampleReimburseStatus(produceIdList, reimburseConfirmReq.getReimburseType());
        });
    }
}
