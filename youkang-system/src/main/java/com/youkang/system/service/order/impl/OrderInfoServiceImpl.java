package com.youkang.system.service.order.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.youkang.common.exception.ServiceException;
import com.youkang.common.utils.SecurityUtils;
import com.youkang.common.utils.StringUtils;
import com.youkang.system.domain.OrderInfo;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.OrderAddReq;
import com.youkang.system.domain.req.order.OrderQueryReq;
import com.youkang.system.domain.req.order.OrderUpdateReq;
import com.youkang.system.domain.req.order.SampleAddReq;
import com.youkang.system.domain.req.order.SampleBatchAddReq;
import com.youkang.system.domain.req.order.SampleItemReq;
import com.youkang.system.domain.resp.order.OrderResp;
import com.youkang.system.mapper.OrderInfoMapper;
import com.youkang.system.mapper.SampleInfoMapper;
import com.youkang.system.service.order.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 订单信息Service业务层处理
 *
 * @author youkang
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private static final DateTimeFormatter ORDER_ID_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final Random RANDOM = new Random();

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private SampleInfoMapper sampleInfoMapper;

    /**
     * 新增订单（自动生成订单ID）
     *
     * @param orderInfo 订单信息
     * @return 是否成功
     */
    @Override
    public boolean save(OrderInfo orderInfo) {
        // 自动生成订单ID：时间戳(yyyyMMddHHmm) + 4位随机数
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
            //todo 发送邮件
            log.info("发送邮件---");
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
        orderInfoMapper.insert(orderInfo);
        //如果样品信息不为空，则添加样品信息
        if (req.getSampleInfoList() != null && !req.getSampleInfoList().isEmpty()){
            List<SampleInfo> sampleInfoList = req.getSampleInfoList().stream().map(item -> {
                SampleInfo sampleInfo = new SampleInfo();
                BeanUtils.copyProperties(item, sampleInfo);
                sampleInfo.setOrderId(req.getOrderId());
                sampleInfo.setCreateUser(username);
                return sampleInfo;
            }).collect(Collectors.toList());
            sampleInfoMapper.insert(sampleInfoList);
        }

    }

    @Override
    public void addSample(SampleAddReq req) {
        SampleInfo sampleInfo = new SampleInfo();
        BeanUtils.copyProperties(req, sampleInfo);
        sampleInfoMapper.insert(sampleInfo);
    }

    @Override
    public void batchAddSample(SampleBatchAddReq req) {
        if (req.getSampleList() != null && !req.getSampleList().isEmpty()) {
            String username = SecurityUtils.getUsername();
            List<SampleInfo> sampleInfoList = req.getSampleList().stream().map(item -> {
                SampleInfo sampleInfo = new SampleInfo();
                BeanUtils.copyProperties(item, sampleInfo);
                sampleInfo.setCreateUser(username);
                return sampleInfo;
            }).collect(Collectors.toList());
            sampleInfoMapper.insert(sampleInfoList);
        }
    }

    /**
     * 生成订单ID
     * 格式：yyyyMMddHHmm + 4位随机数
     *
     * @return 订单ID
     */
    private String generateOrderId() {
        String timeStr = LocalDateTime.now().format(ORDER_ID_FORMATTER);
        String randomStr = String.format("%04d", RANDOM.nextInt(10000));
        return timeStr + randomStr;
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
}
