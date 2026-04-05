package com.youkang.system.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.core.redis.RedisCache;
import com.youkang.common.exception.ServiceException;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.StringUtils;
import com.youkang.common.utils.mail.MailTemplate;
import com.youkang.common.utils.mail.MailUtils;
import com.youkang.system.domain.OrderInfo;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.OrderAddReq;
import com.youkang.system.domain.req.order.OrderQueryReq;
import com.youkang.system.domain.req.order.OrderRangeQueryReq;
import com.youkang.system.domain.req.order.OrderUpdateReq;
import com.youkang.system.domain.req.order.SampleAddReq;
import com.youkang.system.domain.req.order.SampleBatchAddReq;
import com.youkang.system.domain.req.order.SampleItemReq;
import com.youkang.system.domain.resp.order.OrderResp;
import com.youkang.system.domain.resp.order.OrderWithSamplesResp;
import com.youkang.system.domain.resp.order.SampleResp;
import com.youkang.system.mapper.OrderInfoMapper;
import com.youkang.system.mapper.SampleInfoMapper;
import com.youkang.system.service.order.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 订单信息Service业务层处理
 *
 * @author youkang
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private static final DateTimeFormatter ORDER_ID_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter PRODUCE_ID_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    private static final String PRODUCE_ID_KEY_PREFIX = "sample:produce:";

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SampleInfoMapper sampleInfoMapper;

    @Autowired
    private MailUtils mailUtils;

    @Autowired
    private RedisCache redisCache;

    /**
     * 新增订单（自动生成订单ID）
     *
     * @param orderInfo 订单信息
     * @return 是否成功
     */
    @Override
    public boolean save(OrderInfo orderInfo) {
        // 自动生成订单ID：时间戳(yyyyMMddHHmmss) + 毫秒后3位
        if (StringUtils.isEmpty(orderInfo.getOrderId())) {
            orderInfo.setOrderId(generateOrderId());
        }
        if (orderInfo.getCreateTime() == null) {
            orderInfo.setCreateTime(LocalDateTime.now());
        }
        return super.save(orderInfo);
    }
    @Transactional
    @Override
    public void addOrder(OrderAddReq req){

        String username = SecurityUtils.getUsername();
        if(req.getIsEmail()==1){
            log.info("发送邮件---");
            MailTemplate mailTemplate = MailTemplate.builder()
                    .title("订单信息")
                    .recipientName(req.getCustomerInfo().getCustomerName())
                    .content("订单信息")
                    .senderName("有康科技")
                    .contactPhone("13888888888")
                    .contactEmail(req.getCustomerInfo().getEmail())
                    .build();
            mailUtils.sendHtmlByTemplate(req.getCustomerInfo().getEmail(),"订单信息",mailTemplate);
        }
        if (StringUtils.isEmpty(req.getOrderId())) {
            req.setOrderId(generateOrderId());
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(req.getOrderId());
        orderInfo.setGeneration(req.getGeneration());
        orderInfo.setCustomerId(req.getCustomerInfo().getCustomerId());
        orderInfo.setGroupId(req.getCustomerInfo().getSubjectGroupId());
        orderInfo.setOrderType("公司录入");
        orderInfo.setIsAsync(1);
        orderInfo.setRemark(req.getRemark());
        orderInfo.setCreateBy(username);
        orderInfo.setCreateTime(LocalDateTime.now());
        orderInfo.setBelongCompany(req.getBelongCompany());
        orderInfo.setProduceCompany(req.getProduceCompany());
        orderInfo.setGenNo(req.getGenNo());
        orderInfo.setOrderStatus("订单生成");
        orderInfo.setStatusTime(LocalDateTime.now());
        orderInfoMapper.insert(orderInfo);
        //如果样品信息不为空，则添加样品信息
        if (req.getSampleInfoList() != null && !req.getSampleInfoList().isEmpty()){
            List<SampleInfo> sampleInfoList = req.getSampleInfoList().stream().map(item -> {
                SampleInfo sampleInfo = new SampleInfo();
                BeanUtils.copyProperties(item, sampleInfo);
                sampleInfo.setOrderId(req.getOrderId());
                sampleInfo.setCreateUser(username);
                // 自动生成生产编号
                if (sampleInfo.getProduceId() == null) {
                    sampleInfo.setProduceId(generateProduceId());
                }
                // 同步订单的公司信息到样品
                sampleInfo.setBelongCompany(req.getBelongCompany());
                sampleInfo.setProduceCompany(req.getProduceCompany());
                return sampleInfo;
            }).collect(Collectors.toList());
            sampleInfoMapper.insert(sampleInfoList);
        }

    }

    @Override
    public void addSample(SampleAddReq req) {
        SampleInfo sampleInfo = new SampleInfo();
        BeanUtils.copyProperties(req, sampleInfo);
        LambdaQueryWrapper<SampleInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SampleInfo::getOrderId, req.getOrderId());
        List<SampleInfo> sampleInfos = sampleInfoMapper.selectList(queryWrapper);
        List<String> sampleIdList = sampleInfos.stream().map(SampleInfo::getSampleId).toList();
        if (sampleIdList.contains(req.getSampleId())) {
            throw new ServiceException("样品编号已存在");
        }
        sampleInfo.setCreateUser(SecurityUtils.getUsername());
        sampleInfo.setCreateTime(LocalDateTime.now());
        // 自动生成生产编号
        sampleInfo.setProduceId(generateProduceId());

        // 从订单中获取公司信息并同步到样品
        OrderInfo orderInfo = orderInfoMapper.selectById(req.getOrderId());
        if (orderInfo != null) {
            sampleInfo.setBelongCompany(orderInfo.getBelongCompany());
            sampleInfo.setProduceCompany(orderInfo.getProduceCompany());
        }

        sampleInfoMapper.insert(sampleInfo);
    }

    @Override
    public void batchAddSample(SampleBatchAddReq req) {
        if (req.getSampleList() != null && !req.getSampleList().isEmpty()) {
            String username = SecurityUtils.getUsername();

            // 收集所有订单ID，批量查询订单信息
            Set<String> orderIds = req.getSampleList().stream()
                    .map(SampleItemReq::getOrderId)
                    .filter(StringUtils::isNotEmpty)
                    .collect(Collectors.toSet());

            // 批量查询订单信息，构建订单ID到订单的映射
            Map<String, OrderInfo> orderMap = new HashMap<>();
            if (!orderIds.isEmpty()) {
                List<OrderInfo> orders = orderInfoMapper.selectBatchIds(orderIds);
                orderMap = orders.stream()
                        .collect(Collectors.toMap(OrderInfo::getOrderId, o -> o));
            }

            // 最终的订单映射
            final Map<String, OrderInfo> finalOrderMap = orderMap;

            List<SampleInfo> sampleInfoList = req.getSampleList().stream().map(item -> {
                SampleInfo sampleInfo = new SampleInfo();
                BeanUtils.copyProperties(item, sampleInfo);
                sampleInfo.setCreateUser(username);
                sampleInfo.setCreateTime(LocalDateTime.now());
                // 自动生成生产编号
                if (sampleInfo.getProduceId() == null) {
                    sampleInfo.setProduceId(generateProduceId());
                }
                // 从订单中同步公司信息
                if (StringUtils.isNotEmpty(item.getOrderId())) {
                    OrderInfo order = finalOrderMap.get(item.getOrderId());
                    if (order != null) {
                        sampleInfo.setBelongCompany(order.getBelongCompany());
                        sampleInfo.setProduceCompany(order.getProduceCompany());
                    }
                }
                return sampleInfo;
            }).collect(Collectors.toList());
            sampleInfoMapper.insert(sampleInfoList);
        }
    }

    /**
     * 生成订单ID
     * 格式：yyyyMMddHHmmss + 毫秒后3位（精确到毫秒，保证唯一性）
     *
     * @return 订单ID
     */
    private String generateOrderId() {
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(ORDER_ID_FORMATTER);
        int millis = (int) (System.currentTimeMillis() % 1000);
        return dateStr + String.format("%03d", millis);
    }

    /**
     * 生成生产编号
     * 格式：yyMMdd + 4位序号（当日递增）
     * 使用 Redis 原子递增保证并发安全
     *
     * @return 生产编号，如 2503030001
     */
    private Long generateProduceId() {
        // 获取今日日期字符串yyMMdd
        String dateStr = LocalDate.now().format(PRODUCE_ID_FORMATTER);
        String key = PRODUCE_ID_KEY_PREFIX + dateStr;

        // Redis原子递增
        Long seq = redisCache.increment(key);
        if (seq == 1) {
            // 第一次生成时设置2天过期，自动清理旧数据
            redisCache.expire(key, 2, TimeUnit.DAYS);
        }

        // 拼接生成10位生产编号：yyMMdd + 4位序号，转为 Long
        return Long.parseLong(dateStr + String.format("%04d", seq));
    }

    /**
     * 分页查询订单信息列表
     *
     * @param req 订单查询请求
     * @return 订单信息分页结果
     */
    @Override
    public IPage<OrderResp> queryPage(OrderQueryReq req) {
        Page<OrderResp> page = new Page<>(req.getPageNum(), req.getPageSize());
        return orderInfoMapper.queryPage(page, req);
    }

    /**
     * 查询订单信息列表（关联客户和课题组）
     *
     * @param req 订单查询请求
     * @return 订单响应列表
     */
    @Override
    public List<OrderResp> queryList(OrderQueryReq req) {
        return orderInfoMapper.queryList(req);
    }

    /**
     * 根据订单ID查询订单详情（关联客户和课题组）
     *
     * @param orderId 订单ID
     * @return 订单响应
     */
    @Override
    public OrderResp queryById(String orderId) {
        return orderInfoMapper.queryById(orderId);
    }

    /**
     * 导入订单数据
     *
     * @param orderList 订单数据列表
     * @param isUpdateSupport 是否支持更新已存在的数据
     * @param operName 操作人
     * @return 导入结果信息
     */
    @Override
    public String importOrder(List<OrderInfo> orderList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(orderList) || orderList.isEmpty()) {
            throw new ServiceException("导入订单数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (OrderInfo order : orderList) {
            try {
                // 验证订单id是否已存在
                OrderInfo existOrder = this.getById(order.getOrderId());
                if (StringUtils.isNull(existOrder)) {
                    // 新增订单
                    order.setCreateBy(operName);
                    order.setCreateTime(LocalDateTime.now());
                    this.save(order);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、订单 ").append(order.getOrderId()).append(" 导入成功");
                } else if (isUpdateSupport) {
                    // 更新订单
                    this.updateById(order);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、订单 ").append(order.getOrderId()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、订单 ").append(order.getOrderId()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、订单 " + order.getOrderId() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 修改订单信息
     *
     * @param req 订单更新请求
     * @return 是否成功
     */
    @Override
    public boolean updateOrder(OrderUpdateReq req) {
        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(req, orderInfo);
        return this.updateById(orderInfo);
    }

    /**
     * 根据条件范围查询订单信息
     *
     * @param req 范围查询请求
     * @return 订单信息列表
     */
    @Override
    public List<OrderResp> queryByRange(OrderRangeQueryReq req) {
        return orderInfoMapper.queryByRange(req);
    }

    /**
     * 根据订单号查询订单及样品信息
     *
     * @param orderId 订单号
     * @return 订单及样品信息
     */
    @Override
    public OrderWithSamplesResp queryOrderWithSamples(String orderId) {
        // 查询订单信息
        OrderResp orderInfo = orderInfoMapper.queryById(orderId);
        if (orderInfo == null) {
            throw new ServiceException("订单不存在：" + orderId);
        }

        // 查询样品列表
        List<SampleResp> sampleList = sampleInfoMapper.queryByOrderId(orderId);

        // 组装返回结果
        OrderWithSamplesResp resp = new OrderWithSamplesResp();
        resp.setOrderInfo(orderInfo);
        resp.setSampleList(sampleList);
        return resp;
    }

    /**
     * 根据条件范围查询订单及样品信息
     *
     * @param req 范围查询请求
     * @return 订单及样品信息列表
     */
    @Override
    public List<OrderWithSamplesResp> queryOrderWithSamplesByRange(OrderRangeQueryReq req) {
        // 1. 查询符合条件的订单列表
        List<OrderResp> orderList = orderInfoMapper.queryByRange(req);
        if (orderList == null || orderList.isEmpty()) {
            return List.of();
        }

        // 2. 提取订单号列表
        List<String> orderIds = orderList.stream()
                .map(OrderResp::getOrderId)
                .collect(Collectors.toList());

        // 3. 批量查询所有订单的样品
        List<SampleResp> allSamples = sampleInfoMapper.queryByOrderIds(orderIds);

        // 4. 按订单号分组样品
        Map<String, List<SampleResp>> sampleMap = allSamples.stream()
                .collect(Collectors.groupingBy(SampleResp::getOrderId));

        // 5. 组装返回结果
        return orderList.stream().map(order -> {
            OrderWithSamplesResp resp = new OrderWithSamplesResp();
            resp.setOrderInfo(order);
            resp.setSampleList(sampleMap.getOrDefault(order.getOrderId(), List.of()));
            return resp;
        }).collect(Collectors.toList());
    }

    /**
     * 定时任务：检查订单状态流转（订单生成 -> 订单出库）
     * 当订单下所有样品的 flow_name 都在 模板取消/报告成功/报告取消 范围内时，自动出库
     */
    @Override
    public void checkOrderOutbound() {
        List<String> orderIds = orderInfoMapper.selectOutboundOrderIds();
        if (orderIds == null || orderIds.isEmpty()) {
            return;
        }
        log.info("订单出库检查：发现 {} 个满足出库条件的订单", orderIds.size());
        orderInfoMapper.batchUpdateOrderOutbound(orderIds);
        log.info("订单出库检查：已将 {} 个订单状态更新为【订单出库】", orderIds.size());
    }

    /**
     * 定时任务：检查订单自动完成（订单出库 -> 订单完成）
     * 订单出库超过4小时后自动完成
     */
    @Override
    public void checkOrderComplete() {
        List<String> orderIds = orderInfoMapper.selectCompletedOrderIds(4);
        if (orderIds == null || orderIds.isEmpty()) {
            return;
        }
        log.info("订单完成检查：发现 {} 个出库超过4小时的订单", orderIds.size());
        orderInfoMapper.batchUpdateOrderComplete(orderIds);
        log.info("订单完成检查：已将 {} 个订单状态更新为【订单完成】", orderIds.size());
    }
}
