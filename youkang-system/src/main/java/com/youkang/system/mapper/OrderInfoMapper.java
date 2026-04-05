package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.system.domain.OrderInfo;
import com.youkang.system.domain.req.order.OrderQueryReq;
import com.youkang.system.domain.req.order.OrderRangeQueryReq;
import com.youkang.system.domain.resp.order.OrderResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单信息Mapper接口
 *
 * @author youkang
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 分页查询订单信息（关联客户和课题组）
     *
     * @param page 分页参数
     * @param req 查询条件
     * @return 订单响应分页结果
     */
    Page<OrderResp> queryPage(Page<OrderResp> page, @Param("query") OrderQueryReq req);

    /**
     * 查询订单信息列表（关联客户和课题组）
     *
     * @param req 查询条件
     * @return 订单响应列表
     */
    List<OrderResp> queryList(@Param("query") OrderQueryReq req);

    /**
     * 根据订单ID查询订单详情（关联客户和课题组）
     *
     * @param orderId 订单ID
     * @return 订单响应
     */
    OrderResp queryById(@Param("orderId") String orderId);

    /**
     * 根据条件范围查询订单信息（关联客户和课题组）
     *
     * @param query 范围查询条件
     * @return 订单响应列表
     */
    List<OrderResp> queryByRange(@Param("query") OrderRangeQueryReq query);

    /**
     * 查询所有状态为"订单生成"且所有样品流程已完成的订单ID列表
     *
     * @return 满足出库条件的订单ID列表
     */
    List<String> selectOutboundOrderIds();

    /**
     * 批量更新订单状态为出库
     *
     * @param orderIds 订单ID列表
     */
    void batchUpdateOrderOutbound(@Param("orderIds") List<String> orderIds);

    /**
     * 查询状态为"订单出库"且超过指定小时数的订单ID列表
     *
     * @param hours 超时小时数
     * @return 超时的订单ID列表
     */
    List<String> selectCompletedOrderIds(@Param("hours") int hours);

    /**
     * 批量更新订单状态为完成
     *
     * @param orderIds 订单ID列表
     */
    void batchUpdateOrderComplete(@Param("orderIds") List<String> orderIds);

}
