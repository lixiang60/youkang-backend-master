package com.youkang.system.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.OrderInfo;
import com.youkang.system.domain.req.order.OrderAddReq;
import com.youkang.system.domain.req.order.OrderQueryReq;
import com.youkang.system.domain.req.order.OrderRangeQueryReq;
import com.youkang.system.domain.req.order.OrderUpdateReq;
import com.youkang.system.domain.req.order.SampleAddReq;
import com.youkang.system.domain.req.order.SampleBatchAddReq;
import com.youkang.system.domain.resp.order.OrderResp;
import com.youkang.system.domain.resp.order.OrderPriceCalcResp;
import com.youkang.system.domain.resp.order.OrderWithSamplesResp;

import java.util.List;

/**
 * 订单信息Service接口
 *
 * @author youkang
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 分页查询订单信息列表
     *
     * @param req 订单查询请求
     * @return 订单信息分页结果
     */
    IPage<OrderResp> queryPage(OrderQueryReq req);

    /**
     * 查询订单信息列表（关联客户和课题组）
     *
     * @param req 订单查询请求
     * @return 订单响应列表
     */
    List<OrderResp> queryList(OrderQueryReq req);

    /**
     * 根据订单ID查询订单详情（关联客户和课题组）
     *
     * @param orderId 订单ID
     * @return 订单响应
     */
    OrderResp queryById(String orderId);

    /**
     * 导入订单数据
     *
     * @param orderList 订单数据列表
     * @param isUpdateSupport 是否支持更新已存在的数据
     * @param operName 操作人
     * @return 导入结果信息
     */
    String importOrder(List<OrderInfo> orderList, Boolean isUpdateSupport, String operName);

    /**
     * 新增订单信息
     *
     * @param orderAddReq 订单信息
     * @return 是否成功
     */
    void addOrder(OrderAddReq req);

    /**
     * 通过订单新增样品
     *
     * @param req 订单信息
     */
    void addSample(SampleAddReq req);

    /**
     * 通过订单批量新增样品
     *
     * @param req 批量添加样品请求
     */
    void batchAddSample(SampleBatchAddReq req);

    /**
     * 修改订单信息
     *
     * @param req 订单更新请求
     * @return 是否成功
     */
    boolean updateOrder(OrderUpdateReq req);

    /**
     * 根据条件范围查询订单信息
     *
     * @param req 范围查询请求
     * @return 订单信息列表
     */
    List<OrderResp> queryByRange(OrderRangeQueryReq req);

    /**
     * 根据订单号查询订单及样品信息
     *
     * @param orderId 订单号
     * @return 订单及样品信息
     */
    OrderWithSamplesResp queryOrderWithSamples(String orderId);

    /**
     * 根据条件范围查询订单及样品信息
     *
     * @param req 范围查询请求
     * @return 订单及样品信息列表
     */
    List<OrderWithSamplesResp> queryOrderWithSamplesByRange(OrderRangeQueryReq req);

    /**
     * 定时任务：检查订单状态流转（订单生成 -> 订单出库）
     * 当订单下所有样品的流程名称都在终态范围内时，自动更新为出库
     */
    void checkOrderOutbound();

    /**
     * 定时任务：检查订单自动完成（订单出库 -> 订单完成）
     * 订单出库超过4小时后自动完成
     */
    void checkOrderComplete();

    /**
     * 计算订单价格
     * 按样品编号分组，数量叠加，根据价格配置计算单价和总价
     *
     * @param orderId 订单号
     * @return 价格计算结果列表
     */
    List<OrderPriceCalcResp> calcOrderPrice(String orderId);

    /**
     * 订单状态回退（清空上一级状态）
     * 销售回款 → 订单完成 → 订单出库 → 订单生成
     *
     * @param orderId 订单号
     */
    void resetOrderStatus(String orderId);
}
