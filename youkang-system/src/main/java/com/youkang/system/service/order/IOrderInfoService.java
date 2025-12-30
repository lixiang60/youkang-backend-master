package com.youkang.system.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.OrderInfo;
import com.youkang.system.domain.SampleInfo;
import com.youkang.system.domain.req.order.OrderAddReq;
import com.youkang.system.domain.req.order.SampleAddReq;
import com.youkang.system.domain.resp.order.OrderResp;

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
     * @param orderInfo 订单信息查询条件
     * @return 订单信息分页结果
     */
    IPage<OrderResp> queryPage(OrderInfo orderInfo);

    /**
     * 查询订单信息列表（关联客户和课题组）
     *
     * @param orderInfo 订单信息查询条件
     * @return 订单响应列表
     */
    List<OrderResp> queryList(OrderInfo orderInfo);

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
     * @param req 订单信息
     */
    void batchAddSample(List<SampleInfo> req);


}
