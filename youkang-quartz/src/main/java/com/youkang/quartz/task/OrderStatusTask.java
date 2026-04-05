package com.youkang.quartz.task;

import com.youkang.system.service.order.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单状态流转定时任务
 *
 * @author youkang
 */
@Component("orderStatusTask")
@Slf4j
public class OrderStatusTask {

    @Autowired
    private IOrderInfoService orderInfoService;

    /**
     * 检查订单出库：订单生成 -> 订单出库
     * 当订单下所有样品的流程名称都在终态范围内时触发
     */
    public void checkOrderOutbound() {
        log.info("定时任务[订单出库检查]开始执行...");
        try {
            orderInfoService.checkOrderOutbound();
        } catch (Exception e) {
            log.error("定时任务[订单出库检查]执行异常", e);
        }
        log.info("定时任务[订单出库检查]执行结束");
    }

    /**
     * 检查订单自动完成：订单出库 -> 订单完成
     * 订单出库超过4小时后触发
     */
    public void checkOrderComplete() {
        log.info("定时任务[订单自动完成]开始执行...");
        try {
            orderInfoService.checkOrderComplete();
        } catch (Exception e) {
            log.error("定时任务[订单自动完成]执行异常", e);
        }
        log.info("定时任务[订单自动完成]执行结束");
    }
}
