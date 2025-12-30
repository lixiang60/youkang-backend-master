package com.youkang.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.youkang.system.domain.OrderInfo;
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
     * @param orderInfo 查询条件
     * @return 订单响应分页结果
     */
    Page<OrderResp> queryPage(Page<OrderResp> page, @Param("query") OrderInfo orderInfo);

    /**
     * 查询订单信息列表（关联客户和课题组）
     *
     * @param orderInfo 查询条件
     * @return 订单响应列表
     */
    List<OrderResp> queryList(@Param("query") OrderInfo orderInfo);

    /**
     * 根据订单ID查询订单详情（关联客户和课题组）
     *
     * @param orderId 订单ID
     * @return 订单响应
     */
    OrderResp queryById(@Param("orderId") String orderId);

}
