package com.youkang.system.service.price;

import com.youkang.system.domain.req.price.PriceConfigBatchUpdateReq;
import com.youkang.system.domain.req.price.PriceConfigUpdateReq;
import com.youkang.system.domain.resp.price.PriceConfigResp;

import java.util.List;

/**
 * 价格配置Service接口
 *
 * @author youkang
 */
public interface IPriceConfigService {

    /**
     * 查询课题组价格列表（合并模板和自定义价格）
     *
     * @param groupId 课题组ID
     * @return 价格配置列表
     */
    List<PriceConfigResp> getPriceListByGroupId(Integer groupId);

    /**
     * 修改价格配置
     * 支持修改范围、单价、计算方式、状态
     * 修改范围时会联动更新相邻的价格配置
     *
     * @param req 修改请求
     */
    void updatePriceConfig(PriceConfigUpdateReq req);

    /**
     * 批量修改价格配置
     *
     * @param req 批量修改请求
     */
    void batchUpdatePriceConfig(PriceConfigBatchUpdateReq req);

    /**
     * 重置价格配置（删除自定义价格，恢复为模板默认）
     *
     * @param id 自定义价格ID
     */
    void resetPriceConfig(Long id);
}
