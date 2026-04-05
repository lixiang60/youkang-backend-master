package com.youkang.system.service.price;

import com.baomidou.mybatisplus.extension.service.IService;
import com.youkang.system.domain.UnitPrice;

/**
 * 课题组测序单价Service接口
 *
 * @author youkang
 */
public interface IUnitPriceService extends IService<UnitPrice> {

    /**
     * 根据课题组ID获取单价
     */
    UnitPrice getByGroupId(Integer groupId);

    /**
     * 更新或新增课题组单价
     */
    void saveOrUpdateUnitPrice(Integer groupId, java.math.BigDecimal unitPrice, String username);
}
